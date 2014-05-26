package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import database.containers.Annotation;
import database.containers.ChainFile;
import database.containers.Experiment;
import database.containers.FileTuple;
import database.containers.Genome;
import database.subClasses.*;

/**
 * PREREQUISITES: The construction parameters must reference a postgresql
 * database with the Genomizer database tables preloaded. This is done by
 * running the genomizer_database_tables.sql.
 *
 * DatabaseAccessor manipulates the underlying postgresql database using SQL
 * commands.
 *
 * Developed by the Data storage group for the Genomizer Project, Software
 * Engineering course at Umeå University 2014.
 *
 * @author dv12rwt, Ruaridh Watt
 * @author dv12kko, Kenny Kunto
 * @author dv12ann, André Niklasson
 * @author dv12can, Carl Alexandersson
 * @author yhi04jeo, Jonas Engbo
 * @author oi11mhn, Mattias Hinnerson
 */
public class DatabaseAccessor {

    public static Integer FREETEXT = 1;
    public static Integer DROPDOWN = 2;
    private Connection conn;
    public static final String DATAFOLDER = File.separator + "var"
            + File.separator + "www" + File.separator + "data" + File.separator;
    private FilePathGenerator fpg;
    private PubMedToSQLConverter pm2sql;
    private UserMethods userMethods;
    private ExperimentMethods expMethods;
    private AnnotationMethods annoMethods;
    private FileMethods fileMethods;
    private GenomeMethods genMethods;

    /**
     * Creates a databaseAccessor that opens a connection to a database.
     *
     * @param String
     *            username - The username to log in to the database as. Should
     *            be "c5dv151_vt14" as of now.
     * @param String
     *            password - The password to log in to the database. Should be
     *            "shielohh" as of now.
     * @param String
     *            host - The name of the database management system. Will
     *            problebly always be "postgres" unless the DMS is switched with
     *            something else.
     * @param String
     *            database - The name of the database
     * @throws SQLException
     * @throws IOException
     */
    public DatabaseAccessor(String username, String password, String host,
            String database) throws SQLException, IOException {

        String url = "jdbc:postgresql://" + host + "/" + database;

        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        conn = DriverManager.getConnection(url, props);

        fpg = new FilePathGenerator(DATAFOLDER);
        pm2sql = new PubMedToSQLConverter();
        userMethods = new UserMethods(conn);
        annoMethods = new AnnotationMethods(conn);
        expMethods = new ExperimentMethods(conn, fpg, annoMethods);
        fileMethods = new FileMethods(conn, fpg, expMethods);
        genMethods = new GenomeMethods(conn, fpg);
    }

    /**
     * Closes the connection to the database, releasing all resources it uses.
     */
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Could not close database connection!");
        }
    }

    /**
     * Public method to check if the instance of the class is connected to a
     * database.
     *
     * @return boolean - true if it is connected, otherwise false.
     */
    public boolean isConnected() {
        try {
            return !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Searches the database for Experiments. The search criteria are specified
     * in a String that has the same format as that used by PubMed:
     *
     * <NOT> <Value>[<Label>] <AND|OR|NOT> <Value>[<Label>] ...
     *
     * Round brackets should be used to disambiguate the logical expression.
     * Example: "(Human[Species] OR Fly[Species]) AND Joe Bloggs[Uploader]"
     *
     * @param String
     *            pubMedString - The String containing the search criteria in
     *            PubMed format.
     * @return List<Experiment> - A List of experiments containing file that
     *         fullfill the criteria specifies in the pubMedString.
     * @throws IOException
     *             - If the pubMedString is not in the right format
     * @throws SQLException
     *             - if the query does not succeed
     * @throws ParseException
     *             - if the Date is not in the right format. (yyyy-mm-dd).
     */
    public List<Experiment> search(String pubMedString) throws IOException,
            SQLException, ParseException {

        if (pubMedString.isEmpty()) {
            return getAllExperiments();
        }

        isPubMedStringValid(pubMedString);

        if (pm2sql.hasFileConstraint(pubMedString)) {
            return searchFiles(pubMedString);
        }
        return searchExperiments(pubMedString);
    }

    /**
     * Gets all experiments.
     *
     * @return a list of Experiment objects.
     * @throws SQLException
     */
    private List<Experiment> getAllExperiments() throws SQLException {
        String query = "SELECT * FROM Experiment";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        Experiment e;
        List<Experiment> exps = new ArrayList<Experiment>();
        while (rs.next()) {
            e = new Experiment(rs.getString("ExpID"));
            e = expMethods.fillAnnotations(e);
            e = expMethods.fillFiles(e);
            exps.add(e);
        }
        return exps;
    }

    /**
     * Internal method! Checks that the pubmed string is valid.
     *
     * @param String
     *            pubMedString
     * @return boolean - true if ok else throws Exception
     * @throws IOException
     */
    public boolean isPubMedStringValid(String pubMedString) throws IOException {
        int squareBracketsStart = 0, squareBracketsStop = 0;
        char last = 0;
        for (int i = 0; i < pubMedString.length(); i++) {
            if (squareBracketsStart + squareBracketsStop != 0) {
                if (last == pubMedString.charAt(i)) {
                    throw new IOException("Missformed PubMed String");
                }
            }
            if (pubMedString.charAt(i) == '[') {
                squareBracketsStart++;
                last = pubMedString.charAt(i);
            } else if (pubMedString.charAt(i) == ']') {
                squareBracketsStop++;
                last = pubMedString.charAt(i);
            }
        }
        if (squareBracketsStart == squareBracketsStop) {
            return true;
        } else {
            throw new IOException("Missformed PubMed String");
        }
    }

    /**
     * Returns an ArrayList which contains the usernames of all the users in the
     * database in the form of strings.
     *
     * @return List<String> - an ArrayList of usernames
     * @throws SQLException
     *             - if the query does not succeed
     */
    public List<String> getUsers() throws SQLException {
        return userMethods.getUsers();
    }

    /**
     * Method to add a new user to the database.
     *
     * @param String
     *            - the username
     * @param String
     *            - the password
     * @param String
     *            - the role given to the user ie. "Admin"
     * @throws SQLException
     * @throws IOException
     * @throws DuplicatePrimaryKeyException
     */
    public void addUser(String username, String password, String role,
            String fullName, String email) throws SQLException, IOException {
        userMethods.addUser(username, password, role, fullName, email);
    }

    /**
     * Deletes a user from the database.
     *
     * @param String
     *            username - the username of the user to be deleted.
     * @throws SQLException
     *             - if the query does not succeed
     */
    public void deleteUser(String username) throws SQLException {
        userMethods.deleteUser(username);
    }

    /**
     * Returns the password for the given user. Used for login.
     *
     * @param String
     *            user - the username as string
     * @return String - the password
     * @throws SQLException
     *             - if the query does not succeed
     */
    public String getPassword(String user) throws SQLException {
        return userMethods.getPassword(user);
    }

    /**
     * Changes the password for a user.
     *
     * @param String
     *            username - the user to change the password for
     * @param String
     *            newPassword - the new password
     * @return int - the number of tuples updated in the database
     * @throws SQLException
     *             - if the query does not succeed
     * @throws IOException
     *             - if an argument is empty or null
     */
    public int resetPassword(String username, String newPassword)
            throws SQLException, IOException {
        return userMethods.resetPassword(username, newPassword);
    }

    /**
     * Gets the role (permissions) for a user.
     *
     * @param String
     *            username - the user to get the role for
     * @return String - the role as a string
     * @throws SQLException
     *             - if the query does not succeed
     */
    public String getRole(String username) throws SQLException {
        return userMethods.getRole(username);
    }

    /**
     * Sets the role (permissions) for the user.
     *
     * @param String
     *            username - the user to set the role for
     * @param String
     *            role - the role to set for the user
     * @return int - returns the number of tuples updated in the database
     * @throws SQLException
     *             - if the query does not succeed
     */
    public int setRole(String username, String role) throws SQLException {
        return userMethods.setRole(username, role);
    }

    /**
     * Gets an experiment from the database.
     *
     * @param String
     *            expID - the ID of the experiment
     * @return Experiment - an Experiment object or null if the experiment does
     *         not exist.
     * @throws SQLException
     *             - if the query does not succeed
     */
    public Experiment getExperiment(String expID) throws SQLException {
        return expMethods.getExperiment(expID);
    }

    /**
     * Adds an experiment ID to the database.
     *
     * @param String
     *            expID - the ID for the experiment.
     * @return int - the number of tuples inserted in the database.
     * @throws SQLException
     *             - if the query does not succeed
     * @throws IOException
     * @throws DuplicatePrimaryKeyException
     *             If the experiment already exists.
     */
    public int addExperiment(String expID) throws SQLException, IOException {
        return expMethods.addExperiment(expID);
    }

    /**
     * Deletes an experiment from the database.
     *
     * @param String
     *            expId - the experiment ID.
     * @return int - the number of tuples deleted.
     * @throws SQLException
     *             - if the query does not succeed.
     * @throws IOException
     *             If Experiment contains at least one file. (All files relating
     *             to an experiment must be deleted first before an experiment
     *             can be deleted from the database)
     */
    public int deleteExperiment(String expId) throws SQLException, IOException {
        return expMethods.deleteExperiment(expId);
    }

    /**
     * Checks if a given experiment ID exists in the database.
     *
     * @param String
     *            expID - the experiment ID to look for.
     * @return boolean - true if the experiment exists in the database, else
     *         false.
     * @throws SQLException
     *             - if the query does not succeed
     */
    public boolean hasExperiment(String expID) throws SQLException {
        return getExperiment(expID) != null;
    }

    /**
     * Updates a value of a single annotation of a unique experiment
     *
     * @param String
     *            expID - the name of the experiment to annotate.
     * @param String
     *            label - the annotation to set.
     * @param String
     *            value - the value of the annotation.
     * @return int - the number of tuples updated in the database.
     * @throws SQLException
     *             - if the query does not succeed
     * @throws IOException
     *             - if the value is invalid for the annotation type.
     */
    public int updateExperiment(String expID, String label, String value)
            throws SQLException, IOException {
        return expMethods.updateExperiment(expID, label, value);
    }

    /**
     * Annotates an experiment with the given label and value. Checks so that
     * the value is valid if it is a drop down annotation.
     *
     * @param String
     *            expID - the name of the experiment to annotate
     * @param String
     *            label - the annotation to set
     * @param String
     *            value - the value of the annotation
     * @return int - the number of tuples updated in the database
     * @throws SQLException
     *             - if the query does not succeed
     * @throws IOException
     *             - if the value is invalid for the annotation type
     */
    public int annotateExperiment(String expID, String label, String value)
            throws SQLException, IOException {
        return expMethods.annotateExperiment(expID, label, value);
    }

    /**
     * Deletes one annotation from a specific experiment.
     *
     * @param String
     *            expID - the experiment to delete the annotation from
     * @param String
     *            label - the name of the annotation
     * @return int - the number of tuples deleted from the database
     * @throws SQLException
     *             - if the query does not succeed
     */
    public int removeExperimentAnnotation(String expID, String label)
            throws SQLException {
        return expMethods.removeExperimentAnnotation(expID, label);
    }

    /**
     * Gets all the annotation possibilities from the database.
     *
     * @return HashMap<String, Integer> - a Map with the label string as key and
     *         datatype as value. The possible datatypes are FREETEXT and
     *         DROPDOWN.
     * @throws SQLException
     *             - if the query does not succeed
     */
    public Map<String, Integer> getAnnotations() throws SQLException {
        return annoMethods.getAnnotations();
    }

    /**
     * Creates an Annotation object from an annotation label.
     *
     * @param String
     *            label - the name of the annotation to create the object for.
     * @return Annotation - the Annotation object. If the label does not exist,
     *         then null will be returned.
     * @throws SQLException
     *             - if the query does not succeed.
     */
    public Annotation getAnnotationObject(String label) throws SQLException {
        return annoMethods.getAnnotationObject(label);
    }

    /**
     * Creates a list of Annotation objects from a list of annotation labels.
     *
     * @param String
     *            labels - the list of labels.
     * @return List<Annotation> - will return a list with all the annotations
     *         with valid labels. If the list with labels is empty or none of
     *         the labels are valid, then it will return null.
     * @throws SQLException
     *             - if the query does not succeed.
     */
    public List<Annotation> getAnnotationObjects(List<String> labels)
            throws SQLException {
        return annoMethods.getAnnotationObjects(labels);
    }

    /**
     * Finds all annotationLabels that exist in the database, example of labels:
     * sex, tissue, etc... Finds all annotationLabels that exist in the
     * database, example of labels: sex, tissue, etc...
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getAllAnnotationLabels() {
        return annoMethods.getAllAnnotationLabels();
    }

    /**
     * Gets the datatype of a given annotation.
     *
     * @param String
     *            label - annotation label.
     * @return The integer value of the annotation type (FREETEXT or DROPDOWN)
     *         or 0 if there is no annotation with this label
     * @throws SQLException
     *             - if the query does not succeed
     */
    public Integer getAnnotationType(String label) throws SQLException {
        return annoMethods.getAnnotationType(label);
    }

    /**
     * Gets the default value for a annotation if there is one, If not it
     * returns NULL.
     *
     * @param String
     *            annotationLabel -the name of the annotation to check
     * @return String - The defult value or NULL.
     * @throws SQLException
     */
    public String getDefaultAnnotationValue(String annotationLabel)
            throws SQLException {
        return annoMethods.getDefaultAnnotationValue(annotationLabel);
    }

    /**
     * Deletes an annotation from the list of possible annotations. Label
     * SPECIES can't be changed because of dependencies in other tables.
     *
     * @param String
     *            label - the label of the annotation to delete.
     * @return int - the number of tuples deleted in the database.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     *             if label = "Species"
     */
    public int deleteAnnotation(String label) throws SQLException, IOException {
        return annoMethods.deleteAnnotation(label);
    }

    /**
     * Adds a free text annotation to the list of possible annotations.
     *
     * @param String
     *            label - the name of the annotation.
     * @param boolean required - if the annotation should be forced or not
     * @param String
     *            defaultValue - the default value this field should take or
     *            null if a default value is not required
     * @return int - the number of tuples updated in the database.
     * @throws SQLException
     *             - if the query does not succeed
     * @throws IOException
     *             , if the label is an existing file- annotation or contains
     *             invalid characters.
     */
    public int addFreeTextAnnotation(String label, String defaultValue,
            boolean required) throws SQLException, IOException {
        return annoMethods.addFreeTextAnnotation(label, defaultValue, required);
    }

    /**
     * Checks if a given annotation is required to be filled by the user.
     *
     * @param String
     *            annotationLabel - the name of the annotation to check
     * @return boolean - true if it is required, else false
     * @throws SQLException
     */
    public boolean isAnnotationRequiered(String annotationLabel)
            throws SQLException {
        return annoMethods.isAnnotationRequiered(annotationLabel);
    }

    /**
     * Method to add a value to a existing DropDown annotation.
     *
     * @param String
     *            label - the label of the chosen DropDown annotation
     * @param String
     *            value - the value that will be added to the DropDown
     *            annotation
     * @return int - how many rows that were added to the database.
     * @throws SQLException
     *             - if the value already exist or another SQL error.
     * @throws IOException
     *             - if the chosen label does not represent a DropDown
     *             annotation or the value already exists for this label.
     */
    public int addDropDownAnnotationValue(String label, String value)
            throws SQLException, IOException {
        return annoMethods.addDropDownAnnotationValue(label, value);
    }

    /**
     * Method to remove a given annotation of a dropdown- annotation.
     *
     * @param String
     *            label - the label of the chosen annotation
     * @param String
     *            value - the value of the chosen annotation.
     * @return Integer - how many values that were deleted.
     * @throws SQLException
     * @throws IOException
     *             - throws an IOException if the chosen value to be removed is
     *             the active DefaultValue of the chosen label.
     */
    public int removeDropDownAnnotationValue(String label, String value)
            throws SQLException, IOException {
        return annoMethods.removeDropDownAnnotationValue(label, value);
    }

    /**
     * Changes the annotation label.
     *
     * OBS! This changes the label for all experiments. Label Species can't be
     * changed because of dependencies in other tables.
     *
     * @param String
     *            oldLabel
     * @param String
     *            newLabel
     * @return int - the number of tuples updated
     * @throws SQLException
     *             If the update fails
     * @throws IOException
     *             if label = "Species" or the new label contains invalid
     *             characters.
     */
    public int changeAnnotationLabel(String oldLabel, String newLabel)
            throws SQLException, IOException {
        return annoMethods.changeAnnotationLabel(oldLabel, newLabel);
    }

    /**
     * Changes the value of an annotation corresponding to it's label.
     * Parameters: label of annotation, the old value and the new value to
     * change to.
     *
     * OBS! This method changes the value for every experiment.
     *
     * Throws an SQLException if the new value already exists in the choices
     * table (changing all males to female, and female is already in the table)
     *
     * @param String
     *            label - the label name.
     * @param String
     *            oldValue - the name of the old annotation value.
     * @param String
     *            newValue - the name of the new annotation value.
     * @throws SQLException
     *             If the databse could not be contacted.
     * @throws IOException
     *             If the label or old value does not exist, or if the new value
     *             is already a choice for this label. An IOException will also
     *             be thrown if the new value contains invalid characters. ie.
     *             '(', ')', '[' or ']'.
     */
    public void changeAnnotationValue(String label, String oldValue,
            String newValue) throws SQLException, IOException {
        annoMethods.changeAnnotationValue(label, oldValue, newValue);
    }

    /**
     * Method that changes the Required field to the selected boolean.
     *
     * @param AnnoLabel
     *            String, the name of the annotation to change required for.
     * @return resCount int, the numer of rows affected by the change.
     * @throws SQLException
     *             , will be thrown if the psql query fails.
     * @throws IOException
     *             If the label to change is invalid.
     */
    public int changeAnnotationRequiredField(String annoLabel, boolean required)
            throws SQLException, IOException {
        return annoMethods.changeAnnotationRequiredField(annoLabel, required);
    }

    /**
     * Gets all the choices for a drop down annotation.
     *
     * @param String
     *            label - the drop down annotation to get the choice for.
     * @return List<String> - the choices for one annotation label.
     * @throws SQLException
     *             - if the query does not succeed
     */
    public List<String> getChoices(String label) throws SQLException {
        return annoMethods.getChoices(label);
    }

    /**
     * @param String
     *            expID - The unique name of the experiment. OBS! If not null,
     *            this must reference an experiment that has been previously
     *            added.
     * @param int fileType - An Integer identifying the file type eg.
     *        FileTuple.RAW
     * @param String
     *            fileName
     * @param String
     *            inputFileName - The name of the corresponding input file or
     *            null if there is no corresponding input file
     * @param String
     *            metaData - The parameters used in file creation or null if not
     *            applicable
     * @param String
     *            author
     * @param String
     *            uploader
     * @param boolean isPrivate
     * @param String
     *            genomeRelease - The genome release version identifyer (eg.
     *            "hg38") or null if not applicable. OBS! If not null, this must
     *            reference a genome release that has been previously uploaded.
     * @return FileTuple - The FileTuple inserted in the database or null if no
     *         file was entered into the database.
     * @throws SQLException
     *             - If the query could not be executed. Possible reasons:
     *             Duplicate file, Does not reference a valid GenomeRelease.
     * @throws IOException
     *             If the experiment does not exist, the genomeReelase does not
     *             exist or the file name is already in use.
     */
    public FileTuple addNewFile(String expID, int fileType, String fileName,
            String inputFileName, String metaData, String author,
            String uploader, boolean isPrivate, String genomeRelease)
            throws SQLException, IOException {
        return fileMethods.addNewFile(expID, fileType, fileName, inputFileName,
                metaData, author, uploader, isPrivate, genomeRelease);
    }

    /**
     * Sets the status of a file to "Done".
     * NOT USED IN 2014!
     *
     * @param fileID
     *            the ID of the file to set to "Done".
     * @return the number of tuples updated.
     * @throws SQLException
     */
    public int fileReadyForDownload(int fileID) throws SQLException {
        return fileMethods.fileReadyForDownload(fileID);
    }

    /**
     * Returns the FileTuple object associated with the given filePath.
     *
     * @param String
     *            filePath
     * @return FileTuple - The corresponding FileTuple or null if no such file
     *         exists
     * @throws SQLException
     *             - If the query could not be executed.
     */
    public FileTuple getFileTuple(String filePath) throws SQLException {
        return fileMethods.getFileTuple(filePath);
    }

    /**
     * Returns the FileTuple object associated with the given fileID.
     *
     * @param fileID
     *            int
     * @return FileTuple - The corresponding FileTuple or null if no such file
     *         exists
     * @throws SQLException
     *             If the query could not be executed.
     */
    public FileTuple getFileTuple(int fileID) throws SQLException {
        return fileMethods.getFileTuple(fileID);
    }

    /**
     * Deletes a file from the database.
     *
     * @param String
     *            path - the path to the file.
     * @return int - the number of deleted tuples in the database.
     * @throws SQLException
     *             - if the query does not succeed
     * @throws IOException
     */
    public int deleteFile(String path) throws SQLException, IOException {
        return fileMethods.deleteFile(path);
    }

    /**
     * Deletes a file from the database using the fileID.
     *
     * @param int fileID - the fileID of the file to be deleted.
     * @return int - 1 if deletion was successful, else 0.
     * @throws SQLException
     * @throws IOException
     */
    public int deleteFile(int fileID) throws SQLException, IOException {
        return fileMethods.deleteFile(fileID);
    }

    /**
     * Checks if the file with the specified fileID exists in the database.
     *
     * @param int fileID - the fileID of the file.
     * @return boolean - true if the file exists, else false.
     * @throws SQLException
     */
    public boolean hasFile(int fileID) throws SQLException {
        return fileMethods.hasFile(fileID);
    }

    /**
     * Changes the Filename for a specific file with given fileID. This method
     * affects both the saved file name, but also the entries path and fileName
     * in database.
     *
     * @return resCount int, the number of rows affected by the change.
     * @throws SQLException
     *             if failed to send query,
     * @throws IOException
     *             if the chosen new file name already exist as a stored file.
     */
    public int changeFileName(int fileID, String newFileName)
            throws SQLException, IOException {
        return fileMethods.changeFileName(fileID, newFileName);
    }

    /**
     * Adds a drop down annotation to the list of possible annotations.
     *
     * @param String
     *            label - the name of the annotation
     * @param List
     *            <String> - the possible values for the annotation
     * @return int - the number of tuples inserted into the database
     * @throws SQLException
     *             - if the query does not succeed
     * @throws IOException
     *             - if the label is an existing fileannotation or contains
     *             invalid characters. Also if one or more of the values
     *             contains invalid characters.
     */
    public int addDropDownAnnotation(String label, List<String> choices,
            int defaultValueIndex, boolean required) throws SQLException,
            IOException {
        return annoMethods.addDropDownAnnotation(label, choices,
                defaultValueIndex, required);
    }

    /**
     * Generates a folder where the profile files for a certain experiment
     * should be stored.
     *
     * OBS! The files are not be added to the database at this point, and will
     * therefore not be searchable the users. Upon successful processing the
     * addGeneratedProfiles(...) method must be executed to add the files to the
     * database.
     *
     * @param String
     *            expId - The ID name of the experiment
     * @return String - The path to the folder or null if there are no raw files
     *         for this experiment.
     * @throws IOException
     * @throws SQLException
     *             - If the database could not be accessed
     * @throws ParseException
     */
    public Entry<String, String> processRawToProfile(String expId)
            throws IOException, SQLException {
        Experiment e = expMethods.getExperiment(expId);
        if (e == null) {
            throw new IOException("Invalid experiment ID");
        }
        if (e.getFiles() == null || e.getFiles().isEmpty()) {
            throw new IOException("There are no raw files to process!");
        }
        List<FileTuple> fileTuples = e.getFiles();
        FileTuple rawFileTuple = getRawFileTuple(fileTuples);
        if (rawFileTuple == null) {
            throw new IOException(expId + " has no raw files to process!");
        }
        String profileFolderPath = fpg.generateNewProfileSubFolder(fpg
                .getProfileFolderPath(e.getID()));
        return new SimpleEntry<String, String>(rawFileTuple.getParentFolder(),
                profileFolderPath);
    }

    /**
     * Adds all the files in the specified folder to the database's File table.
     * They will all be treated as profile files.
     *
     * @param String
     *            expId - The ID name of the experiment
     * @param String
     *            folderPath - The path to the folder containing the profile
     *            files. (This should be exactly the same path as returned by
     *            the processRawToProfile(expId) method)
     * @param String
     *            inputFileName - The name of the input file or null if no input
     *            file was generated
     * @param String
     *            metaData - A String specifying the parameters used for
     *            processing the raw file.
     * @param String
     *            genomeReleaseVersion - The genome release version used in
     *            processing. OBS! this is a reference to a genome release
     *            stored in the database/on the server and must therefore be
     *            valid.
     * @param String
     *            uploader - The user that commissioned the processing
     * @param boolean isPrivate - True if the files are to be private to the
     *        uploader, otherwise false.
     * @throws SQLException
     *             - If the request uses invalid arguments or the database could
     *             not be reached. Possible reasons: invalid genomeRelease.
     * @throws IOException
     */
    public void addGeneratedProfiles(String expId, String folderPath,
            String inputFileName, String metaData, String grVersion,
            String uploader, boolean isPrivate) throws SQLException,
            IOException {

        Experiment e = expMethods.getExperiment(expId);

        if (e == null) {
            throw new IOException(expId + " does not exist.");
        }

        Genome g = genMethods.getGenomeRelease(grVersion);
        if (g == null) {
            throw new IOException("Invalid Genome Release! " + grVersion
                    + " does not exist");
        }

        File profileFolder = new File(folderPath);

        if (!profileFolder.exists()) {
            throw new IOException("There are no profiles in this folder!");
        }

        for (File f : profileFolder.listFiles()) {
            if (!f.getName().equals(inputFileName)) {
                fileMethods.addGeneratedFile(e.getID(), FileTuple.PROFILE,
                        f.getPath(), inputFileName, metaData, uploader,
                        isPrivate, grVersion);
            }
        }
    }

    /**
     * Not used in Genomizer 2014
     *
     * @param folderPath
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public int removeIncompleteProfileProcess(String folderPath)
            throws SQLException, IOException {
        File profileFolder = new File(folderPath);
        if (profileFolder.exists()) {
            recursiveDelete(profileFolder);
        }
        String filePath = folderPath + "processing...";
        FileTuple pft = getFileFromPath(filePath);
        return fileMethods.deleteFile(pft.id);
    }

    /**
     * Not used in 2014.
     *
     * @param filePath
     * @return
     * @throws SQLException
     * @throws IOException
     */
    private FileTuple getFileFromPath(String filePath) throws SQLException,
            IOException {
        String query = "SELECT * FROM File WHERE Path = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, filePath);
        ResultSet rs = ps.executeQuery();
        FileTuple pft;
        if (rs.next()) {
            pft = new FileTuple(rs);
            ps.close();
            return pft;
        }
        ps.close();
        throw new IOException("There is no database entry for this path");
    }

    /**
     * Gets the file path to a stored Genome Release
     *
     * @param String
     *            genomeVersion, The version to get filepath to, should use
     *            getAllGenomeReleases() and let user choose a version
     * @return Genome - a genome object
     * @throws SQLException
     */
    public Genome getGenomeRelease(String genomeVersion) throws SQLException {
        return genMethods.getGenomeRelease(genomeVersion);
    }

    /**
     * Add one genome release to the database.
     *
     * @param String
     *            genomeVersion
     * @param String
     *            species
     * @return String - The path to the folder where the genome release files
     *         should be saved.
     * @throws SQLException
     *             - if adding query failed.
     * @throws IOException
     */
    public String addGenomeRelease(String genomeVersion, String species,
            String filename) throws SQLException, IOException {
        return genMethods.addGenomeRelease(genomeVersion, species, filename);
    }

    /**
     * Sets the status for a genome release file to "Done".
     *
     * @param version
     *            the file version.
     * @param fileName
     *            the file name.
     * @return the number of tuples updated.
     * @throws SQLException
     */
    public int genomeReleaseFileUploaded(String version, String fileName)
            throws SQLException {
        return genMethods.fileReadyForDownload(version, fileName);
    }

    /**
     * Removes one specific genome version stored in the database.
     *
     * @param String
     *            version - the genome version.
     * @param String
     *            species
     *
     * @return boolean - true if succeeded, false if failed.
     * @throws SQLException
     * @throws IOException
     */
    public boolean removeGenomeRelease(String genomeVersion)
            throws SQLException, IOException {
        return genMethods.removeGenomeRelease(genomeVersion);
    }

    /**
     * Method for getting all the genome releases for a species currently stored
     * in the database.
     *
     * @param species
     *            String, the name of the species you want to get genome
     *            realeases for.
     * @return genomelist ArrayList<Genome>, list of all the genome releases for
     *         a specific species. Returns NULL if the specified specie did NOT
     *         have a genomeRelase entry in the database.
     * @throws SQLException
     */
    public ArrayList<Genome> getAllGenomReleasesForSpecies(String species)
            throws SQLException {
        return genMethods.getAllGenomReleasesForSpecies(species);
    }

    private FileTuple getRawFileTuple(List<FileTuple> fileTuples) {
        for (FileTuple ft : fileTuples) {
            if (ft.type.equalsIgnoreCase("raw")) {
                return ft;
            }
        }
        return null;
    }

    /**
     * Returns a list of all genome releases in the database.
     *
     * @return List<Genome> - A list of genomes, if no genomes are found the
     *         list is empty
     * @throws SQLException
     *             - if the query does not succeed
     */
    public List<Genome> getAllGenomReleases() throws SQLException {
        return genMethods.getAllGenomReleases();
    }

    /**
     * Returns a list of all genome releases in the database for a specie.
     *
     * @return List<Genome> - A list of genomes, if no genomes are found the
     *         list is empty
     * @throws SQLException
     *             - if the query does not succeed
     */
    public List<String> getAllGenomReleaseSpecies() throws SQLException {
        return genMethods.getAllGenomReleaseSpecies();
    }

    /**
     * Get a specific chain file depending on from and to what genome release
     * you want to convert between.
     *
     * @param String
     *            fromVersion - the name of the old genome release version
     * @param String
     *            toVersion - the name of the new genome release version
     * @return a ChainFile object containing all information about the chain
     *         file.
     * @throws SQLException
     */
    public ChainFile getChainFile(String fromVersion, String toVersion)
            throws SQLException {
        return genMethods.getChainFile(fromVersion, toVersion);
    }

    /**
     * Adds a chain file to database for conversions. Parameters: Oldversion,
     * new version and filename. Returns: upload URL
     *
     * @param String
     *            fromVersion
     * @param String
     *            toVersion
     * @param String
     *            fileName
     * @return String upload URL
     * @throws SQLException
     */
    public String addChainFile(String fromVersion, String toVersion,
            String fileName) throws SQLException {
        return genMethods.addChainFile(fromVersion, toVersion, fileName);
    }

    /**
     * Deletes a chain_file from the database. You find the unique file by
     * sending in the genome version the file converts from and the genome
     * version the file converts to.
     *
     * @param String
     *            fromVersion - genome version the Chain_file converts from
     * @param String
     *            toVersion - genome version the Chin_file converts to
     * @return int - the number of deleted tuples in the database. (Should be
     *         one if success)
     * @throws SQLException
     *             - if the query does not succeed
     */
    public int removeChainFile(String fromVersion, String toVersion)
            throws SQLException {
        return genMethods.removeChainFile(fromVersion, toVersion);
    }

    /**
     * @param String
     *            pubMedString
     * @return List<Experiment>
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    private List<Experiment> searchExperiments(String pubMedString)
            throws IOException, SQLException {

        String query = pm2sql.convertExperimentSearch(pubMedString);

        List<Entry<String, String>> params = pm2sql.getParameters();

        PreparedStatement getExperiments = conn.prepareStatement(query);

        getExperiments = annoMethods.bind(getExperiments, params);

        ResultSet rs = getExperiments.executeQuery();
        ArrayList<Experiment> experiments = new ArrayList<Experiment>();
        while (rs.next()) {
            Experiment exp = new Experiment(rs.getString("ExpID"));
            exp = expMethods.fillAnnotations(exp);
            exp = expMethods.fillFiles(exp);
            experiments.add(exp);
        }
        return experiments;
    }

    /**
     * @param String
     *            pubMedString
     * @return List<Experiment>
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    private List<Experiment> searchFiles(String pubMedString)
            throws IOException, SQLException {

        String query = pm2sql.convertFileSearch(pubMedString);

        List<Entry<String, String>> params = pm2sql.getParameters();

        PreparedStatement getFiles = conn.prepareStatement(query);

        getFiles = annoMethods.bind(getFiles, params);
        ResultSet rs = getFiles.executeQuery();

        ArrayList<Experiment> experiments = new ArrayList<Experiment>();
        if (!rs.next()) {
            return experiments;
        }
        String expId = rs.getString("ExpId");
        Experiment exp = new Experiment(expId);
        exp = expMethods.fillAnnotations(exp);
        exp.addFile(new FileTuple(rs));
        while (rs.next()) {
            expId = rs.getString("ExpId");
            if (exp.getID().equals(expId)) {
                exp.addFile(new FileTuple(rs));
            } else {
                experiments.add(exp);
                exp = new Experiment(expId);
                exp = expMethods.fillAnnotations(exp);
                exp.addFile(new FileTuple(rs));
            }
        }
        experiments.add(exp);
        return experiments;
    }

    /**
     * Get's the filePathGenerator object.
     *
     * @return FilePathGenerator
     */
    public FilePathGenerator getFilePathGenerator() {
        return fpg;
    }

    /**
     * Recursively deletes a folder with all it's subfolders and files.
     *
     * @param folder
     *            the folder to delete.
     */
    public void recursiveDelete(File folder) {
        File[] contents = folder.listFiles();
        if (contents == null || contents.length == 0) {
            folder.delete();
        } else {
            for (File f : contents) {
                recursiveDelete(f);
            }
        }
        folder.delete();
    }
}
