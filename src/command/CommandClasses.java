package command;

import command.admin.PutUserAdminCommand;
import command.annotation.*;
import command.connection.IsTokenValidCommand;
import command.connection.PostLoginCommand;
import command.connection.DeleteLoginCommand;
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
import command.user.PutUserCommand;
import command.user.PutUserPasswordCommand;
import command.admin.DeleteUserCommand;
import command.admin.PostUserCommand;

import java.util.HashMap;

/**
 * Contains all of the Command class objects in a hash map. They can be
 * retrieved using the request method followed by a " " and the request context
 * as a key.
 */
@SuppressWarnings("deprecation")
public class CommandClasses {
    private static final HashMap<String, Class<? extends Command>> classes;

    static {
        classes = new HashMap<>();
        classes.put("POST /annotation/field", PostAnnotationFieldCommand.class);
        classes.put("POST /annotation/value", PostAnnotationValueCommand.class);
        classes.put("POST /experiment", PostExperimentCommand.class);
        classes.put("POST /file", PostFileCommand.class);
        classes.put("POST /genomeRelease", PostGenomeReleaseCommand.class);
        classes.put("DELETE /annotation/field", DeleteAnnotationFieldCommand.
                class);
        classes.put("DELETE /annotation/value", DeleteAnnotationValueCommand.
                class);
        classes.put("DELETE /experiment", DeleteExperimentCommand.class);
        classes.put("DELETE /file", DeleteFileCommand.class);
        classes.put("DELETE /genomeRelease/", DeleteGenomeReleaseCommand.class);

        classes.put("PUT /annotation/field", PutAnnotationFieldCommand.class);
        classes.put("PUT /annotation/value", PutAnnotationValueCommand.class);
        classes.put("GET /annotation", GetAnnotationCommand.class);
        classes.put("GET /sysadm", GetAnnotationPrivilegesCommand.class);
        classes.put("GET /experiment", GetExperimentCommand.class);
        classes.put("GET /file", GetFileCommand.class);
        classes.put("GET /genomeRelease", GetGenomeReleaseCommand.class);
        classes.put("GET /genomeRelease/", GetGenomeReleaseSpeciesCommand.
                class);
        classes.put("GET /process", GetProcessStatusCommand.class);
        classes.put("GET /token", IsTokenValidCommand.class);
        classes.put("POST /login", PostLoginCommand.class);
        classes.put("DELETE /login", DeleteLoginCommand.class);
        classes.put("PUT /process/rawtoprofile", PutProcessCommand.class);
        classes.put("GET /search/", SearchCommand.class);
        classes.put("PUT /sysadm/annpriv", PutAnnotationPrivilegesCommand.
                class);
        classes.put("PUT /experiment", PutExperimentCommand.class);
        classes.put("PUT /file", PutFileCommand.class);
        classes.put("POST /admin/user", PostUserCommand.class);
        classes.put("DELETE /admin/user/", DeleteUserCommand.class);
        classes.put("PUT /admin/user", PutUserAdminCommand.class);
        classes.put("PUT /user", PutUserPasswordCommand.class);
        classes.put("PUT /user", PutUserCommand.class);
    }

    /**
     * Returns the class object corresponding to the given context.
     * @param context the context of the command, for example "POST /experiment"
     *                would be used to retrieve the AddExperiment class object.
     * @return the class object corresponding to the given context.
     */
    public static Class<? extends Command> get(String context) {
        return classes.get(context);
    }
}
