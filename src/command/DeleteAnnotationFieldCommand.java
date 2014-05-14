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
 * @author tfy09jnn, Hugo K�llstr�m
 * @version 1.1
 */
public class DeleteAnnotationFieldCommand extends Command {

	@Expose
	private ArrayList<DeleteAnnotationInfo> deleteAnnos = new ArrayList<DeleteAnnotationInfo>();

	/**
	 * Used to validate the delete annotation command.
	 */
	@Override
	public boolean validate() {
		if(deleteAnnos == null) {
			return false;
		}
		for(DeleteAnnotationInfo da: deleteAnnos) {
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
		try {
			DatabaseAccessor db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			Map<String, Integer> currAnno = db.getAnnotations();
			for(DeleteAnnotationInfo da: deleteAnnos) {
				if(currAnno.containsKey(da.getName())) {
					return new MinimalResponse(StatusCode.FILE_NOT_FOUND);
				}
				db.deleteAnnotation(da.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new MinimalResponse(StatusCode.OK);
	}
}
