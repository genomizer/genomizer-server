package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import response.ErrorResponse;
import response.MinimalResponse;
import response.StatusCode;
import com.google.gson.annotations.Expose;
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
			db = initDB();
			db.addExperiment(name);
			for(Annotation annotation: annotations) {
				System.out.println("annotation name: " +annotation.getName() + " annotation value: " + annotation.getValue());
				db.annotateExperiment(name, annotation.getName(), annotation.getValue());
				System.out.println("added annotation" + annotation.getName());
			}
			db.close();
			return new MinimalResponse(StatusCode.CREATED);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		} finally{
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		}
	}






}
