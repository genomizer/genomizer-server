package command;

import java.awt.List;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;

import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

public class EditAnnotationFieldCommand extends Command {

	@Expose
	private String oldName;
	@Expose
	private String newName;

	public EditAnnotationFieldCommand() {

	}

	@Override
	public boolean validate() {
		if (oldName != null && oldName.length() > 0 && newName != null && newName.length() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			Map<String,Integer> anno = db.getAnnotations();

			if (!anno.containsKey(oldName)) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation field " + oldName + " does not exist in the database");
			} else if (anno.containsKey(newName)) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation field " + newName + " already exists in the database");
			}
			db.changeAnnotationLabel(oldName, newName);
		} catch (IOException | SQLException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
			}
		}

		return new MinimalResponse(StatusCode.OK);
	}

}
