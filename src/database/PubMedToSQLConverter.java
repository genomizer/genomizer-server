package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PubMedToSQLConverter {

    private String sqlFragmentForExpSearch = "SELECT * FROM Experiment "
            + "WHERE A.Label = ? AND A.Value = ?";

    private String sqlFragmentForExpAttr = "SELECT * FROM File AS F "
            + "WHERE EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE F.ExpID = A.ExpID AND "
            + "A.Label = ? AND A.Value = ?)";

    private String sqlFragmentForFileAttr = "SELECT * FROM File "
            + "WHERE ";

    private String orderBySqlFragment = "\nORDER BY ExpID";

    private final String AND = " AND ";
    private final String OR = " OR ";

    private final String[] fileAttributeArray = {"FileID", "Path",
            "FileType", "Date", "MetaData", "Author", "Uploader",
            "ExpID", "GRVersion", "FileName"};


    private HashSet<String> fileAttributes;
    private String query;
    private List<String> parameters;

    public PubMedToSQLConverter() {

        fileAttributes = new HashSet<String>();
        for (int i = 0; i < fileAttributeArray.length; i++) {
            fileAttributes.add(fileAttributeArray[i]);
        }

        parameters = new ArrayList<String>();
    }

    public String convertFileSearch(String pmStr)
            throws IOException {

        parameters.clear();
        StringBuilder sqlQuery = new StringBuilder();

        while (pmStr.length() > 0) {
            if (startsWithRoundBracket(pmStr)) {
                pmStr = moveFirstChar(pmStr, sqlQuery);
            } else if (startsWithConj(pmStr)) {
                pmStr = moveConj(pmStr, sqlQuery);
            } else {
                pmStr = moveConstraint(pmStr, sqlQuery);
            }
        }

        sqlQuery.append(orderBySqlFragment);
        return sqlQuery.toString();
    }

    public String convertExperimentSearch(String pmStr)
            throws IOException {

        parameters.clear();
        StringBuilder sqlQuery = new StringBuilder();

        while (pmStr.length() > 0) {
            if (startsWithRoundBracket(pmStr)) {
                pmStr = moveFirstChar(pmStr, sqlQuery);
            } else if (startsWithConj(pmStr)) {
                pmStr = moveConj(pmStr, sqlQuery);
            } else {
                pmStr = moveExperimentConstraint(pmStr, sqlQuery);
            }
        }

        sqlQuery.append(orderBySqlFragment);
        return sqlQuery.toString();
    }

    private String moveExperimentConstraint(String s, StringBuilder sb) throws IOException {
        int leftSqBrIndex = s.indexOf('[');
        int rightSqBrIndex = s.indexOf(']');

        if (leftSqBrIndex == -1 || rightSqBrIndex == -1) {
            throw new IOException("PubMed String is in wrong format");
        }

        String label = s.substring(leftSqBrIndex + 1, rightSqBrIndex);
        String value = s.substring(0, leftSqBrIndex);

        sb.append(sqlFragmentForExpSearch);
        parameters.add(label);
        parameters.add(value);

        return s.substring(rightSqBrIndex + 1);
    }

    private String moveConstraint(String s, StringBuilder sb)
            throws IOException {

        int leftSqBrIndex = s.indexOf('[');
        int rightSqBrIndex = s.indexOf(']');

        if (leftSqBrIndex == -1 || rightSqBrIndex == -1) {
            throw new IOException("PubMed String is in wrong format");
        }

        String label = s.substring(leftSqBrIndex + 1, rightSqBrIndex);
        String value = s.substring(0, leftSqBrIndex);

        if (fileAttributes.contains(label)) {
            sb.append(sqlFragmentForFileAttr);
            sb.append(label);
            sb.append(" = ? ");
            parameters.add(value);
        } else {
            sb.append(sqlFragmentForExpAttr);
            parameters.add(label);
            parameters.add(value);
        }

        return s.substring(rightSqBrIndex + 1);
    }

    private String moveConj(String s, StringBuilder sb) {
        if (s.startsWith(OR)) {
            sb.append("\nUNION\n");
            return s.substring(4);
        }
        sb.append("\nINTERSECT\n");
        return s.substring(5);
    }

    private boolean startsWithConj(String s) {
        return (s.startsWith(AND) || s.startsWith(OR));
    }

    private String moveFirstChar(String s, StringBuilder sb) {
        sb.append(s.charAt(0));
        return s.substring(1);
    }

    private boolean startsWithRoundBracket(String s) {
        return (s.charAt(0) == '(' || s.charAt(0) == ')');
    }

    public List<String> getParameters() {
        return parameters;
    }

    public String getQuery() {
        return query;
    }

    public boolean hasFileConsatraint(String pubMedString) {

        while (pubMedString.indexOf('[') != - 1) {
            pubMedString = pubMedString.substring(pubMedString.indexOf('[') + 1);
            String label = pubMedString.substring(0, pubMedString.indexOf(']'));
            if (fileAttributes.contains(label)) {
                return true;
            }
        }

        return false;
    }

}
