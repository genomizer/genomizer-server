package server;

import java.net.URI;
import java.util.HashMap;

public class Util {

    // Split an URI into a (path, parameterMap) pair.
    // E.g. "some/path?foo=bar&baz=quux" produces
    // ("some/path", {"foo" : "bar", "baz" : "quux"}).
    public static String parseURI(URI uri, HashMap<String, String> outParams) {
        String reqPath  = uri.getPath();
        String reqQuery = uri.getQuery();
        Debug.log("Request URI path: " + reqPath);
        Debug.log("Request URI query: " + reqQuery);

        for (String paramPair : reqQuery.split("&")) {
            String [] splitPair = paramPair.split("=");
            outParams.put(splitPair[0], splitPair[1]);
        }

        return reqPath;
    }
}
