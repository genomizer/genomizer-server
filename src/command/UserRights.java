package command;

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
        userRights.put(AddAnnotationFieldCommand.class, UserType.USER);
        userRights.put(AddAnnotationValueCommand.class, UserType.USER);
        userRights.put(AddExperimentCommand.class, UserType.USER);
        userRights.put(AddFileToExperimentCommand.class, UserType.USER);
        userRights.put(AddGenomeReleaseCommand.class, UserType.USER);
        userRights.put(ChangeUserPasswordCommand.class, UserType.GUEST);
        userRights.put(CreateUserCommand.class, UserType.ADMIN);
        userRights.put(DeleteAnnotationFieldCommand.class, UserType.USER);
        userRights.put(DeleteAnnotationValueCommand.class, UserType.USER);
        userRights.put(DeleteExperimentCommand.class, UserType.USER);
        userRights.put(DeleteFileFromExperimentCommand.class, UserType.USER);
        userRights.put(DeleteGenomeReleaseCommand.class, UserType.USER);
        userRights.put(DeleteUserCommand.class, UserType.ADMIN);
        userRights.put(EditAnnotationFieldCommand.class, UserType.USER);
        userRights.put(EditAnnotationValueCommand.class, UserType.USER);
        userRights.put(GetAnnotationInformationCommand.class, UserType.GUEST);
        userRights.put(GetAnnotationPrivilegesCommand.class, UserType.GUEST);
        userRights.put(GetExperimentCommand.class, UserType.GUEST);
        userRights.put(GetFileFromExperimentCommand.class, UserType.USER);
        userRights.put(GetGenomeReleaseCommand.class, UserType.GUEST);
        userRights.put(GetGenomeReleaseSpeciesCommand.class, UserType.GUEST);
        userRights.put(GetProcessStatusCommand.class, UserType.USER);
        userRights.put(ProcessCommand.class, UserType.USER);
        userRights.put(SearchForExperimentsCommand.class, UserType.GUEST);
        userRights.put(UpdateAnnotationPrivilegesCommand.class, UserType.USER);
        userRights.put(UpdateExperimentCommand.class, UserType.USER);
        userRights.put(UpdateFileInExperimentCommand.class, UserType.USER);
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
