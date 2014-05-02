package response;

/**
 * Response to a command that adds annotation fields.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddAnnotationFieldResponse extends Response {

	/* Reponse that should return code 201 (created)
	 * if everything is working.
	 */
	/**
	 * Empty constructor.
	 */
	public AddAnnotationFieldResponse(int code) {

		this.code = code;

	}


}