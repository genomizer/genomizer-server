package command;

import java.util.HashMap;

/**
 * Contains all of the Command class objects in a hash map. They can be
 * retrieved using the request method followed by a " " and the request context
 * as a key.
 */
public class CommandClasses {
    private static final HashMap<String, Class<? extends Command>> classes;

    static {
        classes = new HashMap<>();
        classes.put("POST /annotation/field", AddAnnotationFieldCommand.class);
        classes.put("POST /annotation/value", AddAnnotationValueCommand.class);
        classes.put("POST /experiment", AddExperimentCommand.class);
        classes.put("POST /file", AddFileToExperimentCommand.class);
        classes.put("POST /genomeRelease", AddGenomeReleaseCommand.class);
        classes.put("PUT /user", ChangeUserPasswordCommand.class);
        classes.put("POST /user", CreateUserCommand.class);
        classes.put("DELETE /annotation/field", DeleteAnnotationFieldCommand.
                class);
        classes.put("DELETE /annotation/value", DeleteAnnotationValueCommand.
                class);
        classes.put("DELETE /experiment", DeleteExperimentCommand.class);
        classes.put("DELETE /file", DeleteFileFromExperimentCommand.class);
        classes.put("DELETE /genomeRelease/", DeleteGenomeReleaseCommand.class);
        classes.put("DELETE /user", DeleteUserCommand.class);
        classes.put("PUT /annotation/field", EditAnnotationFieldCommand.class);
        classes.put("PUT /annotation/value", EditAnnotationValueCommand.class);
        classes.put("GET /annotation", GetAnnotationInformationCommand.class);
        classes.put("GET /sysadm", GetAnnotationPrivilegesCommand.class);
        classes.put("GET /experiment", GetExperimentCommand.class);
        classes.put("GET /file", GetFileFromExperimentCommand.class);
        classes.put("GET /genomeRelease", GetGenomeReleaseCommand.class);
        classes.put("GET /genomeRelease/", GetGenomeReleaseSpeciesCommand.
                class);
        classes.put("GET /process", GetProcessStatusCommand.class);
        classes.put("GET /token", IsTokenValidCommand.class);
        classes.put("POST /login", LoginCommand.class);
        classes.put("DELETE /login", LogoutCommand.class);
        classes.put("PUT /process/rawtoprofile", ProcessCommand.class);
        classes.put("GET /search/", SearchForExperimentsCommand.class);
        classes.put("PUT /sysadm/annpriv", UpdateAnnotationPrivilegesCommand.
                class);
        classes.put("PUT /experiment", UpdateExperimentCommand.class);
        classes.put("PUT /file", UpdateFileInExperimentCommand.class);
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
