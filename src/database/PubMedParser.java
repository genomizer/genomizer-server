package database;

import java.util.ArrayList;

/**
 * Class to parse a PubMed String
 *
 * @author dv11ann
 *
 */
public class PubMedParser {

	private ArrayList<String> values;
	private String whereString;

	/**
	 * Constructor
	 *
	 */
	public PubMedParser() {
		values = new ArrayList<String>();
		whereString = new String();
	}

	/**
	 * Public method to get the parsed PubMed string and its values
	 *
	 * @param pubMed
	 * @return
	 */
	public ParsedPubMed parsePubMed(String pubMed) {
		parse(pubMed);

		return new ParsedPubMed(whereString, values);
	}

	/**
	 * Private method to parse a PubMed string.
	 *
	 * @param pubMed
	 */
	private void parse(String pubMed) {

		StringBuffer totStr = new StringBuffer();
		boolean foundCol = false;

		ArrayList<String> valueList = new ArrayList<String>();

		int startklam = 0;
		int endklam = 0;

		totStr.append(pubMed);
		for(int i = 0; i < totStr.length(); i++) {
			char c = totStr.charAt(i);
			foundCol = false;
			if(c == '[') {
				startklam = i +1;
			}

			if (c == ']') {
				endklam = i;
				foundCol = true;
			}

			if(foundCol) {
				String s = totStr.substring(startklam, endklam);
				int k = startklam;

				while((k <= totStr.length()) && (k > (-1)) && (totStr.charAt(k) != ' ') && totStr.charAt(k) != '(') {
					k--;
				}
				k++;

				valueList.add(totStr.substring(k, startklam -1));
				valueList.add(s);

				totStr.delete(k, endklam +1);
				String appendString = "? = ?";
				totStr.insert(k, appendString);
			}
		}
		values = valueList;
		whereString = totStr.toString();
	}
}