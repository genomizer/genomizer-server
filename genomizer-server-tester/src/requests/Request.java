package requests;
import com.google.gson.Gson;

/**
 * Super class for the requests.
 * All new requests should extend this.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class Request {
    //TODO Inte serialized, beh�ver inte transient CF
    public transient String requestName;
    public transient String url;
    public transient String requestType;

    public Request(String requestName, String url, String type) {
        this.requestName = requestName;
        this.url = url;
        this.requestType = type;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getRequestName() {
        return requestName;
    }

}
