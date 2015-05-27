package response;

import com.google.gson.*;

import java.util.List;

/**
 * Created by dv13thg on 5/27/15.
 */
public class UserListResponse extends Response {

    private JsonArray usernameArray;
    JsonObject usernameListJson;
    public UserListResponse(List<String> usernameList){
        this.code = HttpStatusCode.OK;
        usernameArray = new JsonArray();
        JsonObject usernameListJson;
        usernameListJson = new JsonObject();
        for (String username: usernameList) {

            usernameListJson.addProperty("username", username);
        }

//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.disableHtmlEscaping().create();
//        JsonElement annotationJson = gson.toJsonTree(username);
//        usernameArray.add(usernameListJson);
    }

    /**
     * Creates a Json representation of the body
     * @return The response body as a String
     */
    @Override
    public String getBody() {

        return usernameListJson.toString();
    }
}
