package command;

import command.annotation.*;
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
import command.user.PutUserPasswordCommand;
import command.user.DeleteUserCommand;
import command.user.PostUserCommand;
import database.subClasses.UserMethods.UserType;

import java.util.HashMap;

/**
 * Class containing the required user level for the different commands
 *
 * Created by ens11afk on 2015-04-29.
 */
public class UserRights {

    private static final HashMap<Class<? extends Command>,UserType> userRights;

    static {
        userRights = new HashMap<>();
        userRights.put(PostAnnotationFieldCommand.class, UserType.USER);
        userRights.put(PostAnnotationValueCommand.class, UserType.USER);
        userRights.put(PostExperimentCommand.class, UserType.USER);
        userRights.put(PostFileCommand.class, UserType.USER);
        userRights.put(PostGenomeReleaseCommand.class, UserType.USER);
        userRights.put(PutUserPasswordCommand.class, UserType.GUEST);
        userRights.put(PostUserCommand.class, UserType.ADMIN);
        userRights.put(DeleteAnnotationFieldCommand.class, UserType.USER);
        userRights.put(DeleteAnnotationValueCommand.class, UserType.USER);
        userRights.put(DeleteExperimentCommand.class, UserType.USER);
        userRights.put(DeleteFileCommand.class, UserType.USER);
        userRights.put(DeleteGenomeReleaseCommand.class, UserType.USER);
        userRights.put(DeleteUserCommand.class, UserType.ADMIN);
        userRights.put(PutAnnotationFieldCommand.class, UserType.USER);
        userRights.put(PutAnnotationValueCommand.class, UserType.USER);
        userRights.put(GetAnnotationCommand.class, UserType.GUEST);
        userRights.put(GetAnnotationPrivilegesCommand.class, UserType.GUEST);
        userRights.put(GetExperimentCommand.class, UserType.GUEST);
        userRights.put(GetFileCommand.class, UserType.USER);
        userRights.put(GetGenomeReleaseCommand.class, UserType.GUEST);
        userRights.put(GetGenomeReleaseSpeciesCommand.class, UserType.GUEST);
        userRights.put(GetProcessStatusCommand.class, UserType.GUEST);
        userRights.put(PutProcessCommand.class, UserType.USER);
        userRights.put(SearchCommand.class, UserType.GUEST);
        userRights.put(PutAnnotationPrivilegesCommand.class, UserType.USER);
        userRights.put(PutExperimentCommand.class, UserType.USER);
        userRights.put(PutFileCommand.class, UserType.USER);
    }


    /**
     * Returns the required user right for the command
     * @param command The command to get the user rights for
     * @return The required user rights
     */
    public static UserType getRights(Class<? extends Command> command){

        if (userRights.get(command) == null)
            return UserType.UNKNOWN;
        else
            return userRights.get(command);
    }
}
