package command;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class DownloadResponse extends Response {

	@Expose
	private String experimentID;

	@Expose
	private String fileName;

	@Expose
	private String size;

	@Expose
	private String type;

	@Expose
	private String URL;

	public DownloadResponse(int code, ArrayList<String> attributes) {

		this.code = code;

		switch (code) {
		case 200:
			experimentID = attributes.get(0);
			fileName = attributes.get(1);
			size = attributes.get(2);
			type = attributes.get(3);
			URL = attributes.get(4);
			break;
		case 404:


		}


	}

}
