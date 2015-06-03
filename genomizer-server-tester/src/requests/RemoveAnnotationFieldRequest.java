package requests;

/**
 * Request for removing annotation fields.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class RemoveAnnotationFieldRequest extends Request {
    
    public RemoveAnnotationFieldRequest(String annotationName) {
        super("deleteAnnotation", "/annotation/field/" + annotationName, "DELETE");
    }
}
