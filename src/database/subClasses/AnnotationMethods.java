package database.subClasses;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import database.Annotation;
import database.PubMedToSQLConverter;

/**
 * Class that contains all the methods for adding,changing, getting and removing
 * Annotations in the database. This class is a subClass of
 * databaseAcessor.java.
 *
 * date: 2014-05-14 version: 1.0
 */
public class AnnotationMethods {

    private Connection conn;
    private final String[] fileAttributesArray = { "fileid", "date", "path",
            "filetype", "metadata", "author", "uploader", "expid", "grversion",
            "filename" };
    private HashSet<String> fileAttributes;


    /**
     * Constructor for the AnnotationMethod object.
     *
     * @param connection
     *            Connection, the connection to the database.
     */
    public AnnotationMethods(Connection connection) {
        fileAttributes = new HashSet<String>();
        for (int i = 0; i < fileAttributesArray.length; i++) {
            fileAttributes.add(fileAttributesArray[i]);
        }
        conn = connection;
    }


    /**
     * Gets all the annotation possibilities from the database.
     *
     * @return annotations Map<String, Integer> a Map with the label string as
     *         key and datatype as value. The possible datatypes are FREETEXT
     *         and DROPDOWN.
     * @throws SQLException
     *             if the query does not succeed
     */
    public Map<String, Integer> getAnnotations() throws SQLException {

        HashMap<String, Integer> annotations = new HashMap<String, Integer>();
        String query = "SELECT * FROM Annotation";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            if (rs.getString("DataType").equalsIgnoreCase("FreeText")) {
                annotations.put(rs.getString("Label"), Annotation.FREETEXT);
            } else {
                annotations.put(rs.getString("Label"), Annotation.DROPDOWN);
            }
        }

        stmt.close();

        return annotations;
    }


    /**
     * Creates an Annotation object from an annotation label.
     *
     * @param label
     *            Stringthe name of the annotation to create the object for.
     * @return Annotation - the Annotation object. If the label does not exist,
     *         then null will be returned.
     * @throws SQLException
     *             if the query does not succeed.
     */
    public Annotation getAnnotationObject(String label) throws SQLException {

        String query = "SELECT * FROM Annotation "
                + "LEFT JOIN Annotation_Choices "
                + "ON (Annotation.Label = Annotation_Choices.Label) "
                + "WHERE Annotation.Label ~~* ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, label);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Annotation anno = new Annotation(rs);
            stmt.close();
            return anno;
        } else {
            stmt.close();
            return null;
        }
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

        List<Annotation> annotationsList = null;
        Annotation annotation = null;

        for (String label : labels) {
            annotation = getAnnotationObject(label);
            if (annotation != null) {
                if (annotationsList == null) {
                    annotationsList = new ArrayList<Annotation>();
                }
                annotationsList.add(annotation);
            }
        }

        return annotationsList;
    }


    /**
     * Finds all annotationLabels that exist in the database, example of labels:
     * sex, tissue, etc...
     *
     * @return annotationLabels ArrayList<String>
     */
    public ArrayList<String> getAllAnnotationLabels() {

        ArrayList<String> annotationLabelList = new ArrayList<>();

        String query = "SELECT Label FROM Annotation";
        PreparedStatement stmt;

        try {
            stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                annotationLabelList.add(rs.getString("Label"));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return annotationLabelList;
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

        Annotation a = getAnnotationObject(label);
        if (a == null) {
            return 0;
        }
        return a.dataType;
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

        String query = "SELECT DefaultValue FROM Annotation WHERE Label ~~* ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, annotationLabel);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String value = rs.getString("DefaultValue");
            stmt.close();
            return value;
        }

        stmt.close();
        return null;
    }


    /**
     * Deletes an annotation from the list of possible annotations. Label
     * SPECIES can't be changed because of dependencies in other tables.
     *
     * @param label
     *            String - the label of the annotation to delete.
     * @return res integer - the number of tuples deleted in the database.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     *             if label = "Species"
     */
    public int deleteAnnotation(String label) throws SQLException, IOException {

        if (label.toLowerCase().contentEquals("species")) {
            throw new IOException("Can't remove annotation 'Species'");
        }

        String query = "SELECT * FROM Annotated_With WHERE Label ~~* ? ";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, label);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            throw new IOException(label
                    + " is used by at least one experiment "
                    + "and therefore cannot be removed");
        }

        String query2 = "DELETE FROM Annotation " + "WHERE (Label ~~* ?)";
        ps = conn.prepareStatement(query2);
        ps.setString(1, label);

        int res = ps.executeUpdate();
        ps.close();

        return res;
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

    	if (label==null || label.isEmpty()) {
    		throw new IOException("Invalid Label");
    	}
    	if (defaultValue!=null && defaultValue.isEmpty()) {
    		throw new IOException("Invalid DefaultValue");
    	}

        if (isFileAnnotation(label)) {
            throw new IOException(
                    "The given annotation is a file- annotation.'");
        }

        if (!isValidChoice(label)) {
            throw new IOException("Lable contains invalid characters");
        }

        Annotation a = getAnnotationObject(label);
        if (a != null) {
            throw new IOException("This annotation already exists");
        }

        if (defaultValue != null) {
            if (!isValidChoice(defaultValue)) {
                throw new IOException(
                        "defaultValue contains invalid characters");
            }
        }

        String query = "INSERT INTO Annotation "
                + "VALUES (?, 'FreeText', ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, label);
        stmt.setString(2, defaultValue);
        stmt.setBoolean(3, required);

        int rs = stmt.executeUpdate();
        stmt.close();

        return rs;
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

        String query = "SELECT Required FROM Annotation WHERE Label ~~* ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, annotationLabel);

        ResultSet rs = stmt.executeQuery();
        boolean isRequired = false;

        while (rs.next()) {
            isRequired = rs.getBoolean("Required");
        }

        stmt.close();
        return isRequired;
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

        ArrayList<String> dropDownLabelsList = new ArrayList<String>();
        String query = "SELECT Value FROM Annotation_Choices "
                + "WHERE (Label ~~* ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, label);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            dropDownLabelsList.add(rs.getString("Value"));
        }

        stmt.close();

        return dropDownLabelsList;
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

    	if (label==null || label.isEmpty()) {
    		throw new IOException("Invalid Label");
    	}


        if (isFileAnnotation(label)) {
            throw new IOException(
                    "The given annotation is a file- annotation.'");
        }

        if (!isValidChoice(label)) {
            throw new IOException("Lable contains invalid characters");
        }

        Annotation a = getAnnotationObject(label);
        if (a != null) {
            throw new IOException("This annotation already exists.");
        }

        if (choices.isEmpty()) {
            throw new IOException("Must specify at least one choice");
        }

        for (int i = 0; i < choices.size(); i++) {
            if (!isValidChoice(choices.get(i))) {
                throw new IOException("Choices contains invalid characters");
            }
        }

        if (defaultValueIndex < 0 || defaultValueIndex >= choices.size()) {
            throw new IOException("Invalid default value index");
        }

        int tuplesInserted = 0;

        String annotationQuery = "INSERT INTO Annotation "
                + "VALUES (?, 'DropDown', ?, ?)";

        String choicesQuery = "INSERT INTO Annotation_Choices "
                + "(Label, Value) VALUES (?, ?)";

        PreparedStatement addAnnoStatement = conn
                .prepareStatement(annotationQuery);

        addAnnoStatement.setString(1, label);
        addAnnoStatement.setString(2, choices.get(defaultValueIndex));
        addAnnoStatement.setBoolean(3, required);
        tuplesInserted += addAnnoStatement.executeUpdate();
        addAnnoStatement.close();

        PreparedStatement addChoicesStatement = conn
                .prepareStatement(choicesQuery);
        addChoicesStatement.setString(1, label);

        for (String choice : choices) {
            addChoicesStatement.setString(2, choice);
            try {
                tuplesInserted += addChoicesStatement.executeUpdate();
            } catch (SQLException e) {
                /*
                 * Ignore and try adding next choice. This is probably due to
                 * the list of choices containing a duplicate.
                 */
            }
        }

        addChoicesStatement.close();

        return tuplesInserted;
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



    	if (label==null || label.isEmpty()) {
    		throw new IOException("Invalid Label");
    	}
    	if (value==null || value.isEmpty()) {
    		throw new IOException("Invalid Value");
    	}

        if (!isValidChoice(value)) {
            throw new IOException("Value contains invalid characters");
        }

        String query = "SELECT * FROM Annotation WHERE "
                + "(label ~~* ? AND datatype = 'DropDown')";

        PreparedStatement checkTagStatement = conn.prepareStatement(query);
        checkTagStatement.setString(1, label);

        ResultSet rs = checkTagStatement.executeQuery();
        boolean hasResult = rs.next();
        checkTagStatement.close();

        if (!hasResult) {
            throw new IOException("The annotation of the chosen label"
                    + " is not of type DropDown");
        } else {
            query = "INSERT INTO Annotation_Choices (label , value) "
                    + "VALUES (?,?)";

            PreparedStatement insertTagStatement = conn.prepareStatement(query);

            insertTagStatement.setString(1, label);
            insertTagStatement.setString(2, value);
            int resCount = insertTagStatement.executeUpdate();
            insertTagStatement.close();

            return resCount;
        }
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
    public int removeDropDownAnnotationValue(String label, String value)
            throws SQLException, IOException {

        // Check if value is a dropdown choice and used on any experiments
        String dependQuery = "SELECT * FROM Annotated_With "
                + "WHERE (label ~~* ? AND value ~~* ?)";

        PreparedStatement dependencyStatement = conn
                .prepareStatement(dependQuery);
        dependencyStatement.setString(1, label);
        dependencyStatement.setString(2, value);
        ResultSet res = dependencyStatement.executeQuery();

        boolean hasDependency = res.next();
        dependencyStatement.close();
        if (hasDependency) {
            throw new IOException(value
                    + " is used in other experiments under label " + label
                    + " and can therefore not be removed.");
        }

        String query = "SELECT * FROM Annotation WHERE "
                + "(label ~~* ? AND defaultvalue ~~* ?)";

        PreparedStatement checkTagStatement = conn.prepareStatement(query);
        checkTagStatement.setString(1, label);
        checkTagStatement.setString(2, value);
        ResultSet rs = checkTagStatement.executeQuery();

        boolean hasResult = rs.next();
        checkTagStatement.close();

        if (hasResult) {
            throw new IOException(value + " is the default setting for "
                    + label + " and can therefore not be removed.\n"
                    + "A new default value must first be set.");
        } else {
            query = "DELETE FROM Annotation_Choices "
                    + "WHERE (label ~~* ? AND value ~~* ?)";

            PreparedStatement deleteTagStatement = conn.prepareStatement(query);
            deleteTagStatement.setString(1, label);
            deleteTagStatement.setString(2, value);

            int resCount = deleteTagStatement.executeUpdate();
            deleteTagStatement.close();
            return resCount;
        }
    }


    /**
     * Changes the annotation label.
     *
     * OBS! This changes the label for all experiments. Label SPECIES can't be
     * changed because of dependencies in other tables. If the Species label can
     * be changed to another, it becomes removable.
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
     * @throws Exception
     *             if label = "Species"
     */
    public int changeAnnotationLabel(String oldLabel, String newLabel)
            throws SQLException, IOException {

    	if (oldLabel == null || oldLabel.contentEquals("")
    			|| newLabel == null || newLabel.contentEquals("")) {
    		throw new IOException("Invalid parameters");
    	}

        if (oldLabel.toLowerCase().contentEquals("species")) {
            throw new IOException("Can't change label on annotation 'Species'");
        } else if (isFileAnnotation(newLabel)){
        	throw new IOException("Can't change label name to a file- annotation name.");
        }
        else {
            if (!isValidChoice(newLabel)) {
                throw new IOException(newLabel
                        + " contains invalid characters.\n"
                        + "Brackets cannot be used in annotations.");
            }

            String query = "UPDATE Annotation SET Label = ? WHERE (Label ~~* ?)";

            PreparedStatement stmt;
            stmt = conn.prepareStatement(query);
            stmt.setString(1, newLabel);
            stmt.setString(2, oldLabel);

            int resCount = stmt.executeUpdate();
            stmt.close();
            return resCount;
        }
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
     * @throws ParseException
     */
    public void changeAnnotationValue(String label, String oldValue,
            String newValue) throws SQLException, IOException, ParseException {

        if (!isValidChoice(newValue)) {
            throw new IOException(newValue + " contains invalid characters.\n"
                    + "Brackets cannot be used in annotations.");
        }

        String query = "UPDATE Annotation_Choices " + "SET Value = ? "
                + "WHERE Label ~~* ? and Value ~~* ?";

        String query2 = "UPDATE Annotated_With " + "SET Value = ? "
                + "WHERE Label ~~* ? and Value ~~* ?";

        String query3 = "UPDATE Annotation " + "SET DefaultValue = ? "
                + "WHERE Label ~~* ? and DefaultValue ~~* ?";

        String query4 = "UPDATE Genome_Release " + "SET Species = ? "
                + "WHERE Species ~~* ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        ArrayList<String> parameterList = new ArrayList<String>();
        parameterList.add(newValue);
        parameterList.add(label);
        parameterList.add(oldValue);
        stmt = bindStrings(stmt, parameterList);
        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement(query2);
        stmt = bindStrings(stmt, parameterList);
        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement(query3);
        stmt = bindStrings(stmt, parameterList);
        stmt.executeUpdate();
        stmt.close();

        if (label.equalsIgnoreCase("species")) {
            stmt = conn.prepareStatement(query4);
            stmt.setString(1, newValue);
            stmt.setString(2, oldValue);
            stmt.executeUpdate();
            stmt.close();
        }
    }


    private PreparedStatement bindStrings(PreparedStatement stmt,
            ArrayList<String> parameterList) throws SQLException {

        int i = 1;
        for (String s : parameterList) {
            stmt.setString(i, s);
            i++;
        }
        return stmt;
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

        String query = "SELECT Value FROM Annotation_Choices "
                + "WHERE Label ~~* ?";
        List<String> choicesList = new ArrayList<String>();

        PreparedStatement getChoices = conn.prepareStatement(query);
        getChoices.setString(1, label);

        ResultSet rs = getChoices.executeQuery();

        while (rs.next()) {
            choicesList.add(rs.getString("Value"));
        }

        getChoices.close();

        return choicesList;
    }


    /**
     * binds a sql prepared query statement with parameters, example:
     * "UPDATE Annotation_Choices SET Value = ? WHERE Label = ? and Value = ?;"
     * and the questionmarks are the parameters.
     *
     * @param query
     *            PreparedStatement
     * @param params
     *            List<String> - the parameters connected to the query, size of
     *            list must be equal to nr of questionmarks in query.
     * @return query PreparedStatement
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public PreparedStatement bind(PreparedStatement query,
            List<Entry<String, String>> params) throws SQLException,
            IOException {
        int i = 1;

        for (Entry<String, String> entry : params) {

            switch (entry.getValue()) {
                case PubMedToSQLConverter.STRING_PARAM:
                    query.setString(i, entry.getKey());
                    break;
                case PubMedToSQLConverter.DATE_PARAM:
                    try {
                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        java.util.Date ud = df.parse(entry.getKey());
                        Date d = new Date(ud.getTime());
                        query.setDate(i, d);
                    } catch (ParseException e) {
                        throw new IOException(
                                "Date in wrong format. Use yyyy/MM/dd");
                    }
                    break;
                case PubMedToSQLConverter.INT_PARAM:
                    try {
                        Integer n = Integer.parseInt(entry.getKey());
                        query.setInt(i, n);
                    } catch (NumberFormatException e) {
                        throw new IOException("File ID number in wrong format");
                    }
                    break;
                default:
                    query.setString(i, entry.getKey());
                    break;
            }
            i++;
        }
        return query;
    }


    private boolean isFileAnnotation(String label) {

        if (fileAttributes.contains(label.toLowerCase())) {
            return true;
        }
        return false;
    }


    /**
     * private method to check if the annotation contains invalid characters
     * ('(', ')', '[' and ']'.
     *
     * @param annotation
     * @return
     */
    private boolean isValidChoice(String annotation) {

        if (annotation == null) {
            return false;
        }

        if (annotation.contains("(") || annotation.contains(")")
                || annotation.contains("[") || annotation.contains("]")) {
            return false;
        }

        return true;
    }

}
