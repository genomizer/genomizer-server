package requests;

/**
 * Request for renaming annotations.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class RenameAnnotationValueRequest extends Request {

    private String name;
    private String oldValue;
    private String newValue;

    public RenameAnnotationValueRequest(String annotationName, String oldValue,
            String newValue) {
        super("renameAnnotationValueRequest", "/annotation/value", "PUT");
        this.name = annotationName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

}
