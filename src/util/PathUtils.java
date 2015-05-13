package util;

import server.ServerSettings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PathUtils {

    // Join two file paths, correctly handling the case when
    // one or both start or end with a path separator.
    //
    //
    public static String join(String path1, String path2) {
        StringBuilder builder = new StringBuilder();
        builder.append(path1);

        if (path1.endsWith(File.pathSeparator)) {
            if (path2.startsWith(File.pathSeparator)) {
                builder.append(path2.substring(1));
            }
            else {
                builder.append(path2);
            }
        }
        else {
            if (path2.startsWith(File.pathSeparator)) {
                builder.append(path2);
            }
            else {
                builder.append(File.pathSeparator);
                builder.append(path2);
            }
        }

        return builder.toString();
    }

    // The DB can contain both absolute and relative paths ATM,
    // which are both mapped to relative paths since we don't
    // want to allow arbitrary absolute paths.
    // The following mapping schema is used:
    //
    // '/var/www/data/path/to/file'
    //           -> '/path-map/1/path/to/file'
    // 'resources/path/to/file'
    //           -> '/path-map/2/path/to/file'
    // '$ServerSettings.fileLocation/path/to/file'
    //           -> '/path/to/file'
    private static final HashMap<String, String> localToPublicMap;
    private static final HashMap<String, String> publicToLocalMap;

    private static final String local1  = "/var/www/data/";
    private static final String public1 = "/path-map/1/";
    private static final String local2  = "resources/";
    private static final String public2 = "/path-map/2/";
    private static final String local3  = resourcesAbsPath();
    private static final String public3 = "/path-map/3/";

    private static final String resourcesAbsPath() {
        try {
            return new File("resources/").getCanonicalPath();
        }
        catch (IOException e) {
            return "resources/";
        }
    }

    static {
        localToPublicMap = new HashMap<>();
        publicToLocalMap = new HashMap<>();

        localToPublicMap.put(local1, public1);
        localToPublicMap.put(local2, public2);
        localToPublicMap.put(local3, public3);

        publicToLocalMap.put(public1, local1);
        publicToLocalMap.put(public2, local2);
        publicToLocalMap.put(public3, local3);
    }

    // Map a local path to a public path.
    public static String pathMapLocalToPublic(String path) throws IllegalArgumentException {
        if (path.startsWith(ServerSettings.fileLocation)) {
            return path.substring(ServerSettings.fileLocation.length()-1); // must start with a slash.
        }

        for (String key : localToPublicMap.keySet()) {
            if (path.startsWith(key))
                return PathUtils.join(key, path.substring(localToPublicMap.get(key).length()));
        }

        if (!path.startsWith("/")) {
            return path;
        }
        else {
            throw new IllegalArgumentException("Invalid local path: " + path);
        }
    }

    // Map a public path to a local path.
    public static String pathMapPublicToLocal(String path) throws IllegalArgumentException {
        for (String key : publicToLocalMap.keySet()) {
            if (path.startsWith(key))
                return PathUtils.join(publicToLocalMap.get(key), path.substring(key.length()));
        }

        return PathUtils.join(ServerSettings.fileLocation, path);
    }
}
