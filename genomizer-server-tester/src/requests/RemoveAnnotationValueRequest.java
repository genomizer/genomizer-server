package requests;

/**
 * Request for removing annotation values.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class RemoveAnnotationValueRequest extends Request {
    
    public RemoveAnnotationValueRequest(String annotationName, String valueName) {
        super("removeAnnotationValue", "/annotation/value/" + annotationName
                + "/" + valueName, "DELETE");
    }
}
