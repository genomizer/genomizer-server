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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class used when updating the experiments.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PutExperimentCommand extends Command {

	@Expose
	private String name = null;

	@Expose
	private ArrayList<Annotation> annotations = new ArrayList<>();

	private String expID;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void setFields(String uri, String query, String uuid, UserType userType) {
		super.setFields(uri,query,uuid,userType);
		expID = uri.split("/")[2];
	}

	/**
	 * Used to validate the information that is needed
	 * to execute the actual command.
	 */
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

			if (!db.hasExperiment(expID)){
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"That experiment does not exist.");
			}

			for(Annotation annotation: annotations) {
				db.updateExperiment(name, annotation.getName(),
						annotation.getValue());
			}

			return new MinimalResponse(HttpStatusCode.OK);

		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());

		} finally {

			if (db != null) {
				db.close();
			}
		}
	}

}
