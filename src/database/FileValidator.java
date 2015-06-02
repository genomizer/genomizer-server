package database;


import command.Command;

public class FileValidator {

	public FileValidator() {}

	public static final String validFileNameChars = "[-_\\w,\\.åäöÅÄÖ()]";
	public static final String validFileExtChars  = "[A-ZÅÄÖa-zåäö0-9]";

	public static boolean checkIsValidFileName(String fileName){
		return !Command.hasInvalidCharacters(fileName);
//		String regex = "^" + validFileNameChars + "+\\." + validFileExtChars + "+$";
//		return fileName.matches(regex);
	}
}