package database;

import java.util.ArrayList;
import java.util.List;

public class PubMedToSQLConverter {

    private static String sqlFragment = "SELECT * FROM File AS F "
            + "WHERE EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE F.ExpID = A.ExpID AND " + "A.Label = ? AND A.Value = ?);";

    private static List<String> parameters;

    public static String convert(String pmStr) {
        parameters = new ArrayList<String>();
        return sqlFragment;
    }

    public static List<String> getParameters() {
        return parameters;
    }

}
