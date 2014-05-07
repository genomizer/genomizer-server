package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
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

    private List<String> getValues(ResultSet rs) throws SQLException {
        values = new ArrayList<String>();
        do {
            values.add(rs.getString("Value"));
        } while (rs.next());
        return values;
    }
    
    public List<String> getPossibleValues() {
        return values;
    }
    
}
