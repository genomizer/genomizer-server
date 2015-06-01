package command;

import command.admin.DeleteAdminUserCommand;
import command.admin.GetAdminUserListCommand;
import command.admin.PostAdminUserCommand;
import command.admin.PutAdminUserCommand;
import command.annotation.*;
import command.connection.*;
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
import command.process.*;
import command.search.SearchCommand;
import command.user.GetUserCommand;
import command.user.PutUserCommand;

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
        // Connection Commands
        classes.put("GET /token", IsTokenValidCommand.class);
        classes.put("POST /login", PostLoginCommand.class);
        classes.put("DELETE /login", DeleteLoginCommand.class);

        // Experiment Commands
        classes.put("DELETE /experiment/", DeleteExperimentCommand.class);
        classes.put("POST /experiment", PostExperimentCommand.class);
        classes.put("GET /experiment/", GetExperimentCommand.class);
        classes.put("PUT /experiment/", PutExperimentCommand.class);

        //File commands
        classes.put("POST /file", PostFileCommand.class);
        classes.put("DELETE /file/", DeleteFileCommand.class);
        classes.put("GET /file/", GetFileCommand.class);
        classes.put("PUT /file/", PutFileCommand.class);

        //File Conversion commands
        classes.put("PUT /convertfile", PutConvertFileCommand.class);

        //Search commands
        classes.put("GET /search/", SearchCommand.class);

        //User commands
        //classes.put("PUT /user", PutUserPasswordCommand.class);
        classes.put("PUT /user", PutUserCommand.class);
        classes.put("GET /user/", GetUserCommand.class);

        //Admin commands
        classes.put("POST /admin/user", PostAdminUserCommand.class);
        classes.put("DELETE /admin/user/", DeleteAdminUserCommand.class);
        classes.put("PUT /admin/user", PutAdminUserCommand.class);
        classes.put("GET /admin/userlist", GetAdminUserListCommand.class);

        //Processing
        classes.put("GET /process", GetProcessStatusCommand.class);
        classes.put("DELETE /process", CancelProcessCommand.class);
        classes.put("PUT /process/rawtoprofile", PutProcessCommand.class);
        classes.put("GET /process/dummy", GetProcessDummyCommand.class);

        //Annotation handling commands
        classes.put("POST /annotation/field", PostAnnotationFieldCommand.class);
        classes.put("POST /annotation/value", PostAnnotationValueCommand.class);
        classes.put("DELETE /annotation/field/", DeleteAnnotationFieldCommand.
                class);
        classes.put("DELETE /annotation/value/", DeleteAnnotationValueCommand.
                class);
        classes.put("PUT /annotation/field", PutAnnotationFieldCommand.class);
        classes.put("PUT /annotation/value", PutAnnotationValueCommand.class);
        classes.put("GET /annotation", GetAnnotationCommand.class);

        //Genome Release handling commands
        classes.put("POST /genomeRelease", PostGenomeReleaseCommand.class);
        classes.put("DELETE /genomeRelease/", DeleteGenomeReleaseCommand.
                class);
        classes.put("GET /genomeRelease", GetGenomeReleaseCommand.class);
        classes.put("GET /genomeRelease/", GetGenomeReleaseSpeciesCommand.
                class);
        classes.put("PUT /process/processCommands", ProcessCommands.class);

        //Geo commands



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