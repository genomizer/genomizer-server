package command.experiment;

import com.google.gson.annotations.Expose;
import command.Annotation;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Command used to update an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PutExperimentCommand extends Command {
	@Expose
	private String name = null;
	@Expose
	private ArrayList<Annotation> annotations = null;
	private String expID;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void setFields(String uri, HashMap<String, String> query,
                          String username, UserType userType) {
		super.setFields(uri, query, username, userType);
		expID = uri.split("/")[2];
	}

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
				throw new ValidateException(HttpStatusCode.BAD_REQUEST,
						"Found an empty annotation or annotation value, " +
								"please specify annotations.");
			}

			validateName(annotations.get(i).getName(),
					MaxLength.ANNOTATION_LABEL, "Annotation label");
			validateName(annotations.get(i).getValue(),
					MaxLength.ANNOTATION_VALUE, "Annotation value");
		}
	}

	@Override
	public Response execute() {
		Response response;

		try (DatabaseAccessor db = initDB()) {
			for (Annotation annotation : annotations) {
				db.updateExperiment(name, annotation.getName(),
						annotation.getValue());
			}

			response = new MinimalResponse(HttpStatusCode.OK);
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Editing of experiment '" + expID + "' unsuccessful due " +
							"to temporary database problems");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Editing of experiment '" + expID + "' unsuccessful. " +
							e.getMessage());
		}

		return response;
	}
}
