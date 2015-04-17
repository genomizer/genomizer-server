package response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class DownloadResponse extends Response {

	@Expose
	public String experimentID;
	@Expose
	public String fileName;
	@Expose
	public String size;
	@Expose
	public String type;
	@Expose
	public String URL;

	public DownloadResponse(int code, ArrayList<String> attributes) {
		this.code = code;
		experimentID = attributes.get(0);
		fileName = attributes.get(1);
		size = attributes.get(2);
		type = attributes.get(3);
		URL = attributes.get(4);
	}

}
