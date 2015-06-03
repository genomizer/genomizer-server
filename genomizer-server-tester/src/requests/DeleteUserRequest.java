package requests;

/**
 * Request for deleting a user.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class DeleteUserRequest extends Request{

    /**
     * Request for deleting the user.
     * @param User User to delete.
     */
    public DeleteUserRequest(String User) {
        super("deleteuser", "/admin/user/" + User, "DELETE");
    }

}
