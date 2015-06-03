package responses;

/**
 * Super class for the responses. All new responses should extend this class.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class Response {
  //TODO Inte serialized, behöver inte transient CF
    public transient String responseName;

    public Response(String responseName) {
        this.responseName = responseName;
    }

}
