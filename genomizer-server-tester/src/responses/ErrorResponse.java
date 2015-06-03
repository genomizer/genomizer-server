package responses;

/**
 * Response for the error response.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ErrorResponse extends Response {
    
    public String message;
    
    public ErrorResponse() {
        super("error");
    }
    
}
