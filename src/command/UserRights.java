package command;

import command.admin.*;
import command.annotation.*;
import command.convertfile.*;
import command.experiment.*;
import command.file.*;
import command.genomerelease.*;
import command.process.*;
import command.search.*;
import command.user.*;
import database.subClasses.UserMethods.UserType;

import java.util.HashMap;

/**
 * Class containing the required user level for the different commands
 *
 * Created by ens11afk on 2015-04-29.
 */
@SuppressWarnings("deprecation")
public class UserRights {

    private static final HashMap<Class<? extends Command>,UserType> userRights;

    static {
        userRights = new HashMap<>();

        /** Annotation */
        userRights.put(PutAnnotationFieldCommand.class, UserType.USER);
        userRights.put(PutAnnotationValueCommand.class, UserType.USER);
        userRights.put(PutAnnotationPrivilegesCommand.class, UserType.USER);
        userRights.put(PostAnnotationFieldCommand.class, UserType.USER);
        userRights.put(PostAnnotationValueCommand.class, UserType.USER);
        userRights.put(GetAnnotationCommand.class, UserType.GUEST);
        userRights.put(GetAnnotationPrivilegesCommand.class, UserType.GUEST);
        userRights.put(DeleteAnnotationFieldCommand.class, UserType.USER);
        userRights.put(DeleteAnnotationValueCommand.class, UserType.USER);

        /** Experiment */
        userRights.put(PutExperimentCommand.class, UserType.USER);
        userRights.put(PostExperimentCommand.class, UserType.USER);
        userRights.put(GetExperimentCommand.class, UserType.GUEST);
        userRights.put(DeleteExperimentCommand.class, UserType.USER);

        /** File */
        userRights.put(PutFileCommand.class, UserType.USER);
        userRights.put(PostFileCommand.class, UserType.USER);
        userRights.put(GetFileCommand.class, UserType.USER);
        userRights.put(DeleteFileCommand.class, UserType.USER);

        /** GenomeRelease */
        userRights.put(PostGenomeReleaseCommand.class, UserType.USER);
        userRights.put(GetGenomeReleaseCommand.class, UserType.GUEST);
        userRights.put(GetGenomeReleaseSpeciesCommand.class, UserType.GUEST);
        userRights.put(DeleteGenomeReleaseCommand.class, UserType.USER);

        /** User */
        userRights.put(PutUserCommand.class, UserType.USER);
        userRights.put(PutUserPasswordCommand.class, UserType.USER);
        userRights.put(GetUserCommand.class,UserType.USER);

        /** Admin */
        userRights.put(PutAdminUserCommand.class, UserType.ADMIN);
        userRights.put(PostAdminUserCommand.class, UserType.ADMIN);
        userRights.put(GetAdminUserCommand.class,UserType.ADMIN);
        userRights.put(DeleteAdminUserCommand.class, UserType.ADMIN);

        /** Process */
        userRights.put(PutProcessCommand.class, UserType.USER);
        userRights.put(ProcessCommands.class, UserType.USER);
        userRights.put(GetProcessStatusCommand.class, UserType.USER);

        /** Search */
        userRights.put(SearchCommand.class, UserType.GUEST);

        /** ConvertFile */
        userRights.put(PutConvertFileCommand.class, UserType.USER);
    }


    /**
     * Returns the required user right for the command
     * @param command The command to get the user rights for
     * @return The required user rights
     */
    public static UserType getRights(Class<? extends Command> command){

        UserType userType = userRights.get(command);
        return userType != null ? userType : UserType.UNKNOWN;
    }
}
