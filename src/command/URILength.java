package command;

import java.util.HashMap;

/**
 * Class to be used as a reference for the "length" of the request URI. For
 * example the get experiment request URI looks like
 * /experiment/\<experiment-id\> and hence has the length 2.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class URILength {
	private static final HashMap<Class<? extends Command>, Integer>
			restfulLengths;

	static {
		restfulLengths = new HashMap<>();
		restfulLengths.put(LoginCommand.class, 1);
		restfulLengths.put(LogoutCommand.class, 1);
		restfulLengths.put(GetExperimentCommand.class, 2);
		restfulLengths.put(AddExperimentCommand.class, 1);
		restfulLengths.put(UpdateExperimentCommand.class, 2);
		restfulLengths.put(DeleteExperimentCommand.class, 2);
		restfulLengths.put(GetFileFromExperimentCommand.class, 2);
		restfulLengths.put(AddFileToExperimentCommand.class, 1);
		restfulLengths.put(UpdateFileInExperimentCommand.class, 2);
		restfulLengths.put(DeleteFileFromExperimentCommand.class, 2);
		restfulLengths.put(SearchForExperimentsCommand.class, 2);
		restfulLengths.put(CreateUserCommand.class, 1);
		restfulLengths.put(DeleteUserCommand.class, 2);
		restfulLengths.put(ProcessCommand.class, 2);
		restfulLengths.put(GetProcessStatusCommand.class, 1);
		restfulLengths.put(GetAnnotationInformationCommand.class, 1);
		restfulLengths.put(AddAnnotationFieldCommand.class, 2);
		restfulLengths.put(AddAnnotationValueCommand.class, 2);
		restfulLengths.put(DeleteAnnotationFieldCommand.class, 3);
		restfulLengths.put(GetAnnotationInformationCommand.class, 2);
		restfulLengths.put(UpdateAnnotationPrivilegesCommand.class, 3);
		restfulLengths.put(AddGenomeReleaseCommand.class, 1);
		restfulLengths.put(EditAnnotationValueCommand.class, 2);
		restfulLengths.put(EditAnnotationFieldCommand.class , 2);
		restfulLengths.put(DeleteAnnotationValueCommand.class, 4);
		restfulLengths.put(DeleteGenomeReleaseCommand.class, 3);
		restfulLengths.put(GetGenomeReleaseCommand.class, 1);
		restfulLengths.put(GetGenomeReleaseSpeciesCommand.class, 2);
	}

	/**
	 * Returns the "length" that a request URI should have. For example, in the
	 * request URI "/experiment/\<experiment-id\>" the length is 2.
	 * @param commandClass the command class.
	 * @return the "size" of the request URI.
	 */
	public static int get(Class<? extends Command> commandClass) {
		return restfulLengths.get(commandClass);
	}
}
