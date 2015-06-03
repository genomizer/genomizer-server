package requests;

/**
 * Request for adding new annotation values.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AddNewAnnotationValueRequest extends Request {
    
    public String name;
    public String value;

    public AddNewAnnotationValueRequest(String annotationName, String valueName) {
        super("addAnnotationValue", "/annotation/value", "POST");
        this.name = annotationName;
        this.value = valueName;
    }
    
}
