package response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class GetAnnotationInformationResponse extends Response {

	@Expose
	ArrayList<AnnotationInformation> annotations;

	public GetAnnotationInformationResponse(int code,
			ArrayList<AnnotationInformation> annotations) {

		this.code = code;
		this.annotations = annotations;
	}

}
