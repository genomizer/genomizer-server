package requests;

/**
 * Request for renaming annotations.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class RenameAnnotationFieldRequest extends Request {

    public String newName;
    public String oldName;

    public RenameAnnotationFieldRequest(String oldname, String newname) {
        super("renameAnnotationRequest", "/annotation/field", "PUT");
        this.oldName = oldname;
        this.newName = newname;
    }
    
}
