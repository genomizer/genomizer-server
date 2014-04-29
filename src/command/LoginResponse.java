package command;

import com.google.gson.annotations.Expose;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.Enum;

public class LoginResponse extends Response {

	@Expose
	private String token;

	public LoginResponse(int code, String token) {
		this.code = code;
		this.token = token;
	}

	public String toString() {

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

		return "HTTP/1.1 " + code + " OK\n" + gson.toJson(this);
	}

}
