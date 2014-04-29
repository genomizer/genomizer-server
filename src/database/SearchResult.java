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
	private ArrayList<String> nameList = new ArrayList<String>(); //Annotations
	private ResultSetMetaData colNames;

	/**
	 * Inititates a SearchResult object.
	 */
	public SearchResult() {
		resultList = new ArrayList<HashMap<String, String>>();
	}

	/**
	 * Adds resultdata. An arraylist of hashmaps will be filled
	 * with data. This method must be used before getters can return not
	 * null values. Parameters: ResultSet object
	 *
	 * @param ResultSet r
	 * @throws SQLException
	 */
	public void setResultData(ResultSet r) throws SQLException {
		colNames = r.getMetaData();
		colCount = colNames.getColumnCount();
		HashMap<String, String> rowResult;

        while (r.next()) {
        	rowResult = new HashMap<String, String>();

        	for (int i = 0; i < colCount; i++) {
        		rowResult.put(colNames.getColumnLabel(i + 1),
        				r.getString(i + 1));
        	}

            resultList.add(rowResult);
        }
        rowCount = resultList.size();

        for (int i = 1; i <= colCount; i++) {
			nameList.add(colNames.getColumnName(i));
		}
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

	/**
	 * Returns an arraylist of annotations; column headers.
	 *
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getColHeaders() {
		return nameList;
	}
}
