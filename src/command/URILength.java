package command;

import command.admin.PutUserAdminCommand;
import command.annotation.*;
import command.connection.DeleteLoginCommand;
import command.connection.PostLoginCommand;
import command.convertfile.PutConvertFileCommand;
import command.experiment.DeleteExperimentCommand;
import command.experiment.GetExperimentCommand;
import command.experiment.PostExperimentCommand;
import command.experiment.PutExperimentCommand;
import command.file.DeleteFileCommand;
import command.file.GetFileCommand;
import command.file.PostFileCommand;
import command.file.PutFileCommand;
import command.genomerelease.DeleteGenomeReleaseCommand;
import command.genomerelease.GetGenomeReleaseCommand;
import command.genomerelease.GetGenomeReleaseSpeciesCommand;
import command.genomerelease.PostGenomeReleaseCommand;
import command.process.GetProcessStatusCommand;
import command.process.PutProcessCommand;
import command.search.SearchCommand;
import command.admin.DeleteUserCommand;
import command.admin.PostUserCommand;
import command.user.PutUserCommand;

import java.util.HashMap;

/**
 * Class to be used as a reference for the "length" of the request URI. For
 * example the get experiment request URI looks like
 * /experiment/\<experiment-id\> and hence has the length 2.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
@SuppressWarnings("deprecation")
public class URILength {
	private static final HashMap<Class<? extends Command>, Integer> uriLengths;

	static {
		uriLengths = new HashMap<>();
		uriLengths.put(PostLoginCommand.class, 1);
		uriLengths.put(DeleteLoginCommand.class, 1);
		uriLengths.put(GetExperimentCommand.class, 2);
		uriLengths.put(PostExperimentCommand.class, 1);
		uriLengths.put(PutExperimentCommand.class, 2);
		uriLengths.put(DeleteExperimentCommand.class, 2);
		uriLengths.put(GetFileCommand.class, 2);
		uriLengths.put(PostFileCommand.class, 1);
		uriLengths.put(PutFileCommand.class, 2);
		uriLengths.put(DeleteFileCommand.class, 2);
		uriLengths.put(SearchCommand.class, 2);
		uriLengths.put(PostUserCommand.class, 2);
		uriLengths.put(DeleteUserCommand.class, 3);
		uriLengths.put(PutProcessCommand.class, 2);
		uriLengths.put(GetProcessStatusCommand.class, 1);
		uriLengths.put(GetAnnotationCommand.class, 1);
		uriLengths.put(PostAnnotationFieldCommand.class, 2);
		uriLengths.put(PostAnnotationValueCommand.class, 2);
		uriLengths.put(DeleteAnnotationFieldCommand.class, 3);
		uriLengths.put(PutAnnotationPrivilegesCommand.class, 3);
		uriLengths.put(PostGenomeReleaseCommand.class, 1);
		uriLengths.put(PutAnnotationValueCommand.class, 2);
		uriLengths.put(PutAnnotationFieldCommand.class , 2);
		uriLengths.put(DeleteAnnotationValueCommand.class, 4);
		uriLengths.put(DeleteGenomeReleaseCommand.class, 3);
		uriLengths.put(GetGenomeReleaseCommand.class, 1);
		uriLengths.put(GetGenomeReleaseSpeciesCommand.class, 2);
		uriLengths.put(PutConvertFileCommand.class, 1);
		uriLengths.put(PutUserAdminCommand.class, 2);
		uriLengths.put(PutUserCommand.class, 1);
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
