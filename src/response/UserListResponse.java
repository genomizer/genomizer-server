package response;

import com.google.gson.*;

import java.util.List;

/**
 * Response containing username info of all users.
 *
 * @author Business Logic 2015
 * @version 1.0
 */
public class UserListResponse extends Response {

    private JsonObject JsonObject;

    /**
     * converts a list of username to a json object
     *
     * @param usernameList the list to be converted to json
     */
    public UserListResponse(List<String> usernameList){
        this.code = HttpStatusCode.OK;
        JsonObject = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject.add("username", gson.toJsonTree(usernameList));
    }

    /**
     * Creates a Json representation of the body
     * @return The response body as a String
     */
    @Override
    public String getBody() {

        return JsonObject.toString();
    }
}
