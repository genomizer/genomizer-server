package database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SearchResult {

	private ArrayList<HashMap<String, String>> resultList;
	private int colCount = 0;
	private int rowCount = 0;
	private ResultSetMetaData colNames;
	private ArrayList<String> nameList = new ArrayList<String>();

	public SearchResult(ResultSet r) throws SQLException {
		//Set of column names
		colNames = r.getMetaData();
		colCount = colNames.getColumnCount();

		//List of rows
		resultList = new ArrayList<HashMap<String, String>>();

		//Row data with column names as keys and values
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

	public void printList() {
		Set<String> keySet;

		for (int i = 0; i < resultList.size(); i++) {
			keySet = resultList.get(0).keySet();
			System.out.println(i + ":");

			for (String key: keySet) {
				System.out.print(key + "= " + resultList.get(i).get(key) + ", ");
			}

			System.out.println();
		}
	}

	public ArrayList<HashMap<String, String>> getList() {
		return resultList;
	}

	public int getColCount() {
		return colCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public ArrayList<String> getRowValues(int rowNr) {
		ArrayList<String> valuesList = new ArrayList<String>();
		HashMap<String, String> rowData = resultList.get(rowNr);
		Set<String> keySet = rowData.keySet();

		for (String key: keySet) {
			valuesList.add(rowData.get(key));
		}

		return valuesList;
	}

	public ArrayList<String> getColHeaders() {
		return nameList;
	}
}
