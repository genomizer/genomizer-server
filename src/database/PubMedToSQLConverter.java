package database;

import java.io.IOException;
import java.text.ParseException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Converts a String containing search parameters to an SQL query for the
 * Genomizer database.
 *
 * The search string must be in PubMed format.
 *
 * Example: "(Human[Species] OR Fly[Species]) AND Joe Bloggs[Uploader]"
 *
 * Before choosing a convert method (convertFileSearch or
 * convertExperimentSearch) check if the PubMed string contains file constraints
 * by calling the hasFileConstraints method. If the PubMedString does not
 * contain any file constraints the convertExperimentSearch should be used.
 *
 * @author dv12rwt, Ruaridh Watt
 * @author dv12kko, Kenny Kunto
 * @author dv12ann, André Niklasson
 * @author dv12can, Carl Alexandersson
 * @author yhi04jeo, Jonas Engbo
 * @author oi11mhn, Mattias Hinnerson
 *
 */
public class PubMedToSQLConverter {

    public static final String STRING_PARAM = "string";
    public static final String INT_PARAM = "int";
    public static final String DATE_PARAM = "date";

    private static final String AND = " and ";
    private static final String INTERSECT = "\nINTERSECT\n"; // Will
                                                             // replace
                                                             // AND

    private static final String OR = " or ";
    private static final String UNION = "\nUNION\n"; // Will replace
                                                     // OR

    private static final String NOT = " not ";

    // sql fragments
    private String sqlFragmentForExpSearch = "SELECT ExpID FROM Experiment "
            + "NATURAL JOIN Annotated_With "
            + "WHERE Label ~~* ? AND Value ~* ?";

    private String sqlFragmentForExpSearchNegated = "SELECT ExpID FROM Experiment AS E "
            + "WHERE NOT EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE E.ExpID = A.ExpID AND Label ~~* ? AND Value ~* ?)";

    private String sqlFragmentForExpAttrInFileSearch = "SELECT * FROM File AS F "
            + "WHERE EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE F.ExpID = A.ExpID AND "
            + "A.Label ~~* ? AND A.Value ~* ?) AND F.Status = 'Done'";

    private String sqlFragmentForExpAttrInFileSearchNegated = "SELECT * FROM File AS F "
            + "WHERE NOT EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE F.ExpID = A.ExpID AND "
            + "A.Label ~~* ? AND A.Value ~* ?) AND F.Status = 'Done'";

    private String sqlFragmentForExpIdSearch = "SELECT ExpID FROM Experiment "
            + "WHERE ExpID ~* ?";

    private String sqlFragmentForExpIdSearchNegated = "SELECT ExpID FROM Experiment "
            + "WHERE NOT ExpID ~* ?";

    private String sqlFragmentForFileAttr = "SELECT * FROM File "
            + "WHERE Status='Done' AND ";

    private String orderBySqlFragment = "\nORDER BY ExpID";

    // file attributes to be checked
    private final String[] stringFileAttributesArray = { "Path", "FileType",
            "FileName", "InputFilePath", "MetaData", "Author", "Uploader",
            "GRVersion", "Status" };

    private final String dateFileAttribute = "Date";

    private final String intFileAttribute = "FileID";

    private Map<String, String> fileAttributes;

    private String queryResult;

    private List<Entry<String, String>> parametersResult;


    public PubMedToSQLConverter() {

        fileAttributes = new HashMap<String, String>();
        for (String s : stringFileAttributesArray) {
            fileAttributes.put(s, STRING_PARAM);
        }

        fileAttributes.put(dateFileAttribute, DATE_PARAM);
        fileAttributes.put(intFileAttribute, INT_PARAM);

        parametersResult = new ArrayList<Entry<String, String>>();
    }


    /**
     * Converts a PubMed String to an sql query.
     *
     * The resulting query should only be used if the PubMed string specifies at
     * least one file constraint (can be checked with hasFileConstraint(String
     * pubMedString)).
     *
     * The resulting query will not return experiments with no files.
     *
     * @param pmStr
     *            A String specifying the search criteria in PubMed format.
     * @return An sql String that can be used to search files in the Genomizer
     *         database. The sql query will return all file tuples that satisfy
     *         the criteria.
     * @throws IOException
     *             If the search String is not in PubMed format.
     * @throws ParseException
     */
    public String convertFileSearch(String pmStr) throws IOException {

        parametersResult.clear();

        StringBuilder sqlQuery = new StringBuilder();

        // Change a leading "NOT " to " NOT " so that it matches the NOT Keyword
        // String
        if (pmStr.toLowerCase().startsWith("not ")) {
            pmStr = " " + pmStr;
        }

        while (pmStr.length() > 0) {
            if (startsWithRoundBracket(pmStr)) {
                pmStr = moveFirstChar(pmStr, sqlQuery);
            } else if (startsWithConj(pmStr)) {
                pmStr = moveConj(pmStr, sqlQuery);
            } else if (pmStr.toLowerCase().startsWith(NOT)) {
                pmStr = moveNotStatement(pmStr, sqlQuery);
            } else {
                pmStr = moveConstraint(pmStr, sqlQuery);
            }
        }
        sqlQuery.append(orderBySqlFragment);

        return sqlQuery.toString();
    }


    /**
     * Converts a PubMed String to an sql query.
     *
     * The resulting query should only be used if the PubMed string does not
     * specify a file constraint (can be checked with hasFileConstraint(String
     * pubMedString)).
     *
     * @param pmStr
     *            A String specifying the search criteria in PubMed format.
     * @return An sql String that can be used to search files in the Genomizer
     *         database.
     * @throws IOException
     *             If the search String is not in PubMed format.
     */
    public String convertExperimentSearch(String pmStr) throws IOException {

        parametersResult.clear();
        StringBuilder sqlQuery = new StringBuilder();

        if (pmStr.toLowerCase().startsWith("not ")) {
            pmStr = " " + pmStr;
        }

        while (pmStr.length() > 0) {
            if (startsWithRoundBracket(pmStr)) {
                pmStr = moveFirstChar(pmStr, sqlQuery);
            } else if (startsWithConj(pmStr)) {
                pmStr = moveConj(pmStr, sqlQuery);
            } else if (pmStr.toLowerCase().startsWith(NOT)) {
                pmStr = moveExperimentNotStatement(pmStr, sqlQuery);
            } else {
                pmStr = moveExperimentConstraint(pmStr, sqlQuery);
            }
        }

        sqlQuery.append(orderBySqlFragment);
        return sqlQuery.toString();
    }


    private String moveNotStatement(String s, StringBuilder sb)
            throws IOException {
        if (sb.length() > 0) {
            sb.append(INTERSECT);
        }
        s = s.substring(NOT.length());
        return moveNegatedConstraint(s, sb);
    }


    private String moveNegatedConstraint(String s, StringBuilder sb)
            throws IOException {

        SimpleEntry<String, String> labelValue = getLabelValuePair(s);
        String label = labelValue.getKey();
        String value = labelValue.getValue();

        String attributeType;
        if (label.equalsIgnoreCase("expid")) {
            attributeType = STRING_PARAM;
        } else {
            label = correctLabelCase(label);
            attributeType = fileAttributes.get(label);
        }

        if (attributeType == null) {
            sb.append(sqlFragmentForExpAttrInFileSearchNegated);
            parametersResult.add(new SimpleEntry<String, String>(label, STRING_PARAM));
            parametersResult.add(new SimpleEntry<String, String>(value, STRING_PARAM));
        } else if (attributeType.equals(STRING_PARAM)) {
            sb.append(sqlFragmentForFileAttr);
            sb.append(label);
            sb.append(" NOT ~~* ?");
            parametersResult.add(new SimpleEntry<String, String>(value, attributeType));
        } else {
            sb.append(sqlFragmentForFileAttr);
            sb.append(label);
            sb.append(" <> ?");
            parametersResult.add(new SimpleEntry<String, String>(value, attributeType));
        }

        return removeLeadingConstraint(s);
    }

    private String correctLabelCase(String label) {
        for (Entry<String, String> entry: fileAttributes.entrySet()) {
            String correctLabel = entry.getKey();
            if (label.equalsIgnoreCase(correctLabel)) {
                return correctLabel;
            }
        }
        return label;
    }


    private String moveExperimentNotStatement(String s, StringBuilder sb)
            throws IOException {

        if (sb.length() > 0) {
            sb.append(INTERSECT);
        }
        s = s.substring(NOT.length());
        return moveNegatedExperimentConstraint(s, sb);
    }


    private String moveNegatedExperimentConstraint(String s, StringBuilder sb)
            throws IOException {

        SimpleEntry<String, String> labelValuePair = getLabelValuePair(s);
        String label = labelValuePair.getKey();
        String value = labelValuePair.getValue();

        if (label.equalsIgnoreCase("expid")) {
            sb.append(sqlFragmentForExpIdSearchNegated);
            parametersResult.add(new SimpleEntry<String, String>(value, STRING_PARAM));
        } else {
            sb.append(sqlFragmentForExpSearchNegated);
            parametersResult.add(new SimpleEntry<String, String>(label, STRING_PARAM));
            parametersResult.add(new SimpleEntry<String, String>(value, STRING_PARAM));
        }

        return removeLeadingConstraint(s);
    }


    private String moveExperimentConstraint(String s, StringBuilder sb)
            throws IOException {

        SimpleEntry<String, String> labelValuePair = getLabelValuePair(s);
        String label = labelValuePair.getKey();
        String value = labelValuePair.getValue();

        if (label.equalsIgnoreCase("expid")) {
            sb.append(sqlFragmentForExpIdSearch);
            parametersResult.add(new SimpleEntry<String, String>(value, STRING_PARAM));
        } else {
            sb.append(sqlFragmentForExpSearch);
            parametersResult.add(new SimpleEntry<String, String>(label, STRING_PARAM));
            parametersResult.add(new SimpleEntry<String, String>(value, STRING_PARAM));
        }

        return removeLeadingConstraint(s);
    }


    private String moveConstraint(String s, StringBuilder sb)
            throws IOException {

        SimpleEntry<String, String> labelValue = getLabelValuePair(s);
        String label = labelValue.getKey();
        String value = labelValue.getValue();

        String attributeType;
        if (label.equalsIgnoreCase("expid")) {
            attributeType = STRING_PARAM;
        } else {
            label = correctLabelCase(label);
            attributeType = fileAttributes.get(label);
        }

        if (attributeType == null) {
            sb.append(sqlFragmentForExpAttrInFileSearch);
            parametersResult.add(new SimpleEntry<String, String>(label, STRING_PARAM));
            parametersResult.add(new SimpleEntry<String, String>(value, STRING_PARAM));
        } else if (attributeType.equals(STRING_PARAM)) {
            sb.append(sqlFragmentForFileAttr);
            sb.append(label);
            sb.append(" ~~* ?");
            parametersResult.add(new SimpleEntry<String, String>(value, attributeType));
        } else {
            sb.append(sqlFragmentForFileAttr);
            sb.append(label);
            sb.append(" = ?");
            parametersResult.add(new SimpleEntry<String, String>(value, attributeType));
        }

        return removeLeadingConstraint(s);
    }


    private String removeLeadingConstraint(String s) {
        return s.substring(s.indexOf(']') + 1);
    }


    private SimpleEntry<String, String> getLabelValuePair(String s)
            throws IOException {
        int leftSqBrIndex = s.indexOf('[');
        int rightSqBrIndex = s.indexOf(']');

        if (leftSqBrIndex == -1 || rightSqBrIndex == -1) {
            throw new IOException("PubMed String is in wrong format");
        }

        String label = s.substring(leftSqBrIndex + 1, rightSqBrIndex);
        String value = s.substring(0, leftSqBrIndex);

        return new SimpleEntry<String, String>(label, value);
    }


    private String moveConj(String s, StringBuilder sb) {
        if (s.toLowerCase().startsWith(OR)) {
            sb.append(UNION);
            return s.substring(OR.length());
        }
        sb.append(INTERSECT);
        return s.substring(AND.length());
    }


    private boolean startsWithConj(String s) {
        return (s.toLowerCase().startsWith(AND) || s.toLowerCase().startsWith(OR));
    }


    private String moveFirstChar(String s, StringBuilder sb) {
        sb.append(s.charAt(0));
        return s.substring(1);
    }


    private boolean startsWithRoundBracket(String s) {
        return (s.charAt(0) == '(' || s.charAt(0) == ')');
    }


    public List<Entry<String, String>> getParameters() {
        return parametersResult;
    }


    public String getQuery() {
        return queryResult;
    }


    public boolean hasFileConstraint(String pubMedString) {

        while (pubMedString.indexOf('[') != -1) {
            pubMedString = pubMedString
                    .substring(pubMedString.indexOf('[') + 1);
            String label = pubMedString.substring(0, pubMedString.indexOf(']'));
            label = correctLabelCase(label);
            if (fileAttributes.containsKey(label)) {
                return true;
            }
        }
        return false;
    }
}
