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
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteAnnotationFieldCommand extends Command {

	@Expose
	private ArrayList<DeleteAnnotationInfo> deleteId = new ArrayList<DeleteAnnotationInfo>();

	/**
	 * Used to validate the logout command.
	 */
	@Override
	public boolean validate() {
		if(deleteId == null) {
			return false;
		}

		try {
			DatabaseAccessor db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			Map<String, Integer> currAnno = db.getAnnotations();
			for(DeleteAnnotationInfo da: deleteId) {
				if(da.getId() == null) {
					return false;
				}
				if(currAnno.containsKey(da.getId())) {
					return false;
				}
			}
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	/**
	 * Used to execute the logout command.
	 */
	@Override
	public Response execute() {
		Response rsp;
		int result = 0;

		try {
			DatabaseAccessor dbAccess = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);

			//TODO: Add the label. API looks wierd currently.
			for(DeleteAnnotationInfo delAnno: deleteId) {
				result = dbAccess.deleteAnnotation(delAnno.getId());
			}
			if(result == 0) {
				rsp = new MinimalResponse(403);
			} else {
				rsp = new MinimalResponse(200);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}
}
