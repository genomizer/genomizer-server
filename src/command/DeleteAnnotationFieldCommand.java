package command;

import java.sql.SQLException;
import java.util.ArrayList;

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


	/*
{
 "deleteId": [
              { "id": 1, "values": ["man", "mouse"] },
              { "id": 2, "values": [ ] },
              { "id": 3, "values": ["leg"] }
             ]
}
*/

	@Expose
	private ArrayList<String> deleteId = new ArrayList<String>();

	/**
	 * Used to validate the logout command.
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return false;

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
			//result = dbAccess.deleteAnnotation(label);

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
