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

	@Expose
	private String createdBy;

	@Expose
	private ArrayList<Annotation> annotations = new ArrayList<Annotation>();

	/**
	 * Empty constructor.
	 */
	public AddExperimentCommand() {

	}

	@Override
	public boolean validate() {
		if(name == null || createdBy == null || annotations == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public response.Response execute() {

		DatabaseAccessor db = null;

		try {
			System.out.println("execute add exp");
			db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			System.out.println("NAME: " + name);
			db.addExperiment(name);
			System.out.println("added experiment name...");
			for(Annotation annotation: annotations) {
				System.out.println("annotation name: " +annotation.getName() + " annotation value: " + annotation.getValue());
				db.annotateExperiment(name, annotation.getName(), annotation.getValue());
				System.out.println("added annotation" + annotation.getName());
			}
			return new MinimalResponse(StatusCode.CREATED);

		} catch (SQLException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		} finally{
			try {
				db.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		}
	}






}
