package server;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import response.StatusCode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
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
    }

    // Serve a HTML form allowing to upload files via browser.
    // GET request entry point.
    public void handleGET(HttpExchange exchange) throws IOException {
        byte [] form = ("<html><body>" +
                "<form method=\"POST\" " +
                "enctype=\"multipart/form-data\" action=\""
                + this.handlerRoot + "\">" +
                "File to upload: <input type=\"file\" " +
                // "accept=\".wig,.fastq,.gff,.sgr\" "
                "name=\"uploadfile\"/>" +
                "<input type=\"submit\" value=\"Press\"/>" +
                "to upload the file.</form>" +
                "</body></html>").getBytes();
        Debug.log("Serving upload form...");
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(StatusCode.OK, form.length);
        OutputStream out = exchange.getResponseBody();
        out.write(form);
        out.close();
    }

    // Handle a 'multipart/form-data' upload.
    // POST request entry point.
    public void handlePOST(final HttpExchange exchange) throws Exception {
        Debug.log("Receiving a file from client...");

        // Save uploaded files to memory or a temp directory.
        FileUpload fileUpload = new FileUpload();
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(64*1024, new File(this.tmpDir));
        fileUpload.setFileItemFactory(fileItemFactory);
        final Headers headers = exchange.getRequestHeaders();
        for (String header : headers.keySet()) {
            System.out.println(header + ": " + headers.getFirst(header));
        }
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
            @SuppressWarnings("deprecation")
            public int getContentLength() {
                    return Integer.parseInt(headers.getFirst("Content-Length"));
                }

            @Override
            public InputStream getInputStream() throws IOException {
                return exchange.getRequestBody();
            }
        };
        List<FileItem> fileItems = fileUpload.parseRequest(ctx);

        // Move uploaded files to uploadDir.
        URI requestURI = exchange.getRequestURI();
        HashMap<String,String> reqParams = new HashMap<>();
        Util.parseURI(requestURI, reqParams);
        String absUploadPath = null;
        if (reqParams.containsKey("path")) {
            Debug.log("Using legacy upload method ('upload?path=/absolute/path').");
            absUploadPath = reqParams.get("path");
        }

        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                File outFile = new File(absUploadPath == null
                        ? this.uploadDir + fileItem.getName()
                        : absUploadPath);
                fileItem.write(outFile);
                Debug.log("Successfully saved the uploaded file to '"
                        + outFile.toString() + "'.");
            }
        }

        // Report success to the client.
        byte [] resp = "OK".getBytes();
        exchange.sendResponseHeaders(StatusCode.CREATED, resp.length);
        OutputStream out = exchange.getResponseBody();
        out.write(resp);
        out.close();
    }
}