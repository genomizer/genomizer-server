package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;

/**
 * Class used to add an experiment represented as a command.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class AddExperimentCommand extends Command {
	@Expose
	private String name = null;

	@Expose
	private ArrayList<Annotation> annotations = new ArrayList<>();

	@Override
	public void validate() throws ValidateException {

		hasRights(UserRights.getRights(this.getClass()));
		validateString(name, MaxLength.EXPID, "Experiment name");

		if(annotations == null || annotations.size() == 0) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify " +
					"annotations for the experiment.");
		}

		for(int i =0;i<annotations.size();i++){
			if(annotations.get(i) == null){
				throw new ValidateException(StatusCode.BAD_REQUEST, "Found " +
						"an empty annotation or annotation value, please " +
						"specify annotations.");
			}
			validateString(annotations.get(i).getName(),
					MaxLength.ANNOTATION_LABEL, "Annotation label");
			validateString(annotations.get(i).getValue(),
					MaxLength.ANNOTATION_VALUE, "Annotation value");
		}
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			db.addExperiment(name);
			for(Annotation annotation: annotations) {
				db.annotateExperiment(name, annotation.getName(),
						annotation.getValue());
			}
			return new MinimalResponse(StatusCode.CREATED);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public void setRights(UserType rights) {
		this.userType = rights;
	}
}
