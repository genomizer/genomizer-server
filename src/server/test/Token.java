package server.test;

import com.google.gson.annotations.Expose;

public class Token {

	@Expose static String token;

	 public String getToken() {
		return token;
	}
}
