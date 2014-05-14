package database.subClasses;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Annotation;

public class AnnotationMethods {

	private Connection conn;

	public AnnotationMethods(Connection connection){

		conn = connection;
	}

	 /**
     * Gets all the annotation possibilities from the database.
     *
     * @return a Map with the label string as key and datatype as
     *         value.
     *
     *         The possible datatypes are FREETEXT and DROPDOWN.
     * @throws SQLException
     *             if the query does not succeed
     */
    public Map<String, Integer> getAnnotations() throws SQLException {

        HashMap<String, Integer> annotations = new HashMap<String, Integer>();
        String query = "SELECT * FROM Annotation";

        Statement getAnnotations = conn.createStatement();
        ResultSet rs = getAnnotations.executeQuery(query);

        while (rs.next()) {
            if (rs.getString("DataType").equalsIgnoreCase("FreeText")) {
                annotations.put(rs.getString("Label"),
                        Annotation.FREETEXT);
            } else {
                annotations.put(rs.getString("Label"),
                        Annotation.DROPDOWN);
            }
        }

        getAnnotations.close();

        return annotations;
    }

    /**
     * Creates an Annotation object from an annotation label.
     *
     * @param label
     *            the name of the annotation to create the object for.
     * @return the Annotation object. If the label does not exist,
     *         then null will be returned.
     * @throws SQLException
     *             if the query does not succeed.
     */
    public Annotation getAnnotationObject(String label)
            throws SQLException {

        String query = "SELECT * FROM Annotation "
                + "LEFT JOIN Annotation_Choices "
                + "ON (Annotation.Label = Annotation_Choices.Label) "
                + "WHERE Annotation.Label = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, label);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Annotation(rs);
        } else {
            return null;
        }
    }

    /**
     * Creates a list of Annotation objects from a list of annotation
     * labels.
     *
     * @param labels
     *            the list of labels.
     * @return will return a list with all the annotations with valid
     *         labels. If the list with labels is empty or none of the
     *         labels are valid, then it will return null.
     * @throws SQLException
     *             if the query does not succeed.
     */
    public List<Annotation> getAnnotationObjects(List<String> labels)
            throws SQLException {

        List<Annotation> annotations = null;
        Annotation annotation = null;

        for (String label : labels) {
            annotation = getAnnotationObject(label);
            if (annotation != null) {
                if (annotations == null) {
                    annotations = new ArrayList<Annotation>();
                }
                annotations.add(annotation);
            }
        }

        return annotations;
    }

    /**
     * Finds all annotationLabels that exist in the database, example
     * of labels: sex, tissue, etc...
     *
     * @return ArrayList<String> annotationLabels
     */
    public ArrayList<String> getAllAnnotationLabels() {

        ArrayList<String> allAnnotationlabels = new ArrayList<>();

        String findAllLabelsQuery = "SELECT Label FROM Annotation";
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(findAllLabelsQuery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                allAnnotationlabels.add(res.getString("Label"));
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return allAnnotationlabels;
    }

    /**
     * Gets the datatype of a given annotation.
     *
     * @param label
     *            annotation label.
     * @return the annotation's datatype (FREETEXT or DROPDOWN).
     *
     * @throws SQLException
     *             if the query does not succeed
     */
    public Integer getAnnotationType(String label)
            throws SQLException {

        Map<String, Integer> annotations = getAnnotations();

        return annotations.get(label);
    }

    /**
     * Gets the default value for a annotation if there is one, If not
     * it returns NULL.
     *
     * @param annotationLabel
     *            the name of the annotation to check
     * @return The defult value or NULL.
     * @throws SQLException
     */
    public String getDefaultAnnotationValue(String annotationLabel)
            throws SQLException {

        String query = "SELECT DefaultValue FROM Annotation WHERE Label = ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, annotationLabel);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getString("DefaultValue");
        }

        return null;
    }

    /**
     * Deletes an annotation from the list of possible annotations.
     *
     * @param label
     *            the label of the annotation to delete.
     * @return the number of tuples deleted in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int deleteAnnotation(String label) throws SQLException {

        String statementStr = "DELETE FROM Annotation "
                + "WHERE (Label = ?)";
        PreparedStatement deleteAnnotation = conn
                .prepareStatement(statementStr);
        deleteAnnotation.setString(1, label);

        int res = deleteAnnotation.executeUpdate();
        deleteAnnotation.close();

        return res;
    }

    /**
     * Adds a free text annotation to the list of possible
     * annotations.
     *
     * @param label
     *            the name of the annotation.
     * @param required
     *            if the annotation should be forced or not
     * @param defaultValue
     *            the default value this field should take or null if
     *            a default value is not required
     * @return the number of tuples updated in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int addFreeTextAnnotation(String label,
            String defaultValue, boolean required)
            throws SQLException {

        String query = "INSERT INTO Annotation "
                + "VALUES (?, 'FreeText', ?, ?)";

        PreparedStatement addAnnotation = conn
                .prepareStatement(query);
        addAnnotation.setString(1, label);
        addAnnotation.setString(2, defaultValue);
        addAnnotation.setBoolean(3, required);

        int res = addAnnotation.executeUpdate();
        addAnnotation.close();

        return res;
    }

    /**
     * Checks if a given annotation is required to be filled by the
     * user.
     *
     * @param annotationLabel
     *            the name of the annotation to check
     * @return true if it is required, else false
     * @throws SQLException
     */
    public boolean isAnnotationRequiered(String annotationLabel)
            throws SQLException {

        String query = "SELECT Required FROM Annotation WHERE Label = ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, annotationLabel);

        ResultSet rs = ps.executeQuery();
        boolean isRequired = false;

        while (rs.next()) {
            isRequired = rs.getBoolean("Required");
        }

        return isRequired;
    }

    /**
     * Gets all the choices for a drop down annotation. Deprecated,
     * use {@link #getChoices(String) getChoices} instead.
     *
     * @param label
     *            the drop down annotation to get the choice for.
     * @return the choices.
     * @throws SQLException
     *             if the query does not succeed
     */
    @Deprecated
    public ArrayList<String> getDropDownAnnotations(String label)
            throws SQLException {

        String query = "SELECT Value FROM Annotation_Choices "
                + "WHERE (Label = ?)";
        ArrayList<String> dropDownStrings = new ArrayList<String>();

        PreparedStatement getDropDownStrings = conn
                .prepareStatement(query);
        getDropDownStrings.setString(1, label);

        ResultSet rs = getDropDownStrings.executeQuery();

        while (rs.next()) {
            dropDownStrings.add(rs.getString("Value"));
        }

        getDropDownStrings.close();

        return dropDownStrings;
    }

    /**
     * Adds a drop down annotation to the list of possible
     * annotations.
     *
     * @param label
     *            the name of the annotation.
     * @param choices
     *            the possible values for the annotation.
     * @return the number of tuples inserted into the database.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     *             if the choices are invalid
     */
    public int addDropDownAnnotation(String label,
            List<String> choices, int defaultValueIndex,
            boolean required) throws SQLException, IOException {

        if (choices.isEmpty()) {
            throw new IOException("Must specify at least one choice");
        }

        if (defaultValueIndex < 0
                || defaultValueIndex >= choices.size()) {
            throw new IOException("Invalid default value index");
        }

        int tuplesInserted = 0;

        String annotationQuery = "INSERT INTO Annotation "
                + "VALUES (?, 'DropDown', ?, ?)";

        String choicesQuery = "INSERT INTO Annotation_Choices "
                + "(Label, Value) VALUES (?, ?)";

        PreparedStatement addAnnotation = conn
                .prepareStatement(annotationQuery);

        addAnnotation.setString(1, label);
        addAnnotation.setString(2, choices.get(defaultValueIndex));
        addAnnotation.setBoolean(3, required);
        tuplesInserted += addAnnotation.executeUpdate();
        addAnnotation.close();

        PreparedStatement addChoices = conn
                .prepareStatement(choicesQuery);
        addChoices.setString(1, label);

        for (String choice : choices) {
            addChoices.setString(2, choice);
            try {
                tuplesInserted += addChoices.executeUpdate();
            } catch (SQLException e) {
                /*
                 * Ignore and try adding next choice. This is probably
                 * due to the list of choices containing a duplicate.
                 */
            }
        }

        addChoices.close();

        return tuplesInserted;

    }

    /**
     * Method to add a value to a existing DropDown annotation.
     *
     * @param label
     *            , the label of the chosen DropDown annotation.
     * @param value
     *            , the value that will be added to the DropDown
     *            annotation.
     * @return, Integer, how many rows that were added to the
     *          database.
     * @throws SQLException
     *             , if the value already exist or another SQL error.
     * @throws IOException
     *             , if the chosen label does not represent a DropDown
     *             annotation.
     */
    public int addDropDownAnnotationValue(String label, String value)
            throws SQLException, IOException {

        String statementStr = "SELECT * FROM Annotation WHERE "
                + "(label = ? AND datatype = 'DropDown')";

        PreparedStatement checkTag = conn
                .prepareStatement(statementStr);
        checkTag.setString(1, label);

        ResultSet rs = checkTag.executeQuery();
        boolean res = rs.next();
        checkTag.close();

        if (!res) {
            throw new IOException(
                    "The annotation of the chosen label"
                            + " is not of type DropDown");
        } else {
            statementStr = "INSERT INTO Annotation_Choices (label , value) "
                    + "VALUES (?,?)";

            PreparedStatement insertTag = conn
                    .prepareStatement(statementStr);

            insertTag.setString(1, label);
            insertTag.setString(2, value);
            int ress = insertTag.executeUpdate();
            insertTag.close();

            return ress;
        }
    }

    /**
     * Method to remove a given annotation of a dropdown- annotation.
     *
     * @param label
     *            , the label of the chosen annotation
     * @param the
     *            value of the chosen annotation.
     * @return Integer, how many values that were deleted.
     * @throws SQLException
     * @throws IOException
     *             , throws an IOException if the chosen value to be
     *             removed is the active DefaultValue of the chosen
     *             label.
     *
     */
    public int removeAnnotationValue(String label, String value)
            throws SQLException, IOException {

        String statementStr = "SELECT * FROM Annotation WHERE "
                + "(label = ? AND defaultvalue = ?)";

        PreparedStatement checkTag = conn
                .prepareStatement(statementStr);
        checkTag.setString(1, label);
        checkTag.setString(2, value);

        ResultSet rs = checkTag.executeQuery();

        boolean res = rs.next();
        checkTag.close();

        if (res) {
            throw new IOException(
                    "The chosen value of the label is a"
                            + " default value. Change the default value of "
                            + "the label and run this method again.");
        } else {
            statementStr = "DELETE FROM Annotation_Choices "
                    + "WHERE (label = ? AND value = ?)";

            PreparedStatement deleteTag = conn
                    .prepareStatement(statementStr);
            deleteTag.setString(1, label);
            deleteTag.setString(2, value);

            int ress = deleteTag.executeUpdate();
            deleteTag.close();

            return ress;
        }
    }

    /**
     * Changes the annotation Label value.
     *
     * @param String
     *            oldLabel
     * @param string
     *            newLabel
     * @return boolean true if changed succeeded, false if it failed.
     */
    public boolean changeAnnotationLabel(String oldLabel,
            String newLabel) {

        String changeLblQuery = "UPDATE Annotation SET Label = ?"
                + " WHERE (Label =?)";

        PreparedStatement lblExp;

        try {
            lblExp = conn.prepareStatement(changeLblQuery);

            lblExp.setString(1, newLabel);
            lblExp.setString(2, oldLabel);
            lblExp.execute();
            return true;

        } catch (SQLException e) {

            System.out.println("Failed to Create changeLabel query");
            return false;
        }
    }

    /*
     * Changes the value of an annotation corresponding to it's label.
     * Parameters: label of annotation, the old value and the new
     * value to change to. Throws an SQLException if the new value
     * already exists in the choices table (changing all males to
     * female, and female is already in the table)
     *
     * @param String label
     *
     * @param String oldValue
     *
     * @param String newValue
     *
     * @throws SQLException
     */
    public void changeAnnotationValue(String label, String oldValue,
            String newValue) throws SQLException {

        String query = "UPDATE Annotation_Choices "
                + "SET Value = ? " + "WHERE Label = ? and Value = ?";

        String query2 = "UPDATE Annotated_With " + "SET Value = ? "
                + "WHERE Label = ? and Value = ?";

        String query3 = "UPDATE Annotation "
                + "SET DefaultValue = ? "
                + "WHERE Label = ? and DefaultValue = ?";

        PreparedStatement statement = conn.prepareStatement(query);
        ArrayList<String> parameters = new ArrayList<String>();
        parameters.add(newValue);
        parameters.add(label);
        parameters.add(oldValue);
        statement = bind(statement, parameters);
        statement.executeUpdate();
        statement.close();

        statement = conn.prepareStatement(query2);
        statement = bind(statement, parameters);
        statement.executeUpdate();
        statement.close();

        statement = conn.prepareStatement(query3);
        statement = bind(statement, parameters);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Gets all the choices for a drop down annotation.
     *
     * @param label
     *            the drop down annotation to get the choice for.
     * @return the choices.
     * @throws SQLException
     *             if the query does not succeed
     */
    public List<String> getChoices(String label) throws SQLException {

        String query = "SELECT Value FROM Annotation_Choices "
                + "WHERE Label = ?";
        List<String> choices = new ArrayList<String>();

        PreparedStatement getChoices = conn.prepareStatement(query);
        getChoices.setString(1, label);

        ResultSet rs = getChoices.executeQuery();

        while (rs.next()) {
            choices.add(rs.getString("Value"));
        }

        getChoices.close();

        return choices;
    }

    public PreparedStatement bind(PreparedStatement query,
            List<String> params) throws SQLException {

        for (int i = 0; i < params.size(); i++) {
            query.setString(i + 1, params.get(i));
        }

        return query;
    }

}
