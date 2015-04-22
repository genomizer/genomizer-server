package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import response.StatusCode;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class DownloadHandler {
    private String downloadDir;

    public DownloadHandler (String downloadDir) {
        this.downloadDir = downloadDir;
    }

    private void serveIndex(HttpExchange exchange) throws IOException {
        final ArrayList<String> fileList = new ArrayList<>();
        Files.walkFileTree((new File(downloadDir)).toPath(), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                fileList.add(file.toString());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        String fileListHTML = "";
        int parentLen = downloadDir.length();
        for (String file : fileList) {
            String f = file.substring(parentLen);
            fileListHTML += "<li>" + "<a href=\"/download/" + f
                    + "\">" + f + "</a>" + "</li>";
        }
        byte [] index = ("<html><body><ul>" +
                fileListHTML +
                "</ul></body></html>").getBytes();
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
        if (requestURI.equals("/download") || requestURI.equals("/download/")) {
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
