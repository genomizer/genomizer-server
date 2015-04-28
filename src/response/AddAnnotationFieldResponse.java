package response;

/**
 * Response to a command that adds annotation fields.
 * Should return code 201 (created) if everything is working.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddAnnotationFieldResponse extends Response {


	/**
	 * Constructor for the response
	 * @param code The return code of the response
	 */
	public AddAnnotationFieldResponse(int code) {

		this.code = code;

	}
}
