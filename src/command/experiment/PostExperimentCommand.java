package command.experiment;

import com.google.gson.annotations.Expose;
import command.Annotation;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command used to add an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostExperimentCommand extends Command {
	@Expose
	private String name = null;
	@Expose
	private ArrayList<Annotation> annotations = null;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.EXPID, "Experiment name");

		if (annotations == null || annotations.size() < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify " +
					"annotations for the experiment.");
		}

		for (int i =0;i<annotations.size();i++) {
			if (annotations.get(i) == null) {
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
			if (!buildAnnotationList().containsAll(db.
					getAllForcedAnnotationLabels())) {
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Adding experiment '" + name + "' unsuccessful, not " +
								"all forced values are present.");
			} else {
				db.addExperiment(name);
				for (Annotation annotation : annotations) {
					db.annotateExperiment(name, annotation.getName(),
							annotation.getValue());
				}

				response = new MinimalResponse(HttpStatusCode.OK);
			}
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Adding experiment '" + name + "' unsuccessful due to " +
							"temporary database problems");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Adding experiment '" + name + "' unsuccessful. " +
							e.getMessage());
		}

		return response;
	}

	private List<String> buildAnnotationList() {
		ArrayList<String> nameList = new ArrayList<>();

		for (Annotation annotation : annotations) {
			nameList.add(annotation.getName());
		}

		return nameList;
	}
}
