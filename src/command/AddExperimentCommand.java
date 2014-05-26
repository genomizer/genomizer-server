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
	private ArrayList<Annotation> annotations = new ArrayList<Annotation>();

	/**
	 * Empty constructor.
	 */
	public AddExperimentCommand() {

	}

	@Override
	public boolean validate() {
		if(name == null || annotations == null || name.indexOf('/') != -1) {
			return false;
		} else {
			for(int i =0;i<annotations.size();i++){
				if(annotations.get(i).getName()==null || annotations.get(i).getValue()==null){
					return false;
				}
			}
			return true;
		}
		/*if(name.indexOf('/') != -1) {
			return false;
		}*/

	}

	@Override
	public response.Response execute() {

		DatabaseAccessor db = null;

		try {
			db = initDB();
			db.addExperiment(name);
			for(Annotation annotation: annotations) {
				db.annotateExperiment(name, annotation.getName(), annotation.getValue());
			}
			db.close();
			return new MinimalResponse(StatusCode.CREATED);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally{
				db.close();
		}
	}
}
