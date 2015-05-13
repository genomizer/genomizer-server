package command.experiment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import command.Annotation;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import server.Debug;

/**
 * Class used to add an experiment represented as a command.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostExperimentCommand extends Command {
	@Expose
	private String name = null;

	@Expose
	private ArrayList<Annotation> annotations = new ArrayList<>();


	@Override
	public void validate() throws ValidateException {

		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.EXPID, "Experiment name");

		if(annotations == null || annotations.size() < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify " +
					"annotations for the experiment.");
		}

		for(int i =0;i<annotations.size();i++){
			if(annotations.get(i) == null){
				throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Found " +
						"an empty annotation or annotation value, please " +
						"specify annotations.");
			}
			validateName(annotations.get(i).getName(),
					MaxLength.ANNOTATION_LABEL, "Annotation label");
			validateName(annotations.get(i).getValue(),
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
			return new MinimalResponse(HttpStatusCode.CREATED);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			Debug.log("Adding of experiment " + name + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Adding of experiment "+ name+
					" didn't work due to temporary problems with the database.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

}
