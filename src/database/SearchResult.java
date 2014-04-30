package database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for holding resultdata in an arraylist of hashmaps. Every row is a
 * database entry and contains all values and keys.
 *
 * @author yhi04jeo
 */
public class SearchResult {

	private ArrayList<HashMap<String, String>> resultList; //List of Row data
	private int colCount = 0; //Nr of data columns
	private int rowCount = 0; //Nr of rows (results)
	private ArrayList<String> nameList; //Annotations

	/**
	 * Inititates a SearchResult object. Parameters: ResultSet object
	 *
	 * @param ResultSet r
	 * @throws SQLException
	 */
	public SearchResult(ResultSet r) throws SQLException {
		nameList = new ArrayList<String>();

		setResultData(r);
	}

	/**
	 * Returns the list with hashmaps.
	 *
	 * @return ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> getList() {

		return resultList;
	}

	/**
	 * Returns the number of columns(annotations)
	 *
	 * @return int
	 */
	public int getColCount() {

		return colCount;
	}

	/**
	 * Returns the number of rows(search results)
	 *
	 * @return int
	 */
	public int getRowCount() {

		return rowCount;
	}

	/**
	 * Returns the values of a row. Parameters: row number
	 *
	 * @param int rowNr
	 * @return ArrayList<String> or null if failed.
	 */
	public ArrayList<String> getRowValues(int rowNr) {

		ArrayList<String> valuesList = new ArrayList<String>();

		if(rowCount > 0){

			HashMap<String, String> rowData = resultList.get(rowNr);

			for (int i = 0; i < nameList.size(); i++) {
				valuesList.add(rowData.get(nameList.get(i)));
			}
			return valuesList;
		}
		return null;
	}

	public ArrayList<String> getColHeaders() {

		return nameList;
	}

	/**
	 * Returns the value of the annotation field on a row. Paramters: row
	 * number and the annotation. Returns null if table position is incorrect
	 * or list is empty
	 *
	 * @param int rowNr
	 * @param String annotation
	 * @return String value
	 */
	public String getValueByAnnotation(int rowNr, String annotation) {
		String value = null;

		if (isWithinBounds(rowNr, resultList.size())) {
			value = resultList.get(rowNr).get(annotation);
		}

		return value;
	}

	/**
	 * Returns the value of the table by coordinates. Paramters: row
	 * number and column number. Returns null if table position is incorrect
	 * or list is empty
	 *
	 * @param int rowNr
	 * @param int colNr
	 * @return String value
	 */
	public String getValueByIndex(int rowNr, int colNr) {
		String value = null;
		String annotation = null;

		if (resultList != null) {
			if (isWithinBounds(rowNr, resultList.size())) {
				annotation = nameList.get(colNr);
				value = resultList.get(rowNr).get(annotation);
			}
		}

		return value;
	}

	/**
	 * Adds resultdata. An arraylist of hashmaps will be filled
	 * with data. This method must be used before getters can return not
	 * null values. Parameters: ResultSet object
	 *
	 * @param ResultSet r
	 * @throws SQLException
	 */
	private void setResultData(ResultSet r) throws SQLException {

		ResultSetMetaData colNames = r.getMetaData();
		colCount = colNames.getColumnCount();

		resultList = fillList(r, colNames);
        rowCount = resultList.size();

        for (int i = 1; i <= colCount; i++) {
			nameList.add(colNames.getColumnName(i));
		}
	}

	/**
	 * Fills an arrayList with hashmaps with annotations as keys and valus.
	 *
	 * @param ResultSet r
	 * @param ResultSetMetaData colNames
	 * @return ArrayList<HashMap<String, String>>
	 * @throws SQLException
	 */
	private ArrayList<HashMap<String, String>> fillList(ResultSet r,
			ResultSetMetaData colNames) throws SQLException {

		HashMap<String, String> rowResult;
		ArrayList<HashMap<String, String>> list =
				new ArrayList<HashMap<String, String>>();

        while (r.next()) {
        	rowResult = new HashMap<String, String>();

        	for (int i = 0; i < colCount; i++) {
        		rowResult.put(colNames.getColumnLabel(i + 1),
        				r.getString(i + 1));
        	}

            list.add(rowResult);
        }

        return list;
	}

	/**
	 * Method for checking that an index is within bounds of the list.
	 *
	 * @param index
	 * @param size
	 * @return
	 */
	private boolean isWithinBounds(int index, int size) {

		boolean isWithin = false;

		if (rowCount > 0 && index >= 0 && index < size) {
			isWithin = true;
		}

		return isWithin;
	}

}
