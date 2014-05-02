package database;

import java.util.ArrayList;
/**
 * Data holder object
 * @author dv11ann
 *
 */
public class ParsedPubMed {

	private String whereString;
	private ArrayList<String> values;

	public ParsedPubMed(String whereString, ArrayList<String> values) {
		this.whereString = whereString;
		this.values = values;
	}

	public String getWhereString() {
		return whereString;
	}

	/**
	 * Returns a array list of values to be mapped to a prepared statement.
	 * @return an arraylist of strings
	 */
	public ArrayList<String> getValues() {
		return values;
	}
}