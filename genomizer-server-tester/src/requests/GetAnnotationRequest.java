package requests;

/**
 * Request for getting the annotations.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetAnnotationRequest extends Request {

    public GetAnnotationRequest() {
        super("getAnnotation", "/annotation", "GET");
    }
}
