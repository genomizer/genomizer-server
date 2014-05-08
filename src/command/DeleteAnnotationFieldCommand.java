package command;

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
			if(da.getId() == null) {
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
		int result = 0;

		try {
			DatabaseAccessor db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			Map<String, Integer> currAnno = db.getAnnotations();
			//TODO: Add the label. API looks wierd currently.
			for(DeleteAnnotationInfo da: deleteId) {
				if(currAnno.containsKey(da.getId())) {
					return new MinimalResponse(400);
				}
				result = db.deleteAnnotation(da.getId());
			}
			if(result == 0) {
				return new MinimalResponse(403);
			} else {
				return new MinimalResponse(200);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}
}
