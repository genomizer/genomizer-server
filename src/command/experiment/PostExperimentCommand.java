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
		try (DatabaseAccessor db = initDB()) {

		} catch (SQLException e) {

		} catch (IOException e) {

		}

//		DatabaseAccessor db = null;
//		try {
//			db = initDB();
//
//			ArrayList<String> anns = db.getAllAnnotationLabels();
//			for(String ann:anns){
//				database.containers.Annotation anno = db.getAnnotationObject(ann);
//				if(anno.isRequired){
//					if(!annotationsContains(anno)){
//						return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
//								"Not all forced values are present. Missing " +
//										"atleast " + anno.label);
//					}
//				}
//			}
//
//			db.addExperiment(name);
//			for(Annotation annotation: annotations) {
//				db.annotateExperiment(name, annotation.getName(),
//						annotation.getValue());
//			}
//			return new MinimalResponse(HttpStatusCode.OK);
//		} catch (IOException | SQLException e) {
//			e.printStackTrace();
//			Debug.log("Adding of experiment " + name + " didn't work, reason: " +
//					e.getMessage());
//			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
//					"Adding of experiment " + name + " didn't work due to ." +
//							e.getMessage());
//		} finally {
//			if (db != null) {
//				db.close();
//			}
//		}

		return null;
	}

	private boolean annotationsContains(database.containers.Annotation anno) {
		for(Annotation ann: this.annotations){
			if(ann.getName().equals(anno.label)){
				return true;
			}
		}
		return false;
	}
}
