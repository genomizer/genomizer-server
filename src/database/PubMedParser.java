package database;

import java.util.ArrayList;

/**
 * Class to parse a PubMed String
 *
 * @author dv11ann
 * Rör för tusan inte din hjäkel, detta är min klass :P get your own!
 */
public class PubMedParser {

	private ArrayList<String> values;
	private String whereString;
	private ArrayList<String> fileAnno;

	/**
	 * Constructor
	 *
	 */
	public PubMedParser() {
		values = new ArrayList<String>();
		whereString = new String();
		makeFileAnno();
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

		/*
		 * Makes a stringbuffer out of the pubMed string.
		 * Will loop until it finds one '[' and saves the position as
		 * an integer 'startklam', then continues to loop until it
		 * finds a ']' and marks the position as an 'endklam'.
		 */
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

			/*
			 * When the ']' is found, it will go back to the startklam
			 * position, and loop back in the string until it finds a
			 * space ' ' or a paranthesis '('. It will use these positions
			 * to cut out the part of the string and replace it with:
			 * (s + " = ?") or ("(Label = ? AND Value = ?)"), dpending on
			 * if the annotation exists in the filetable or if it is an
			 * experiment annotation.
			 *
			 */
			if(foundCol) {
				String s = totStr.substring(startklam, endklam);
				int index = startklam;

				while((index <= totStr.length()) && (index > (-1)) && (totStr.charAt(index) != ' ') && totStr.charAt(index) != '(') {
					index--;
				}
				index++;

				boolean isFileAnno = false;

				for(int j = 0; j < fileAnno.size(); j ++) {
					if(s.equals(fileAnno.get(j))) {
						isFileAnno = true;
					}
				}

				/*
				 * if the value is "date" it has to be handled differently.
				 *
				 */
				String appendString = null;
				if(isFileAnno) {
					appendString = s + " = ?";
					if(s.equals("Date")) {
						valueList.add(s);
					}
				} else {
					appendString = "(Label = ? AND Value = ?)";
					valueList.add(s);
				}

				valueList.add(totStr.substring(index, startklam -1));

				totStr.delete(index, endklam +1);

				totStr.insert(index, appendString);

				i = index + appendString.length();
			}
		}
		values = valueList;
		whereString = totStr.toString();
	}

	/*
	 * The fileannotations, ugly i know.
	 */
	private void makeFileAnno() {
		fileAnno = new ArrayList<String>();
		fileAnno.add("FileID");
		fileAnno.add("Path");
		fileAnno.add("Type");
		fileAnno.add("Date");
		fileAnno.add("MetaData");
		fileAnno.add("Author");
		fileAnno.add("Uploader");
		fileAnno.add("IsPrivate");
		fileAnno.add("ExpID");
		fileAnno.add("GRVersion");
	}
}