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
import java.util.List;

// A class that implements file upload functionality.
// It needs access to the raw InputStream of the request,
// so can't be implemented with the Command interface.
public class UploadHandler {
    private String uploadDir;

    public UploadHandler (String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void handleGET(HttpExchange exchange) throws IOException {
        byte [] form = ("<html><body>" +
                "<form method=\"POST\" " +
                "enctype=\"multipart/form-data\" action=\"/upload\">" +
                "File to upload: <input type=\"file\" " +
                // "accept=\".wig,.fastq,.gff,.sgr\" "
                "name=\"uploadfile\"/>" +
                "<input type=\"submit\" value=\"Press\"/>" +
                "to upload the file.</form>" +
                "</body></html>").getBytes();
        exchange.sendResponseHeaders(StatusCode.OK, form.length);
        OutputStream out = exchange.getResponseBody();
        out.write(form);
        out.close();
    }

    public void handlePOST(final HttpExchange exchange) throws Exception {
        FileUpload fileUpload = new FileUpload();
        // TODO: don't hard-code the temp directory location.
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(4096, new File("/tmp"));
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

        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                File outFile = new File(this.uploadDir + fileItem.getName());
                fileItem.write(outFile);
            }
        }

        byte [] resp = "OK".getBytes();
        exchange.sendResponseHeaders(StatusCode.CREATED, resp.length);
        OutputStream out = exchange.getResponseBody();
        out.write(resp);
        out.close();
    }
}
