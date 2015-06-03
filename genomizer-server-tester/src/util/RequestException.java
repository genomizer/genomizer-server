package util;

/**
 * Exception which is sent when a request doesn't return the OK status code.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class RequestException extends Exception {

    private static final long serialVersionUID = -5331379710023333449L;
    private int responseCode;
    private String responseBody;

    /**
     * Creates the exception with the response information.
     * @param responseCode
     * @param responseBody
     */
    public RequestException(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    /**
     * Getter for the response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Getter for the response body
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * ToString method which formats the error messages nicely.
     * @return The formatted message.
     */
    @Override
    public String getMessage() {
        return "Code: " + responseCode + "\nBody: " + responseBody;
    }
}
