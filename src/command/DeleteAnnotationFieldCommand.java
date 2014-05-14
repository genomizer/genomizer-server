package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;

/**
 * Class used to represent a logout command.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class DeleteAnnotationFieldCommand extends Command {

	@Expose
	private ArrayList<DeleteAnnotationInfo> deleteId = new ArrayList<DeleteAnnotationInfo>();

	/**
	 * Used to validate the logout command.
	 * Checks if all annotations which are to
	 * be deleted are present in the database.
	 */
	@Override
	public boolean validate() {
		if(deleteId == null) {
			return false;
		}
		for(DeleteAnnotationInfo da: deleteId) {
			if(da.getName() == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Used to execute the logout command.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;

		try {
			db = initDB();
			for(DeleteAnnotationInfo da: deleteId) {
				db.deleteAnnotation(da.getName());
			}
			db.close();
			return new MinimalResponse(200);

		} catch (SQLException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		}
	}

}
