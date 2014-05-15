package command;

import java.io.IOException;
import java.sql.SQLException;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
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
		if(name == null || oldValue == null || newValue == null) {
			return false;
		}
		return true;
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;

		try {
			db = initDB();
			db.changeAnnotationValue(name, oldValue, newValue);
		} catch (SQLException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.NO_CONTENT);
		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} finally{
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new MinimalResponse(StatusCode.NO_CONTENT);
			}
		}
		return new MinimalResponse(StatusCode.OK);
	}

}
