package response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.containers.FileTuple;

/**
 * A single file response
 *
 * @author dv13thg
 */
public class SingleFileResponse extends Response {

    private String json;

    public SingleFileResponse(FileTuple fileTuple){
        this.code = HttpStatusCode.OK;

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
        FileInformation fileInfo = new FileInformation(fileTuple);
        json = gson.toJson(fileInfo);
    }

    /**
     * Creates a Json representation of the body
     * @return The response body as a String
     */
    @Override
    public String getBody() {
        return json;
    }
}
