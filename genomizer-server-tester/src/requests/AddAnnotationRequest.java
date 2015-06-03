package requests;

/**
 * Request for adding new annotations.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AddAnnotationRequest extends Request {

    public String name;
    public String[] type;
    public String defaultType;
    public Boolean forced;

    public AddAnnotationRequest(String name, String[] categories, Boolean forced) {
        super("addAnnotation", "/annotation/field", "POST");
        this.name = name;
        this.type = categories;
        this.defaultType = "Unknown";
        this.forced = forced;
    }

    @Override
    public String toJson() {
        String json = super.toJson();
        json = json.replaceFirst("defaultType", "default");
        return json;
    }

}
