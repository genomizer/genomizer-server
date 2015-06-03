package requests;

/**
 * Request for editing the annotations.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class EditAnnotationRequest extends Request {

    public String name;
    public String annotation;

    public EditAnnotationRequest(String name,
            String annotation) {
        super("changeannotation", "/file/changeAnnotation", "POST");
        this.name = name;
        this.annotation = annotation;
    }
}
