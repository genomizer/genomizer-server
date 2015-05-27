package util;

import database.FileValidator;

import java.io.IOException;
import java.net.URI;
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
                if (splitPair.length==1) {
                    outParams.put(splitPair[0], "");
                } else {
                    outParams.put(splitPair[0], splitPair[1]);
                }

            }
        }

        return reqPath;
    }

    // Replace a given file's extension.
    // Example: replaceExtension("foo/bar.baz", ".quux") -> "foo/bar.quux",
    public static String replaceExtension(String file, String newExt) {
        return file.replaceAll("\\.[^\\.]+$", newExt);
    }

    // Format a millisecond-resolution time difference as a string of form
    // "A day(s), B hour(s), C minute(s), D second(s), E ms".
    public static String formatTimeDifference(long diffMillis) {
        long seconds = diffMillis / 1000; diffMillis = diffMillis % 1000;
        long minutes = seconds / 60; seconds = seconds % 60;
        long hours   = minutes / 60; minutes = minutes % 60;
        long days    = hours   / 24; hours = hours % 24;

        StringBuilder ret = new StringBuilder();
        if (days > 0)
            ret.append(days + " " + pluralize(days, "day") + ", ");
        if (hours > 0)
            ret.append(hours + " " + pluralize(hours, "hour") + ", ");
        if (minutes > 0)
            ret.append(minutes + " " + pluralize(minutes, "minute") + ", ");
        if (seconds > 0)
            ret.append(seconds + " " + pluralize(seconds, "second") + ", ");
        ret.append(diffMillis + " ms");

        return ret.toString();
    }

    private static String pluralize(long num, String word) {
        return (num > 1 ? word + "s" : word);
    }
}
