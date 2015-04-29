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
	private static final HashMap<Class<? extends Command>, Integer> uriLengths;

	static {
		uriLengths = new HashMap<>();
		uriLengths.put(LoginCommand.class, 1);
		uriLengths.put(LogoutCommand.class, 1);
		uriLengths.put(GetExperimentCommand.class, 2);
		uriLengths.put(AddExperimentCommand.class, 1);
		uriLengths.put(UpdateExperimentCommand.class, 2);
		uriLengths.put(DeleteExperimentCommand.class, 2);
		uriLengths.put(GetFileFromExperimentCommand.class, 2);
		uriLengths.put(AddFileToExperimentCommand.class, 1);
		uriLengths.put(UpdateFileInExperimentCommand.class, 2);
		uriLengths.put(DeleteFileFromExperimentCommand.class, 2);
		uriLengths.put(SearchForExperimentsCommand.class, 2);
		uriLengths.put(CreateUserCommand.class, 1);
		uriLengths.put(DeleteUserCommand.class, 2);
		uriLengths.put(ProcessCommand.class, 2);
		uriLengths.put(GetProcessStatusCommand.class, 1);
		uriLengths.put(GetAnnotationInformationCommand.class, 1);
		uriLengths.put(AddAnnotationFieldCommand.class, 2);
		uriLengths.put(AddAnnotationValueCommand.class, 2);
		uriLengths.put(DeleteAnnotationFieldCommand.class, 3);
		uriLengths.put(GetAnnotationInformationCommand.class, 2);
		uriLengths.put(UpdateAnnotationPrivilegesCommand.class, 3);
		uriLengths.put(AddGenomeReleaseCommand.class, 1);
		uriLengths.put(EditAnnotationValueCommand.class, 2);
		uriLengths.put(EditAnnotationFieldCommand.class , 2);
		uriLengths.put(DeleteAnnotationValueCommand.class, 4);
		uriLengths.put(DeleteGenomeReleaseCommand.class, 3);
		uriLengths.put(GetGenomeReleaseCommand.class, 1);
		uriLengths.put(GetGenomeReleaseSpeciesCommand.class, 2);
	}

	/**
	 * Returns the "length" that a request URI should have. For example, in the
	 * request URI "/experiment/\<experiment-id\>" the length is 2.
	 * @param commandClass the command class.
	 * @return the "length" of the request URI.
	 */
	public static int get(Class<? extends Command> commandClass) {
		return uriLengths.get(commandClass);
	}
}
