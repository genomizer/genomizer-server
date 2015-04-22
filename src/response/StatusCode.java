package response;

/**
 * Contains all HTTP response codes that are used by the server.
 * Currently, 403 and 429 are not used.
 *
 * @author ens10olm
 * @version 1.0
 */
public class StatusCode {

	public static final int OK = 200;
	public static final int CREATED = 201;
	public static final int NO_CONTENT = 204;
	public static final int BAD_REQUEST = 400;
	public static final int UNAUTHORIZED = 401;
	public static final int FORBIDDEN = 403;
	public static final int FILE_NOT_FOUND = 404;
	public static final int METHOD_NOT_ALLOWED = 405;
	public static final int TOO_MANY_REQUESTS = 429;
	public static final int SERVICE_UNAVAILABLE = 503;

}
