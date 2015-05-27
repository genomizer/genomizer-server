package util;

import java.io.File;

public class PathUtils {

    // Join two file paths, correctly handling the case when
    // one or both start or end with a path separator.
    //
    public static String join(String path1, String path2) {
        StringBuilder builder = new StringBuilder();
        builder.append(path1);

        if (path1.endsWith(File.separator)) {
            if (path2.startsWith(File.separator)) {
                builder.append(path2.substring(1));
            }
            else {
                builder.append(path2);
            }
        }
        else {
            if (path2.startsWith(File.separator)) {
                builder.append(path2);
            }
            else {
                builder.append(File.separator);
                builder.append(path2);
            }
        }

        return builder.toString();
    }
}
