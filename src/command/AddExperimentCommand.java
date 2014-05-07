package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.ws.Response;

import response.MinimalResponse;
import response.StatusCode;
import server.DatabaseSettings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import database.DatabaseAccessor;

/**
 * Class used to add an experiment represented as a command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddExperimentCommand extends Command {

	@Expose
	private String name;

	@SerializedName("created by")
	@Expose
	private String created_by;

	@Expose
	private ArrayList<Annotation> annotations = new ArrayList<Annotation>();

	/**
	 * Empty constructor.
	 */
	public AddExperimentCommand() {

	}

	@Override
	public boolean validate() {
		if(name == null || created_by == null || annotations == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public response.Response execute() {
		try {
			DatabaseAccessor db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			db.addExperiment(name);

			for(Annotation annotation: annotations) {
				db.annotateExperiment(name, annotation.getName(), annotation.getValue());
			}
			return new MinimalResponse(StatusCode.CREATED);

		} catch (SQLException e) {
			e.printStackTrace();
			return 	new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} catch (IOException e) {
			e.printStackTrace();
			return 	new MinimalResponse(StatusCode.BAD_REQUEST);
		}
	}






}
