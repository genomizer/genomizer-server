package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import database.subClasses.*;

/**
 * PREREQUISITES: The construction parameters must reference a postgresql
 * database with the genomizer database tables preloaded. This is done by
 * running the genomizer_database_tables.sql.
 *
 * DatabaseAccessor manipulates the underlying postgresql database using SQL
 * commands.
 *
 * Developed by the Datastorage group for the Genomizer Project, Software
 * Engineering course at Umeå University 2014.
 *
 * @author dv12rwt, Ruaridh Watt
 * @author dv12kko, Kenny Kunto
 * @author dv12ann, André Niklasson
 * @author dv12can, Carl Alexandersson
 * @author yhi04jeo, Jonas Engbo
 * @author oi11mhn, Mattias Hinnerson
 *
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
	 * @param username
	 *            - The username to log in to the database as. Should be
	 *            "c5dv151_vt14" as of now.
	 * @param password
	 *            - The password to log in to the database. Should be "shielohh"
	 *            as of now.
	 * @param host
	 *            - The name of the database management system. Will problebly
	 *            always be "postgres" unless the DMS is switched with something
	 *            else.
	 * @param database
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
		fileMethods = new FileMethods(conn, fpg);
		genMethods = new GenomeMethods(conn, fpg);
	}

	public DatabaseAccessor() {

	}

	/**
	 * Closes the connection to the database, releasing all resources it uses.
	 * @throws SQLException
	 *
	 */
	public void close() throws SQLException {

		conn.close();

	}

	/**
	 * Public method to check if the instance of the class is connected to a
	 * database.
	 *
	 * @return boolean, true if it is connected, otherwise false.
	 */
	public boolean isConnected() {
		return conn != null;
	}

	/**
	 * Searches the database for Experiments. The search criteria are specified
	 * in a String that has the same format as that used by PubMed:
	 *
	 * <Value>[<Label>] <AND|OR> <Value>[<Label>] ...
	 *
	 * Round brackets should be used to disambiguate the logical expression.
	 *
	 * Example: "(Human[Species] OR Fly[Species]) AND Joe Bloggs[Uploader]"
	 *
	 * @param pubMedString
	 *            The String containing the search criteria in PubMed format.
	 * @return A List of experiments containing file that fullfill the criteria
	 *         specifies in the pubMedString.
	 * @throws IOException
	 *             If the pubMedString is not in the right format
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public List<Experiment> search(String pubMedString) throws IOException,
			SQLException {

		isPubMedStringValid(pubMedString);

		if (pm2sql.hasFileConstraint(pubMedString)) {
			return searchFiles(pubMedString);
		}

		return searchExperiments(pubMedString);
	}

	/**
	 * Internal method!
	 *
	 * Checks that the pubmed string is valid.
	 *
	 * @param pubMedString
	 * @return true if ok else throws Exception
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
	 * @return an ArrayList of usernames.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public List<String> getUsers() throws SQLException {

		return userMethods.getUsers();
	}

	/**
	 * Method to add a new user to the database.
	 *
	 * @param String
	 *            the username
	 * @param String
	 *            the password
	 * @param String
	 *            the role given to the user ie. "Admin"
	 * @throws SQLException
	 */
	public void addUser(String username, String password, String role)
			throws SQLException {

		userMethods.addUser(username, password, role);
	}

	/**
	 * Deletes a user from the database.
	 *
	 * @param username
	 *            the username of the user to be deleted.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public void deleteUser(String username) throws SQLException {

		userMethods.deleteUser(username);

	}

	/**
	 * Returns the password for the given user. Used for login.
	 *
	 * @param user
	 *            - the username as string
	 * @return String - the password
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public String getPassword(String user) throws SQLException {

		return userMethods.getPassword(user);
	}

	/**
	 * Changes the password for a user.
	 *
	 * @param username
	 *            the user to change the password for.
	 * @param newPassword
	 *            the new password.
	 * @return the number of tuples updated in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public int resetPassword(String username, String newPassword)
			throws SQLException {

		return userMethods.resetPassword(username, newPassword);
	}

	/**
	 * Gets the role (permissions) for a user.
	 *
	 * @param username
	 *            the user to get the role for.
	 * @return the role as a string.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public String getRole(String username) throws SQLException {

		return userMethods.getRole(username);
	}

	/**
	 * Sets the role (permissions) for the user.
	 *
	 * @param username
	 *            the user to set the role for.
	 * @param role
	 *            the role to set for the user.
	 * @return returns the number of tuples updated in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public int setRole(String username, String role) throws SQLException {

		return userMethods.setRole(username, role);
	}

	/**
	 * Gets an experiment from the database.
	 *
	 * @param expID
	 *            the ID of the experiment.
	 * @return an Experiment object.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public Experiment getExperiment(String expID) throws SQLException {

		return expMethods.getExperiment(expID);
	}

	/**
	 * Adds an experiment ID to the database.
	 *
	 * @param expID
	 *            the ID for the experiment.
	 * @return the number of tuples inserted in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public int addExperiment(String expID) throws SQLException {

		return expMethods.addExperiment(expID);
	}

	/**
	 * Deletes an experiment from the database.
	 *
	 * @param expId
	 *            the experiment ID.
	 * @return the number of tuples deleted.
	 * @throws SQLException
	 *             if the query does not succeed. Occurs if Experiment contains
	 *             at least one file. (All files relating to an experiment must
	 *             be deleted first before an experiment can be deleted from the
	 *             database)
	 */
	public int deleteExperiment(String expId) throws SQLException {

		return expMethods.deleteExperiment(expId);
	}

	/**
	 * Checks if a given experiment ID exists in the database.
	 *
	 * @param expID
	 *            the experiment ID to look for.
	 * @return true if the experiment exists in the database, else false.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public boolean hasExperiment(String expID) throws SQLException {

		return expMethods.hasExperiment(expID);
	}

	/**
	 * Updates a value of a single annotation of a unique experiment
	 *
	 * @param expID
	 *            the name of the experiment to annotate.
	 * @param label
	 *            the annotation to set.
	 * @param value
	 *            the value of the annotation.
	 * @return the number of tuples updated in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 * @throws IOException
	 *             if the value is invalid for the annotation type.
	 */
	public int updateExperiment(String expID, String label, String value)
			throws SQLException, IOException {

		return expMethods.updateExperiment(expID, label, value);
	}

	/**
	 * Annotates an experiment with the given label and value. Checks so that
	 * the value is valid if it is a drop down annotation.
	 *
	 * @param expID
	 *            the name of the experiment to annotate.
	 * @param label
	 *            the annotation to set.
	 * @param value
	 *            the value of the annotation.
	 * @return the number of tuples updated in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 * @throws IOException
	 *             if the value is invalid for the annotation type.
	 */
	public int annotateExperiment(String expID, String label, String value)
			throws SQLException, IOException {

		return expMethods.annotateExperiment(expID, label, value);
	}

	/**
	 * Deletes one annotation from a specific experiment.
	 *
	 * @param expID
	 *            the experiment to delete the annotation from.
	 * @param label
	 *            the name of the annotation.
	 * @return the number of tuples deleted from the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public int removeExperimentAnnotation(String expID, String label)
			throws SQLException {

		return expMethods.removeExperimentAnnotation(expID, label);
	}

	/**
	 * Gets all the annotation possibilities from the database.
	 *
	 * @return annotations Map<String, Integer> - a Map with the label string as
	 *         key and datatype as value.
	 *
	 *         The possible datatypes are FREETEXT and DROPDOWN.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public Map<String, Integer> getAnnotations() throws SQLException {

		return annoMethods.getAnnotations();
	}

	/**
	 * Creates an Annotation object from an annotation label.
	 *
	 * @param label
	 *            String - the name of the annotation to create the object for.
	 * @return Annotation - the Annotation object. If the label does not exist,
	 *         then null will be returned.
	 * @throws SQLException
	 *             if the query does not succeed.
	 */
	public Annotation getAnnotationObject(String label) throws SQLException {

		return annoMethods.getAnnotationObject(label);
	}

	/**
	 * Creates a list of Annotation objects from a list of annotation labels.
	 *
	 * @param labels
	 *            the list of labels.
	 * @return annotations List<Annotation> - will return a list with all the
	 *         annotations with valid labels. If the list with labels is empty
	 *         or none of the labels are valid, then it will return null.
	 * @throws SQLException
	 *             if the query does not succeed.
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
	 * @return annotationLabels ArrayList<String>
	 */
	public ArrayList<String> getAllAnnotationLabels() {

		return annoMethods.getAllAnnotationLabels();
	}

	/**
	 * Gets the datatype of a given annotation.
	 *
	 * @param label
	 *            annotation label.
	 * @return integer - the annotation's datatype (FREETEXT or DROPDOWN).
	 *
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public Integer getAnnotationType(String label) throws SQLException {

		return annoMethods.getAnnotationType(label);
	}

	/**
	 * Gets the default value for a annotation if there is one, If not it
	 * returns NULL.
	 *
	 * @param annotationLabel
	 *            String - the name of the annotation to check
	 * @return DefaultValue String - The defult value or NULL.
	 * @throws SQLException
	 */
	public String getDefaultAnnotationValue(String annotationLabel)
			throws SQLException {

		return annoMethods.getDefaultAnnotationValue(annotationLabel);
	}

	/**
	 * Deletes an annotation from the list of possible annotations.
	 *
	 * @param label
	 *            String - the label of the annotation to delete.
	 * @return res integer - the number of tuples deleted in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public int deleteAnnotation(String label) throws SQLException {

		return annoMethods.deleteAnnotation(label);
	}

	/**
	 * Adds a free text annotation to the list of possible annotations.
	 *
	 * @param label
	 *            String the name of the annotation.
	 * @param required
	 *            boolean if the annotation should be forced or not
	 * @param defaultValue
	 *            String the default value this field should take or null if a
	 *            default value is not required
	 * @return res int - the number of tuples updated in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 * @throws IOException
	 */
	public int addFreeTextAnnotation(String label, String defaultValue,
			boolean required) throws SQLException, IOException {

		return annoMethods.addFreeTextAnnotation(label, defaultValue, required);
	}

	/**
	 * Checks if a given annotation is required to be filled by the user.
	 *
	 * @param annotationLabel
	 *            String - the name of the annotation to check
	 * @return boolean - true if it is required, else false
	 * @throws SQLException
	 */
	public boolean isAnnotationRequiered(String annotationLabel)
			throws SQLException {

		return annoMethods.isAnnotationRequiered(annotationLabel);
	}

	/**
	 * Gets all the choices for a drop down annotation. Deprecated, use
	 * {@link #getChoices(String) getChoices} instead.
	 *
	 * @param label
	 *            String the drop down annotation to get the choice for.
	 * @return theChoices ArrayList<String> - all the choices.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	@Deprecated
	public ArrayList<String> getDropDownAnnotations(String label)
			throws SQLException {

		return annoMethods.getDropDownAnnotations(label);
	}

	/**
	 * Adds a drop down annotation to the list of possible annotations.
	 *
	 * @param label
	 *            String - the name of the annotation.
	 * @param choices
	 *            List<String> - the possible values for the annotation.
	 * @return tuplesInserted int - the number of tuples inserted into the
	 *         database.
	 * @throws SQLException
	 *             if the query does not succeed
	 * @throws IOException
	 *             if the choices are invalid
	 */
	public int addDropDownAnnotation(String label, List<String> choices,
			int defaultValueIndex, boolean required) throws SQLException,
			IOException {

		return annoMethods.addDropDownAnnotation(label, choices,
				defaultValueIndex, required);
	}

	/**
	 * Method to add a value to a existing DropDown annotation.
	 *
	 * @param label
	 *            String , the label of the chosen DropDown annotation.
	 * @param value
	 *            String , the value that will be added to the DropDown
	 *            annotation.
	 * @return Integer, how many rows that were added to the database.
	 * @throws SQLException
	 *             , if the value already exist or another SQL error.
	 * @throws IOException
	 *             , if the chosen label does not represent a DropDown
	 *             annotation.
	 */
	public int addDropDownAnnotationValue(String label, String value)
			throws SQLException, IOException {

		return annoMethods.addDropDownAnnotationValue(label, value);
	}

	/**
	 * Method to remove a given annotation of a dropdown- annotation.
	 *
	 * @param label
	 *            String - the label of the chosen annotation
	 * @param value
	 *            String - the value of the chosen annotation.
	 * @return Integer, how many values that were deleted.
	 * @throws SQLException
	 * @throws IOException
	 *             , throws an IOException if the chosen value to be removed is
	 *             the active DefaultValue of the chosen label.
	 */
	public int removeAnnotationValue(String label, String value)
			throws SQLException, IOException {

		return annoMethods.removeAnnotationValue(label, value);
	}

	/**
	 * Changes the annotation label.
	 *
	 * OBS! This changes the label for all experiments.
	 *
	 * @param oldLabel
	 *            String
	 * @param newLabel
	 *            string
	 *
	 * @return res int - the number of tuples updated
	 * @throws SQLException
	 *             If the update fails
	 * @throws IOException
	 */
	public int changeAnnotationLabel(String oldLabel, String newLabel)
			throws SQLException, IOException {

		return annoMethods.changeAnnotationLabel(oldLabel, newLabel);
	}

	/**
	 * Changes the value of an annotation corresponding to it's label.
	 *
	 * Parameters: label of annotation, the old value and the new value to
	 * change to.
	 *
	 * OBS! This method changes the value for every experiment.
	 *
	 * Throws an SQLException if the new value already exists in the choices
	 * table (changing all males to female, and female is already in the table)
	 *
	 * @param label
	 *            String - the label name.
	 * @param oldValue
	 *            String - the name of the old annotation value.
	 * @param newValue
	 *            String - the name of the new annotation value.
	 *
	 * @throws SQLException
	 * @throws IOException
	 */
	public void changeAnnotationValue(String label, String oldValue,
			String newValue) throws SQLException, IOException {

		annoMethods.changeAnnotationValue(label, oldValue, newValue);
	}

	/**
	 * Gets all the choices for a drop down annotation.
	 *
	 * @param label
	 *            String - the drop down annotation to get the choice for.
	 * @return choices List<String> - the choices for one annotation label.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public List<String> getChoices(String label) throws SQLException {

		return annoMethods.getChoices(label);
	}

	/**
	 * @param expID
	 *            String The unique name of the experiment. OBS! If not null,
	 *            this must reference an experiment that has been previously
	 *            added.
	 * @param fileType
	 *            int An Integer identifying the file type eg. FileTuple.RAW
	 * @param fileName
	 *            String
	 * @param inputFileName
	 *            String The name of the corresponding input file or null if
	 *            there is no corresponding input file
	 * @param metaData
	 *            String The parameters used in file creation or null if not
	 *            applicable
	 * @param author
	 *            String
	 * @param uploader
	 *            String
	 * @param isPrivate
	 *            boolean
	 * @param genomeRelease
	 *            String The genome release version identifyer (eg. "hg38") or
	 *            null if not applicable. OBS! If not null, this must reference
	 *            a genome release that has been previously uploaded.
	 * @return FileTuple - The FileTuple inserted in the database or null if no
	 *         file was entered into the database.
	 * @throws SQLException
	 *             If the query could not be executed. (Probably because the
	 *             file already exists)
	 */
	public FileTuple addNewFile(String expID, int fileType, String fileName,
			String inputFileName, String metaData, String author,
			String uploader, boolean isPrivate, String genomeRelease)
			throws SQLException {

		return fileMethods.addNewFile(expID, fileType, fileName, inputFileName,
				metaData, author, uploader, isPrivate, genomeRelease);
	}

	/**
	 * Returns the FileTuple object associated with the given filePath.
	 *
	 * @param filePath
	 *            String
	 * @return FileTuple - The corresponding FileTuple or null if no such file
	 *         exists
	 * @throws SQLException
	 *             If the query could not be executed.
	 */
	public FileTuple getFileTuple(String filePath) throws SQLException {

		return fileMethods.getFileTuple(filePath);
	}

	// Too many parameters. Should take a JSONObject or FileTuple
	// instead.
	/**
	 * Adds a file to the database. Users should migrate to serverAddFile(...)
	 * which returns the FileTuple added to the database.
	 *
	 * @param fileType
	 * @param fileName
	 * @param metaData
	 * @param author
	 * @param uploader
	 * @param isPrivate
	 * @param expID
	 * @param grVersion
	 * @return the number if tuples inserted to the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	@Deprecated
	public String addFile(String fileType, String fileName, String metaData,
			String author, String uploader, boolean isPrivate, String expID,
			String grVersion) throws SQLException {

		return fileMethods.addFile(fileType, fileName, metaData, author,
				uploader, isPrivate, expID, grVersion);
	}

	/**
	 * Adds a file to the database with URL. Use clientAddFile(...)
	 *
	 * @param fileType
	 * @param fileName
	 * @param metaData
	 * @param author
	 * @param uploader
	 * @param isPrivate
	 * @param expID
	 * @param grVersion
	 * @return the number if tuples inserted to the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	@Deprecated
	public String addFileURL(String fileType, String fileName, String metaData,
			String author, String uploader, boolean isPrivate, String expID,
			String grVersion) throws SQLException {

		return fileMethods.addFileURL(fileType, fileName, metaData, author,
				uploader, isPrivate, expID, grVersion);
	}

	/**
	 * Deletes a file from the database.
	 *
	 * @param path
	 *            String - the path to the file.
	 * @return int - the number of deleted tuples in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public int deleteFile(String path) throws SQLException {

		return fileMethods.deleteFile(path);
	}

	/**
	 * Deletes a file from the database using the fileID.
	 *
	 * @param fileID
	 *            int - the fileID of the file to be deleted.
	 * @return 1 if deletion was successful, else 0.
	 * @throws SQLException
	 */
	public int deleteFile(int fileID) throws SQLException {

		return fileMethods.deleteFile(fileID);
	}

	/**
	 * Checks if the file with the specified fileID exists in the database.
	 *
	 * @param fileID
	 *            int - the fileID of the file.
	 * @return true if the file exists, else false.
	 * @throws SQLException
	 */
	public boolean hasFile(int fileID) throws SQLException {

		return fileMethods.hasFile(fileID);
	}

	/**
	 * Checks if the file path is a valid file path. Not used.
	 *
	 * @param filePath
	 * @return
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	@Deprecated
	public boolean isValidFilePath(String filePath) throws SQLException {

		return fileMethods.isValidFilePath(filePath);
	}

	/**
	 * Deprecated: Use ProcessRawToProfile(...)
	 *
	 * Method to convert from raw data to profile data. Returns a list of
	 * filepaths
	 *
	 * @param fileID
	 * @param fileType
	 * @param fileName
	 * @param metaData
	 * @param uploader
	 * @param grVersion
	 * @param expID
	 * @return ArrayList<String>
	 * @throws SQLException
	 */
	@Deprecated
	public ArrayList<String> process(String fileID, String fileType,
			String fileName, String metaData, String uploader,
			String grVersion, String expID) throws SQLException {

		return fileMethods.process(fileID, fileType, fileName, metaData,
				uploader, grVersion, expID);
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
	 * @param expId
	 *            The ID name of paththe experiment
	 * @return The path to the folder or null if there are no raw files for this
	 *         experiment.
	 * @throws SQLException
	 *             If the database could not be accessed
	 */
	public Entry<String, String> processRawToProfile(String expId)
			throws SQLException {

		List<Experiment> experiments;
		try {
			experiments = search(expId + "[ExpID] AND Raw[FileType]");
		} catch (IOException e) {
			System.err.println("Search query failed!");
			return null;
		}

		if (experiments.isEmpty()) {
			System.err.println("There are no raw files to process!");
			return null;
		}

		Experiment e = experiments.get(0);

		if (e.getFiles().isEmpty()) {
			System.err.println("There are no raw files to process!");
			return null;
		}

		FileTuple ft = e.getFiles().get(0);

		String profileFolder = fpg.generateProfileFolder(expId);

		return new SimpleEntry<String, String>(ft.getParentFolder(),
				profileFolder);
	}

	/**
	 * <<<<<<< HEAD Adds all the files in the specified folder to the database's
	 * File table. They will all be treated as profile files. ======= Adds all
	 * the files in the specified folder to the database's File table. They will
	 * all be treated as profile files. >>>>>>>
	 * 788f07a10737996a72576dbd675418e8313f65f3
	 *
	 * @param expId
	 *            The ID name of the experiment
	 * @param folderPath
	 *            The path to the folder containing the profile files. (This
	 *            should be exactly the same path as returned by the
	 *            processRawToProfile(expId) method).
	 * @param inputFileName
	 *            The name of the input file or null if no input file was
	 *            generated.
	 * @param metaData
	 *            A String specifying the parameters used for processing the raw
	 *            file.
	 * @param genomeReleaseVersion
	 *            The genome release version used in processing. OBS! this is a
	 *            reference to a genome release stored in the database/on the
	 *            server and must therefore be valid.
	 * @param uploader
	 *            The user that commissioned the processing.
	 * @param isPrivate
	 *            True if the files are to be private to the uploader, otherwise
	 *            false.
	 * @throws SQLException
	 *             If the request uses invalid arguments or the database could
	 *             not be reached.
	 */
	public void addGeneratedProfiles(String expId, String folderPath,
			String inputFileName, String metaData, String genomeReleaseVersion,
			String uploader, boolean isPrivate) throws SQLException {

		File profileFolder = new File(folderPath);

		for (File f : profileFolder.listFiles()) {

			if (!f.getName().equals(inputFileName)) {
				addGeneratedProfile(expId, f.getPath(), folderPath
						+ inputFileName, metaData, genomeReleaseVersion,
						uploader, isPrivate);
			}
		}
	}

	private void addGeneratedProfile(String expId, String path,
			String inputFilePath, String metaData, String genomeReleaseVersion,
			String uploader, boolean isPrivate) throws SQLException {

		String query = "INSERT INTO File "
				+ "(Path, FileType, FileName, Date, MetaData, InputFilePath, "
				+ "Author, Uploader, IsPrivate, ExpID, GRVersion) "
				+ "VALUES (?, 'Profile', ?, CURRENT_TIMESTAMP, ?, ?, 'Genomizer', ?, ?, ?, ?)";

		PreparedStatement addFile = conn.prepareStatement(query);

		addFile.setString(1, path);
		addFile.setString(2, getFileName(path));
		addFile.setString(3, metaData);
		addFile.setString(4, inputFilePath);
		addFile.setString(5, uploader);
		addFile.setBoolean(6, isPrivate);
		addFile.setString(7, expId);
		addFile.setString(8, genomeReleaseVersion);

		addFile.executeUpdate();
		addFile.close();
	}

	private String getFileName(String path) {
		int lastFileSeperatorIndex = path.lastIndexOf(File.separator);
		return path.substring(lastFileSeperatorIndex + 1);
	}

	/**
	 * Gets the file path to a stored Genome Release
	 *
	 * @param genomeVersion
	 *            - The version to get filepath to, should use
	 *            getAllGenomeReleases() and let user choose a version
	 * @return Genome - a genome object
	 * @throws SQLException
	 */

	public Genome getGenomeRelease(String genomeVersion) throws SQLException {

		return genMethods.getGenomeRelease(genomeVersion);
	}

	/**
	 * Add one genomerelease to the database.
	 *
	 * @param String
	 *            genomeVersion.
	 * @param String
	 *            species.
	 * @return String The path to the folder where the genome release files
	 *         should be saved.
	 * @throws SQLException
	 *             if adding query failed.
	 */
	public String addGenomeRelease(String genomeVersion, String species,
			String filename) throws SQLException {

		return genMethods.addGenomeRelease(genomeVersion, species, filename);
	}

	/**
	 * Removes one specific genome version stored in the database.
	 *
	 * @param version
	 *            , the genome version.
	 * @param specie
	 *            .
	 * @return boolean, true if succeded, false if failed.
	 */
	public boolean removeGenomeRelease(String genomeVersion, String specie) {

		return genMethods.removeGenomeRelease(genomeVersion, specie);
	}

	/**
	 * method for getting all the genome releases for a species currently stored
	 * in the database.
	 *
	 * @param species
	 *            String, the name of the specie you want to get genome
	 *            realeases for.
	 * @return genomeVersions List<String>, list of all the genome releases for
	 *         a specific specie.
	 * @throws SQLException
	 */
	public ArrayList<Genome> getAllGenomReleasesForSpecies(String species)
			throws SQLException {

		return genMethods.getAllGenomReleasesForSpecies(species);
	}

	/**
	 * method for getting all the genome releases currently stored in the
	 * database.
	 *
	 * @return genomeList ArrayList<Genome>, list of all the genome releases.
	 * @throws SQLException
	 */
	public ArrayList<Genome> getAllGenomReleases() throws SQLException {

		return genMethods.getAllGenomReleases();
	}

	/**
	 * get a specific chainfile depending on from and to what genome release you
	 * want to convert between.
	 *
	 * @param fromVersion
	 *            String, the name of the old genome release version
	 * @param toVersion
	 *            String, the name of the new genome release version
	 * @return resFilePath String, the filePath of that chain file.
	 * @throws SQLException
	 */
	public String getChainFile(String fromVersion, String toVersion)
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
	 * @param fromVersion
	 *            - genome version the Chain_file converts from
	 * @param toVersion
	 *            - genome version the Chin_file converts to
	 * @return the number of deleted tuples in the database. (Should be one if
	 *         success)
	 * @throws SQLException
	 *             - if the query does not succeed
	 */
	public int removeChainFile(String fromVersion, String toVersion)
			throws SQLException {

		return genMethods.removeChainFile(fromVersion, toVersion);
	}

	/**
	 *
	 * @param pubMedString
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	private List<Experiment> searchExperiments(String pubMedString)
			throws IOException, SQLException {

		String query = pm2sql.convertExperimentSearch(pubMedString);
		List<String> params = pm2sql.getParameters();

		PreparedStatement getFiles = conn.prepareStatement(query);
		getFiles = annoMethods.bind(getFiles, params);

		ResultSet rs = getFiles.executeQuery();
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
	 *
	 * @param pubMedString
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	private List<Experiment> searchFiles(String pubMedString)
			throws IOException, SQLException {

		String query = pm2sql.convertFileSearch(pubMedString);
		List<String> params = pm2sql.getParameters();

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
	 *
	 * @return
	 */
	public FilePathGenerator getFilePathGenerator() {
		return fpg;
	}
}
