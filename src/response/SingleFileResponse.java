package response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import database.containers.FileTuple;

/**
 * A single file response
 *
 * @author dv13thg
 */
public class SingleFileResponse extends Response {

    private JsonObject jsonObj = new JsonObject();

    public SingleFileResponse(int code, FileTuple fileTuple){

        this.code = code;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        FileInformation fileInfo = new FileInformation(fileTuple);
        jsonObj.add("file", gson.toJsonTree(fileInfo));
    }

    /**
     * Creates a Json representation of the body
     * @return The response body as a String
     */
    @Override
    public String getBody() {

        return jsonObj.toString();
    }
}
