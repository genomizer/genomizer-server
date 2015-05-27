package response;

import com.google.gson.annotations.Expose;

/**
 * Default response for database errors.
 *
 * @author Business logic 2015
 * @version 1.0
 */
public class DatabaseErrorResponse extends Response{
    @Expose
    private String message;

    /**
     * Creator for the error response
     * @param action what the user attempted to do.
     *              (eg. Login attempt unsuccessful due..)
     */
    public DatabaseErrorResponse(String action) {
        this.code = HttpStatusCode.INTERNAL_SERVER_ERROR;
        this.message = action + " unsuccessful due to temporary database problems.";
    }

    /**
     * Getter for the error message
     * @return The error message as a String
     */
    public String getMessage() {
        return message;
    }
}
