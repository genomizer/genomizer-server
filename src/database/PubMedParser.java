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

		ArrayList<Integer> logList = new ArrayList<Integer>();
		ArrayList<String> valueList = new ArrayList<String>();

		int startklam = 0;
		int endklam = 0;

		/*
		 * Makes a stringbuffer out of the pubMed string.
		 * Will loop until it finds one '[' and saves the position as
		 * an integer 'startklam', then continues to loop until it
		 * finds a ']' and marks the position as an 'endklam'.
		 */

//		pubMed = pubMed.replace(" AND ", " INTERSECT ");
//		pubMed = pubMed.replace(" OR ", " UNION ");

		totStr.append(pubMed);
		int loopIndex = 0;
		while(totStr.indexOf(" AND ") != (-1)) {
			loopIndex = totStr.indexOf(" AND ");
			totStr.delete(loopIndex, loopIndex + " AND ".length());

			totStr.insert(loopIndex, " INTERSECT ");
			logList.add(loopIndex + " INTERSECT ".length());
			System.out.println(totStr.indexOf(" AND "));
		}
		loopIndex = 0;
		while(totStr.indexOf(" OR ") != (-1)) {

			loopIndex = totStr.indexOf(" OR ");
			totStr.delete(loopIndex, loopIndex + " OR ".length());

			totStr.insert(loopIndex, " UNION ");
			logList.add(loopIndex + " UNION ".length());
			System.out.println(totStr.indexOf(" OR "));
		}
		System.out.println(totStr.toString());
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
System.out.println("innan");
				while((index <= totStr.length()) && (index > (0)) && (!logList.contains(index)) && totStr.charAt(index) != '(') {
					index--;
				}
				if(totStr.charAt(index) == '(') {
					index++;
				}

System.out.println("efter");
				boolean isFileAnno = false;

				for(int j = 0; j < fileAnno.size(); j ++) {
					if(s.equals(fileAnno.get(j))) {
						isFileAnno = true;
					}
				}
				System.out.println("effter 2");
				/*
				 * if the value is "date" it has to be handled differently.
				 *
				 */
				String appendString = null;
				if(isFileAnno) {
//					appendString = "SELECT FileID, Path, FileType, Date, MetaData, Author, Uploader, IsPrivate, ExpID, GRVersion FROM File NATURAL JOIN Annotated_With WHERE (" + s + " = ?)";
					appendString = "SELECT FileID FROM File NATURAL JOIN Annotated_With WHERE (" + s + " = ?)";
					if(s.equals("Date")) {
						valueList.add(s);
					}
				} else {
//					appendString = "SELECT FileID, Path, FileType, Date, MetaData, Author, Uploader, IsPrivate, ExpID, GRVersion FROM File NATURAL JOIN Annotated_With WHERE (Label = ? AND Value = ?)";
					appendString = "SELECT FileID FROM File NATURAL JOIN Annotated_With WHERE (Label = ? AND Value = ?)";
					valueList.add(s);
				}
				System.out.println(index);
				valueList.add(totStr.substring(index, startklam -1));
System.out.println("effter 3");
				totStr.delete(index, endklam + 1);
				int change = appendString.length() - (endklam + 1 - index);
				totStr.insert(index, appendString);
				System.out.println("effter 4");
				for(int k = 0; k < logList.size(); k++) {
					logList.set(k, (logList.get(k) + (change)));
				}
				System.out.println("effter 5");
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
		fileAnno.add("FileType");
		fileAnno.add("Date");
		fileAnno.add("MetaData");
		fileAnno.add("Author");
		fileAnno.add("Uploader");
		fileAnno.add("IsPrivate");
		fileAnno.add("ExpID");
		fileAnno.add("GRVersion");
	}
}