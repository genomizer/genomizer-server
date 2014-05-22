package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import response.Response;

import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;

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
			List<String> values = db.getChoices(name);
			if(values.contains(value)) {

				db.removeDropDownAnnotationValue(name, value);

			} else {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The value " + value + " does not exist in " + name + " and can not be deleted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.NO_CONTENT, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
		} finally {
			db.close();
		}
		return new MinimalResponse(StatusCode.OK);
	}

}
