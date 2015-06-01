package response;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Response containing information about a user.
 *
 * @author Business Logic 2015
 * @version 1.1
 */
public class UserInfoResponse extends Response {

    private JsonObject userInfoJson;

    /**
     * converts a list of username to a json object
     *
     * @param userInfo userinfo to be converted to json
     *       username
     *       UserRights (privileges)
     *       Full Name
     *       E-mail
     */
    public UserInfoResponse(List<String> userInfo){
        this.code = HttpStatusCode.OK;
        userInfoJson = new JsonObject();
        userInfoJson.addProperty("username", userInfo.get(0));
        userInfoJson.addProperty("privileges",userInfo.get(1));
        userInfoJson.addProperty("name",userInfo.get(2));
        userInfoJson.addProperty("email", userInfo.get(3));
    }

    /**
     * Creates a Json representation of the body
     * @return The response body as a String
     */
    @Override
    public String getBody() {

        return userInfoJson.toString();
    }
}