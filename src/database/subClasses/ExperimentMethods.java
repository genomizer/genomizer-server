package database.subClasses;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Annotation;
import database.Experiment;
import database.FilePathGenerator;
import database.FileTuple;

public class ExperimentMethods {

	private Connection conn;
	private FilePathGenerator fpg;
	AnnotationMethods annoMethods;

	public ExperimentMethods(Connection connection, FilePathGenerator filePG,
								AnnotationMethods annoM){

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

    	String query = "SELECT ExpID FROM Experiment " + "WHERE ExpID = ?";

        PreparedStatement getExp = conn.prepareStatement(query);
        getExp.setString(1, expID);
        ResultSet rs = getExp.executeQuery();
        Experiment e = null;

        if (rs.next()) {
            e = new Experiment(rs.getString("ExpID"));
            e = fillAnnotations(e);
            e = fillFiles(e);
        }

        getExp.close();

        return e;
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

        String query = "INSERT INTO Experiment " + "(ExpID) VALUES (?)";
        PreparedStatement addExp = conn.prepareStatement(query);
        addExp.setString(1, expID);

        fpg.generateExperimentFolders(expID);

        int res = addExp.executeUpdate();
        addExp.close();

        return res;
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
     */
    public int deleteExperiment(String expId) throws SQLException {

    	String statementStr = "DELETE FROM Experiment " + "WHERE (ExpID = ?)";

        PreparedStatement deleteExperiment = conn
                .prepareStatement(statementStr);
        deleteExperiment.setString(1, expId);

        int res = deleteExperiment.executeUpdate();
        deleteExperiment.close();

        return res;
    }

    /**
     * Checks if a given experiment ID exists in the database.
     *
     * @param expID
     *            the experiment ID to look for.
     * @return true if the experiment exists in the database, else
     *         false.
     * @throws SQLException
     *             if the query does not succeed
     */
    public boolean hasExperiment(String expID) throws SQLException {

    	String query = "SELECT ExpID FROM Experiment " + "WHERE ExpID = ?";

        PreparedStatement hasExp = conn.prepareStatement(query);
        hasExp.setString(1, expID);
        ResultSet rs = hasExp.executeQuery();

        boolean res = rs.next();
        hasExp.close();

        return res;
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
    public int updateExperiment(String expID, String label,
            String value) throws SQLException, IOException {

        if (!isValidAnnotationValue(label, value)) {
            throw new IOException(
                    value
                            + " is not a valid choice for the annotation type "
                            + label);
        }

        String query = "UPDATE Annotated_With SET Value = ? WHERE (Label = ?) AND (ExpID = ?)";

        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, value);
        ps.setString(2, label);
        ps.setString(3, expID);

        int res = ps.executeUpdate();
        ps.close();
        return res;
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

    	if (!isValidAnnotationValue(label, value)) {
            throw new IOException(value
                    +" is not a valid choice for the annotation type " + label);
        }

        String query = "INSERT INTO Annotated_With " + "VALUES (?, ?, ?)";
        PreparedStatement tagExp = conn.prepareStatement(query);
        tagExp.setString(1, expID);
        tagExp.setString(2, label);
        tagExp.setString(3, value);

        int res = tagExp.executeUpdate();
        tagExp.close();

        return res;
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

    	String statementStr = "DELETE FROM Annotated_With "
                + "WHERE (ExpID = ? AND Label = ?)";

        PreparedStatement deleteTag = conn.prepareStatement(statementStr);
        deleteTag.setString(1, expID);
        deleteTag.setString(2, label);

        int res = deleteTag.executeUpdate();
        deleteTag.close();

        return res;
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

        String query = "SELECT * FROM File " + "WHERE ExpID = ?";
        PreparedStatement getFiles = conn.prepareStatement(query);
        getFiles.setString(1, e.getID());
        ResultSet rs = getFiles.executeQuery();

        while (rs.next()) {
            e.addFile(new FileTuple(rs));
        }

        getFiles.close();

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
                + "WHERE ExpID = ?";
        PreparedStatement getExpAnnotations = conn.prepareStatement(query);
        getExpAnnotations.setString(1, e.getID());
        ResultSet rs = getExpAnnotations.executeQuery();

        while (rs.next()) {
            e.addAnnotation(rs.getString("Label"), rs.getString("Value"));
        }

        getExpAnnotations.close();

        return e;
    }

    /**
     * Checks so that the annotation value is valid.
     *
     * @param label
     *            the annotation name.
     * @param value
     *            the value to be evaluated.
     * @return true if the value is valid, else false.
     * @throws SQLException
     *             if the query does not succeed
     */
    private boolean isValidAnnotationValue(String label, String value)
            throws SQLException {

        return annoMethods.getAnnotationType(label) == Annotation.FREETEXT
                || annoMethods.getChoices(label).contains(value);
    }

}
