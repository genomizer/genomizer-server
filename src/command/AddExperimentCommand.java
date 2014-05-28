package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import response.ErrorResponse;
import response.MinimalResponse;
import response.StatusCode;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxSize;

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
	public boolean validate() throws ValidateException {
		if(name == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a name for the experiment.");
		}
		if(annotations == null || annotations.size() == 0) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify annotations for the experiment.");
		}
		if(name.length() > MaxSize.EXPID || name.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Experiment name has to be between 1 and "
					+ database.constants.MaxSize.EXPID + " characters long.");
		}
		if(name.indexOf('/') != -1 || !hasOnlyValidCharacters(name)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation name. Valid characters are: " + validCharacters);
		}

		for(int i =0;i<annotations.size();i++){
			if(annotations.get(i) == null){
				throw new ValidateException(StatusCode.BAD_REQUEST, "Found a null annotation or annotation value, please specify annotations.");
			}
			if(annotations.get(i).getName()==null || annotations.get(i).getValue()==null){
				throw new ValidateException(StatusCode.BAD_REQUEST, "Found an empty annotation or annotation value, please specify annotations.");
			}
			if(!hasOnlyValidCharacters(annotations.get(i).getName()) || !hasOnlyValidCharacters(annotations.get(i).getValue())) {
				throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation name or value. Valid characters are: " + validCharacters);
			}
		}
		return true;
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
