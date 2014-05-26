package command;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

public class RenameAnnotationValueCommand extends Command {

	@Expose
	String name;

	@Expose
	String oldValue;

	@Expose
	String newValue;

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;

		try {
			db = initDB();
			ArrayList<String> annotations = db.getAllAnnotationLabels();
			if(annotations.contains(name)) {
				List<String> values = db.getChoices(name);
				if(values.contains(oldValue)) {
					db.changeAnnotationValue(name, oldValue, newValue);
					return new MinimalResponse(StatusCode.OK);
				} else {
					return new ErrorResponse(StatusCode.BAD_REQUEST, "The value" + oldValue + " does not exist");
				}
			} else {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation " + name + " does not");
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			db.close();
		}
	}

}
