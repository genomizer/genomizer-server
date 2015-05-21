package transfer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import response.HttpStatusCode;
import server.Debug;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;

// Implements file downloading.
public class DownloadHandler {
    private String handlerRoot;
    private String downloadDir;

    public DownloadHandler (String handlerRoot, String downloadDir) {
        this.handlerRoot = handlerRoot;
        this.downloadDir = downloadDir;
    }

    // Serve an index page listing all files in the downloadDir.
    // Default action when the users requests '/download'.
    private void serveIndex(HttpExchange exchange) throws IOException {
        Debug.log("Serving file index...");

        // List the directory contents. Quite verbose, but...
        final ArrayList<String> fileList = new ArrayList<>();
        Files.walkFileTree((new File(downloadDir)).toPath(), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // ...this is the only interesting line of code.
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

        // Render the HTML page.
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
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, index.length);
        OutputStream out = exchange.getResponseBody();
        out.write(index);
        out.close();
    }

    // Serve a file given an absolute path on the filesystem.
    private void serveFile(HttpExchange exchange, File file) throws IOException {
        OutputStream out = exchange.getResponseBody();

        if (file.exists()) {
            Debug.log(file.toString() + " exists, serving...");
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Description", "File Transfer");
            headers.set("Content-Type", "application/octet-stream");
            headers.set("Content-Disposition", "attachment; filename="
                    + file.getName());
            headers.set("Expires", "0");
            headers.set("Cache-Control", "must-revalidate");
            headers.set("Pragma", "public");
            exchange.sendResponseHeaders(200, file.length());

            byte[] buf = new byte[64*1024];
            InputStream in = new FileInputStream(file);
            int c = 0;
            while ((c = in.read(buf, 0, buf.length)) > 0) {
                out.write(buf, 0, c);
            }

            in.close();
        }
        else {
            Debug.log(file.toString() + " does not exist, failing...");
            byte [] resp = ("404 Not Found").getBytes();
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND, resp.length);
            out.write(resp);
        }

        out.close();
    }

    // Serve a directory index or a file, depending on the context.
    // GET request entry point.
    public void handleGET(HttpExchange exchange) throws IOException {
        HashMap<String, String> reqParams = new HashMap<>();
        String reqPath = Util.parseURI(exchange.getRequestURI(), reqParams);

        if (reqPath.equals(this.handlerRoot)
                || reqPath.equals(this.handlerRoot + "/")) {
            if (reqParams.containsKey("path")) {
                Debug.log("Using legacy download method ('?path=/absolute/path').");
                serveFile(exchange, new File(reqParams.get("path")));
            }
            else {
                serveIndex(exchange);
            }
        }
        else {
            String relPath = reqPath.substring(this.handlerRoot.length() + 1);
            serveFile(exchange, new File(this.downloadDir + relPath));
        }
    }
}
