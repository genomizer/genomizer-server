package response;

import com.google.gson.annotations.Expose;

/**
 * Class that represents an actual response.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseResponse extends Response {

	@Expose
	private String URLupload = null;

	/**
	 * Constructor used to initiate the command.
	 *
	 * @param code to send as a responsecode.
	 */
	public AddGenomeReleaseResponse(int code, String URLupload) {

		this.code = code;
		this.URLupload = URLupload;

	}

}