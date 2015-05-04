package command;

import authentication.Authenticate;
import authentication.BCrypt;
import authentication.LoginAttempt;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Command for user to update his/her information
 * Created by dv13jen on 2015-05-04.
 */
public class UpdateUserCommand extends Command {

    private String username = null;

    @Expose
    private String oldPassword = null;

    @Expose
    private String newPassword = null;

    @Expose
    private String name = null;

    @Expose
    private String email = null;

    /**
     * Set username using uuid along with the usertype
     * @param uri the URI from the http request.
     * @param uuid
     * @param userType
     */
    @Override
    public void setFields(String uri, String uuid, UserMethods.UserType userType) {
        username = Authenticate.getUsernameByID(uuid);
        this.userType = userType;
    }

    /**
     * Used to make sure the strings of the command are correct
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {

        validateName(oldPassword, MaxLength.PASSWORD, "oldPassword");
        validateName(newPassword, MaxLength.PASSWORD, "newPassword");
        validateExists(name, MaxLength.FULLNAME, "Name");
        validateExists(email, MaxLength.EMAIL, "Email");
    }

    @Override
    public Response execute() {
        DatabaseAccessor db;
        String dbHash;
        try {
            db = initDB();
            dbHash = db.getPasswordHash(username);
        } catch (SQLException | IOException e) {
        Debug.log("UPDATE WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
                e.getMessage());
        return new ErrorResponse(StatusCode.BAD_REQUEST,
                "UPDATE WAS UNSUCCESSFUL FOR: " + username + ". REASON: " + e.getMessage());
         }

        if(dbHash == null || dbHash.isEmpty()){
            return new ErrorResponse(StatusCode.UNAUTHORIZED, "Incorrect user name for user "
                    + username);
        }

        LoginAttempt login = Authenticate.login(username, oldPassword, dbHash);


        //TODO Enable below code when db method is implemented.
        return new MinimalResponse(StatusCode.NO_CONTENT);
        /*
        if(login.wasSuccessful()) {


            try {

            String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            db.updateUser(username, hash, name, email);
            } catch (SQLException | IOException e) {
                Debug.log("UPDATE WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
                        e.getMessage());
                return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
                        "editing user "+username+" in database, user probably don't exist. " +
                        e.getMessage());
            }
            return new MinimalResponse(StatusCode.OK);
        }


        Debug.log("UPDATE WAS UNSUCCESSFUL FOR: " + username + ". REASON: INCORRECT OLD PASSWORD");
        return new ErrorResponse(StatusCode.UNAUTHORIZED, "Error with update of user " +username+
                ". Incorrect old password"+ login.getErrorMessage());

                */
    }

}
