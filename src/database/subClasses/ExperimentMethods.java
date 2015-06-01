package database.subClasses;

import database.FilePathGenerator;
import database.containers.Annotation;
import database.containers.Experiment;
import database.containers.FileTuple;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class that contains all the methods for adding,changing, getting and removing
 * Experiments in the database. This class is a subClass of databaseAcessor.java
 *
 * date: 2014-05-14 version: 1.0
 */
public class ExperimentMethods {

    private Connection conn;
    private FilePathGenerator fpg;
    private AnnotationMethods annoMethods;

    public ExperimentMethods(Connection connection,
            FilePathGenerator filePG, AnnotationMethods annoM) {

        conn = connection;
        fpg = filePG;
        annoMethods = annoM;
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

        String query = "SELECT ExpID FROM Experiment "
                + "WHERE ExpID ~~* ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, expID);
        ResultSet rs = stmt.executeQuery();

        String expid = null;
        if (rs.next()) {
            expid = rs.getString("ExpID");
        }
        stmt.close();

        if (expid != null) {
            Experiment e = new Experiment(expid);
            e = fillAnnotations(e);
            e = fillFiles(e);
            return e;
        }

        return null;
    }

    /**
     * Adds an experiment ID to the database.
     *
     * @param expID
     *            the ID for the experiment.
     * @return the number of tuples inserted in the database.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     *             If the experiment already exists.
     */
    public int addExperiment(String expID) throws SQLException, IOException {
        if (expID == null || expID.isEmpty()) {
            throw new IOException("Invalid experiment ID.");
        }

        Experiment e = getExperiment(expID);
        if (e != null) {
            throw new IOException(expID + " already exists");
        }

        fpg.generateExperimentFolders(expID);
        String query = "INSERT INTO Experiment "
                + "(ExpID) VALUES (?)";
        int rs = 0;
        try (PreparedStatement stmt= conn.prepareStatement(query);) {
            stmt.setString(1, expID);
            rs = stmt.executeUpdate();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
        return rs;
    }

    /**
     * Deletes an experiment from the database.
     *
     * @param expId
     *            the experiment ID.
     * @return the number of tuples deleted.
     * @throws SQLException
     *             if the query does not succeed. Occurs if Experiment
     *             contains at least one file. (All files relating to
     *             an experiment must be deleted first before an
     *             experiment can be deleted from the database)
     * @throws IOException
     */
    public int deleteExperiment(String expId)
            throws SQLException, IOException {
        Experiment e = getExperiment(expId);
        if (e == null) {
            throw new IOException("No experiment with ID " + expId);
        }

        removeHiddenFilesFromExperiment(expId);
        if (!e.getFiles().isEmpty()) {
            throw new IOException("This experiment contains files and therefore cannot be removed.");
        }

        File experimentFolder = new File(fpg.getRootDirectory() + e.getID());
        boolean success = true;
        if (experimentFolder.exists()) {
            if (!recursiveDelete(experimentFolder)) success = false;
        }

        int rs = 0;
        if (success) {
            String query = "DELETE FROM Experiment "
                    + "WHERE ExpID ~~* ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, expId);
            rs = stmt.executeUpdate();
            stmt.close();
        }

        return rs;
    }

    /**
     * Help method to remove 'invisible' in progress marked files.
     * @param expId
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private int removeHiddenFilesFromExperiment(String expId)
            throws IOException, SQLException {

        Experiment e = getExperiment(expId);

        if (e == null) {
            throw new IOException("No experiment with ID " + expId);
        }

        String query = "DELETE FROM File "
                + "WHERE ExpID ~~* ? AND status='In Progress'";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, expId);
        int rs = stmt.executeUpdate();
        stmt.close();
        return rs;
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
     * @deprecated Use updateExperiment(String expID, HashMap<String, String>)
     *             instead
     */
    @Deprecated
    public int updateExperiment(String expID, String label,
            String value) throws SQLException, IOException {

        value = validateAnnotation(label, value);

        if (value == null) {
            throw new IOException("Invalid annotation");
        }

        String query = "UPDATE Annotated_With SET Value = ?"
                + " WHERE (Label ~~* ?) AND (ExpID ~~* ?)";

        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, value);
        stmt.setString(2, label);
        stmt.setString(3, expID);

        int rs = stmt.executeUpdate();
        stmt.close();
        return rs;
    }

    /**
     * Updates values of multiple annotations of a unique experiment.
     *
     * @param expID
     *            the name of the experiment to annotate.
     * @param annotations
     *            the list of annotations to set.
     *            Should consist of objects of type Entry<String1, String2> where
     *            String1 is the label and key of the entry, and String2 is the
     *            value to assign to that label as well as the value of the entry.
     * @return the number of tuples updated in the database.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     *             if the value is invalid for the annotation type.
     */
    public int updateExperiment(String expID, HashMap<String, String> annotations)
            throws SQLException, IOException {

        // Validate all annotations
        for (Entry<String, String> e : annotations.entrySet()) {
            String key = e.getKey();
            String value = e.getValue();
            e.setValue(validateAnnotation(key, value));
        }

        String query = "UPDATE Annotated_With SET Value = ?"
                + " WHERE (Label ~~* ?) AND (ExpID ~~* ?)";

        // Run all SQL queries as one transaction
        int rs = 0;
        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            conn.setAutoCommit(false);
            for (Entry<String, String> e : annotations.entrySet()) {
                String key = e.getKey();
                String value = e.getValue();
                stmt.setString(1, value);
                stmt.setString(2, key);
                stmt.setString(3, expID);
                stmt.addBatch();
            }

            int arr[] = stmt.executeBatch();
            for (int i = 0; i < arr.length; i++) {
                rs += arr[i];
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }

        return rs;
    }

    /**
     * Annotates an experiment with the given label and value. Checks
     * so that the value is valid if it is a drop down annotation.
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
    public int annotateExperiment(String expID, String label,
            String value) throws SQLException, IOException {

        value = validateAnnotation(label, value);

        String query = "INSERT INTO Annotated_With "
                + "VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, expID);
        stmt.setString(2, label);
        stmt.setString(3, value);

        int rs = stmt.executeUpdate();
        stmt.close();

        return rs;
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

        String query = "DELETE FROM Annotated_With "
                + "WHERE (ExpID ~~* ? AND Label ~~* ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, expID);
        stmt.setString(2, label);

        int rs = stmt.executeUpdate();
        stmt.close();

        return rs;
    }

    /**
     * Adds all the files that belong to the experiment to an
     * Experiment object.
     *
     * @param e
     *            the experiment to add files to.
     * @return the Experiment object containing all its files.
     * @throws SQLException
     *             if the query does not succeed
     */
    public Experiment fillFiles(Experiment e) throws SQLException {

        String query = "SELECT * FROM File " + "WHERE ExpID ~~* ? AND Status = 'Done'";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, e.getID());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            e.addFile(new FileTuple(rs));
        }

        stmt.close();

        return e;
    }

    /**
     * Fill an Experiment object with all annotations that exists for
     * that experiment.
     *
     * @param e
     *            the Experiment object.
     * @return the Experiment object containing all it's annotations.
     * @throws SQLException
     *             if the query does not succeed
     */
    public Experiment fillAnnotations(Experiment e)
            throws SQLException {

        String query = "SELECT Label, Value FROM Annotated_With "
                + "WHERE ExpID ~~* ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, e.getID());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            e.addAnnotation(rs.getString("Label"),
                    rs.getString("Value"));
        }

        stmt.close();

        return e;
    }

    /**
     * Checks that the annotation value is valid.
     *
     * @param label
     *            the annotation name.
     * @param value
     *            the value to be evaluated.
     * @return Cleaned-up version of the value.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     */
    private String validateAnnotation(String label, String value)
            throws SQLException, IOException {

        Annotation a = annoMethods.getAnnotationObject(label);

        if (a == null) {
            throw new IOException(label + " is not a valid annotation. (Does not exist)");
        }

        if (value == null) {
            throw new IOException("Invalid annotation value. (value null)");
        }

        if (a.dataType == Annotation.FREETEXT) {
            return value;
        }

        List<String> choices = a.getPossibleValues();
        String res = matchChoice(value, choices);

        if (res == null) {
            throw new IOException(value + "is not a valid choice for the drop down annotation " + label);
        }

        return res;
    }

    private String matchChoice(String value, List<String> choices) {

        for (String s : choices) {
            if (value.equalsIgnoreCase(s)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Recursively deletes a folder with all its subfolders and
     * files.
     *
     * @param folder
     *            the folder to delete.
     */
    private static boolean recursiveDelete(File folder) {

        File[] contents = folder.listFiles();

        if (contents == null || contents.length == 0) {
            return folder.delete();
        } else {
            for (File f : contents) {
                recursiveDelete(f);
            }
        }

        return folder.delete();
    }
}
