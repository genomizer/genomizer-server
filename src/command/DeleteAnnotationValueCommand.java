package command;

import java.io.IOException;
import java.sql.SQLException;

import response.Response;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

public class DeleteAnnotationValueCommand extends Command {

	private String name;
	private String value;

	public DeleteAnnotationValueCommand(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public boolean validate() {
		if(name == null || value == null) {
			return false;
		}
		return true;
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;

		try {
			db = initDB();
			db.removeAnnotationValue(name, value);
		} catch (SQLException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.NO_CONTENT);
		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		}
		return new MinimalResponse(StatusCode.OK);
	}

}
