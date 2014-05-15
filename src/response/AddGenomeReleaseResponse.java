package response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class that represents an actual response.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseResponse extends Response {

	@SerializedName("filepath")
	@Expose
	private String filePath = null;

	/**
	 * Constructor used to initiate the command.
	 *
	 * @param code to send as a responsecode.
	 */
	public AddGenomeReleaseResponse(int code, String filePath) {

		this.code = code;
		this.filePath = filePath;

	}

}