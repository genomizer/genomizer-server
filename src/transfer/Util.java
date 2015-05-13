package transfer;

import database.FileValidator;
import server.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class Util {

    // Check that a relative path does not contain forbidden components ('..' or '.').
    public static void validatePath (String path) throws IOException {
        if (path.startsWith("/") || path.endsWith("/")) {
            throw new IOException("The path should be relative!");
        }
        if (path.matches(".*/\\.\\./.*")) {
            throw new IOException("The '..' path component is not allowed!");
        }
        if (path.matches(".*/\\./.*")) {
            throw new IOException("The '.' path component is not allowed!");
        }
        if (path.matches(".*//+.*")) {
            throw new IOException("Multiple slashes are not allowed!");
        }
        for (String component : path.split("/")) {
            if (!component.matches("^" + FileValidator.validFileNameChars + "+$")) {
                throw new IOException("Invalid path component: " + component + "!");
            }
        }
    }

    // Split an URI into a (path, parameterMap) pair.
    // E.g. "some/path?foo=bar&baz=quux" produces
    // ("some/path", {"foo" : "bar", "baz" : "quux"}).
    public static String parseURI(URI uri, HashMap<String, String> outParams) {
        String reqPath  = uri.getPath();
        String reqQuery = uri.getQuery();

        if (reqQuery != null) {
            for (String paramPair : reqQuery.split("&")) {
                String[] splitPair = paramPair.split("=");
                outParams.put(splitPair[0], splitPair[1]);
            }
        }

        return reqPath;
    }

}
