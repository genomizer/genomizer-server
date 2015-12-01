package transfer;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import database.containers.ChainFile;
import database.containers.FileTuple;
import database.containers.GenomeFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import response.HttpStatusCode;
import server.Debug;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

// Implements file uploading.
// Needs access to the raw InputStream of the request,
// and so can't be implemented with the Command interface.
public class UploadHandler {
    private String handlerRoot;
    private String uploadDir;
    private String tmpDir;

    public UploadHandler (String handlerRoot, String uploadDir, String tmpDir) {
        this.uploadDir   = uploadDir;
        this.handlerRoot = handlerRoot;
        this.tmpDir      = tmpDir;

        new File(tmpDir).mkdirs();
    }

    // Serve a HTML form allowing to upload files via browser.
    // GET request entry point.
    public void handleGET(HttpExchange exchange) throws IOException {
        byte [] form = ("<html><body>" +
                "<form method=\"POST\" " +
                "enctype=\"multipart/form-data\" action=\""
                + this.handlerRoot + "/upload.test" + "\">" +
                "File to upload: <input type=\"file\" " +
                // "accept=\".wig,.fastq,.gff,.sgr\" "
                "name=\"uploadfile\"/>" +
                "<input type=\"submit\" value=\"Press\"/>" +
                "to upload the file.</form>" +
                "</body></html>").getBytes();
        Debug.log("Serving upload form...");
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(HttpStatusCode.OK, form.length);
        OutputStream out = exchange.getResponseBody();
        out.write(form);
        out.close();
        Debug.log("END OF EXCHANGE\n------------------");
    }

    // Handle a 'multipart/form-data' upload.
    // POST request entry point.
    public void handlePOST(final HttpExchange exchange) throws Exception {
        // Retrieve the file path of the file being uploaded.
        URI requestURI = exchange.getRequestURI();
        String absUploadPath = extractAbsUploadPath(requestURI);

        // Receive FileItems from the client.
        List<FileItem> fileItems = receiveFileItems(exchange, absUploadPath);

        // Move uploaded file to uploadDir.
        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {

                File outFile = new File(absUploadPath == null
                        ? this.uploadDir + fileItem.getName()
                        : absUploadPath);

                outFile.getParentFile().mkdirs();
                fileItem.write(outFile);
                commitFile(absUploadPath, fileItem);

                Debug.log("Successfully saved the uploaded file to '"
                        + outFile.toString() + "'.");
                fileItem.delete();
            }
        }
    }

    // Receive file items from the client.
    private List<FileItem> receiveFileItems(final HttpExchange exchange,
                                            String absUploadPath)
            throws SQLException, IOException, FileUploadException {
        List<FileItem> fileItems;
        try {
            Debug.log("Receiving a file from client...");

            // Save uploaded files to memory or a temp directory.
            FileUpload fileUpload = new FileUpload();
            DiskFileItemFactory fileItemFactory =
              new DiskFileItemFactory(64 * 1024, new File(this.tmpDir));
            fileUpload.setFileItemFactory(fileItemFactory);
            final Headers headers = exchange.getRequestHeaders();
            for (String header : headers.keySet()) {
                Debug.log(header + ": " + headers.getFirst(header));
            }
            @SuppressWarnings("deprecation")
            RequestContext ctx = new RequestContext() {
                @Override
                public String getCharacterEncoding() {
                    return headers.getFirst("Content-encoding");
                }

                @Override
                public String getContentType() {
                    return headers.getFirst("Content-Type");
                }

                @Override
                public int getContentLength() {
                    return 0;
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return exchange.getRequestBody();
                }
            };
            fileItems = fileUpload.parseRequest(ctx);

        /* Send response to client */
            OutputStream out = exchange.getResponseBody();
            byte[] resp = null;
            if (fileItems.size() > 0) {
                // Report success to the client.
                resp = "OK".getBytes();
                exchange.sendResponseHeaders(HttpStatusCode.OK, resp.length);

            } else {
                resp = "ERROR".getBytes();
                exchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST,
                                             resp.length);
            }

            out.write(resp);
            out.close();
            Debug.log("END OF EXCHANGE\n------------------");
        }
        catch (Exception ex) {
            // Upload was interrupted. We need to remove the stale
            // 'In Progress' tuple from the DB.
            rollbackFile(absUploadPath);
            throw ex;
        }
        return fileItems;
    }

    private String extractAbsUploadPath(URI requestURI) {
        HashMap<String,String> reqParams = new HashMap<>();
        String reqPath = Util.parseURI(requestURI, reqParams);
        String absUploadPath = null;
        if (reqParams.containsKey("path")) {
            Debug.log("Using legacy upload method " +
                      "('upload?path=/absolute/path').");
            absUploadPath = reqParams.get("path");
        }
        else {
            String relPath = reqPath.substring(this.handlerRoot.length() + 1);
            absUploadPath = this.uploadDir + relPath;
        }
        return absUploadPath;
    }

    // Remove the 'In Progress' tuple corresponding to a given file.
    private void rollbackFile(String absUploadPath)
        throws  SQLException, IOException {

    Debug.log("Rolling back file, upload was not successful");

        try ( DatabaseAccessor db = Command.initDB()) {
            FileTuple ft = db.getFileTupleInProgress(absUploadPath);
            if (ft != null) {
        Debug.log("Removing file tuple " + ft.path);
                db.deleteFile(ft.path);
                return;
            }

            GenomeFile gf = db.getGenomeReleaseFileInProgress(absUploadPath);
            if (gf != null) {
        Debug.log("Removing genome release file " + gf.fileName);
                db.removeGenomeReleaseFile(gf.genomeVersion, gf.fileName);
                return;
            }

            ChainFile cf = db.getChainFileInProgress(absUploadPath);
            if (cf != null) {
        Debug.log("Removing chain file " + cf.fileName);
                db.removeChainFiles(cf.fromVersion, cf.toVersion);
                return;
            }


        }
    }

    // Verify the file's integrity and mark it as available for downloading.
    private void commitFile(String absUploadPath, FileItem fileItem)
            throws SQLException, ValidateException, IOException {
        try( DatabaseAccessor db = Command.initDB() )
        {
            //String actualMD5 = DigestUtils.md5Hex(fileItem.getInputStream());

            FileTuple  ft = db.getFileTupleInProgress(absUploadPath);
            if (ft != null) {
                //verifyOrUpdateMD5(ft, actualMD5, db);
                int count = db.markReadyForDownload(ft);
                checkMarkReadyForDownloadSucceeded(count, ft.filename);
                db.updateFileSize(ft, fileItem.getSize());
                return;
            }

            GenomeFile gf = db.getGenomeReleaseFileInProgress(absUploadPath);
            if (gf != null) {
                //verifyOrUpdateMD5(gf, actualMD5, db);
                int count = db.markReadyForDownload(gf);
                checkMarkReadyForDownloadSucceeded(count, gf.fileName);
                return;
            }

            ChainFile cf = db.getChainFileInProgress(absUploadPath);
            if (cf != null) {
                //verifyOrUpdateMD5(cf, actualMD5, db);
                int count = db.markReadyForDownload(cf);
                checkMarkReadyForDownloadSucceeded(count, cf.fileName);
                return;
            }

            Debug.log("File '" + absUploadPath +
                      "' not registered in the database!");
            Files.delete(new File(absUploadPath).toPath());
            throw new ValidateException(HttpStatusCode.BAD_REQUEST,
                                        "Request to upload a file that " +
                                        "wasn't previously registered!");
        }
    }

    // Helper function for 'commitFile'.
    private void checkMarkReadyForDownloadSucceeded (int countUpdated,
                                                     String fileName)
            throws ValidateException {
        if (countUpdated <= 0) {
            throw new ValidateException(HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Couldn't mark file '" + fileName
                                        + "' as ready for download!");
        }
    }

    // Verify the file's MD5 checksum.
    // If the client hasn't sent us one, just save the actual checksum.
//    private void verifyOrUpdateMD5(HasCheckSumMD5 file, String actualMD5, DatabaseAccessor db)
//            throws SQLException, ValidateException {
//        String checkSumMD5 = file.getCheckSumMD5();
//
//        if (checkSumMD5 != null) {
//            if (!actualMD5.equals(checkSumMD5)) {
//                Debug.log("MD5 verification error. Expected: "
//                        + checkSumMD5 + ", actual: " + actualMD5 + ".");
//                throw new ValidateException(HttpStatusCode.BAD_REQUEST,
//                        "Incorrect checksum!");
//            }
//            else {
//                Debug.log("MD5 checksum verified successfully.");
//                return;
//            }
//        }
//        else {
//            Debug.log("MD5 checksum not provided by the client: "
//                    + "updating the database with actual checksum.");
//            file.setCheckSumMD5(db, actualMD5);
//            return;
//        }
//    }
}
