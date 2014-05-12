package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an annotation with all properties as
 * seen in the database. Use the public final
 * fields to get the values for Label, DataType,
 * Required and DefaultValue. To get the values
 * a dropdown annotation can have, use the
 * {@link #getPossibleValues()
 * getPossibleValues} method.
 *
 *
 * An idea just now! Not used yet...
 *
 * @author ruaridh
 *
 */
public class Annotation {

    public static Integer FREETEXT = 1;
    public static Integer DROPDOWN = 2;

    public final String label;
    public final int dataType;
    public final boolean isRequired;

    private List<String> values;        // null for freetext annotations
    public final String defaultValue;   // null if no default was set

    /**
     * Creates an annotation object from a ResultSet. Don't call
     * this constructor directly, use
     * {@link database.DatabaseAccessor#getAnnotationObject(String label)
     * getAnnotationObject} to create an Annotation object.
     * @param rs the ResultSet.
     * @throws SQLException
     */
    public Annotation(ResultSet rs) throws SQLException {
        label = rs.getString("Label");

        if (rs.getString("DataType").equalsIgnoreCase("freetext")) {
            dataType = FREETEXT;
        } else {
            dataType = DROPDOWN;
        }

        isRequired = rs.getBoolean("Required");

        defaultValue = rs.getString("DefaultValue");

        if (dataType == DROPDOWN) {
            values = getValues(rs);
        }

    }

    /**
     * Private method that gets all the dropdown values for an annotation
     * from a ResultSet and stores them in a list.
     * @param rs the ResultSet.
     * @return the list with dropdown values.
     * @throws SQLException
     */
    private List<String> getValues(ResultSet rs) throws SQLException {
    	values = new ArrayList<String>();
        do {
            values.add(rs.getString("Value"));
        } while (rs.next());
        return values;
    }

    /**
     * Gets all the possible values for dropdown annotations. Will
     * return an empty list if called on a freetext annotation.
     * @return a list with the possible values for this annotation.
     */
    public List<String> getPossibleValues() {
        return values;
    }

}
