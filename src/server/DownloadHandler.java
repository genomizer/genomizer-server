package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import response.StatusCode;

import java.io.*;

public class DownloadHandler {
    private String downloadDir;

    public DownloadHandler (String downloadDir) {
        this.downloadDir = downloadDir;
    }

    private void serveIndex(HttpExchange exchange) throws IOException {
        // TODO: Generate index.
        byte [] index = ("<html><body>" +
                "TODO" +
                "</body></html>").getBytes();
        exchange.sendResponseHeaders(200, index.length);
        OutputStream out = exchange.getResponseBody();
        out.write(index);
        out.close();
    }

    private void serveFile(HttpExchange exchange, File file) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Description", "File Transfer");
        headers.set("Content-Type", "application/octet-stream");
        headers.set("Content-Disposition", "attachment; filename="
                + file.getName());
        headers.set("Expires", "0");
        headers.set("Cache-Control", "must-revalidate");
        headers.set("Pragma", "public");
        exchange.sendResponseHeaders(200, file.length());
        OutputStream out = exchange.getResponseBody();

        byte[] buf = new byte[8192];
        InputStream in = new FileInputStream(file);
        int c = 0;
        while ((c = in.read(buf, 0, buf.length)) > 0) {
            out.write(buf, 0, c);
        }

        out.close();
        in.close();
    }

    public void handleGET(HttpExchange exchange) throws IOException {
        String requestURI = exchange.getRequestURI().toString();
        Debug.log("Request URI: " + requestURI);
        if (requestURI.equals("/download")) {
            serveIndex(exchange);
        }
        else {
            String relPath = requestURI.substring(10);
            File fileName = new File(this.downloadDir + relPath);
            if (fileName.exists()) {
                Debug.log(fileName.toString() + " exists, serving...");
                serveFile(exchange, fileName);
            }
            else {
                Debug.log(fileName.toString() + " does not exist, failing...");
                byte [] resp = ("404 Not Found: " + requestURI).getBytes();
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(StatusCode.NOT_FOUND, resp.length);
                OutputStream out = exchange.getResponseBody();
                out.write(resp);
                out.flush();
                out.close();
            }
        }
    }
}
