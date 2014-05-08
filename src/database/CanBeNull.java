package database;

public class CanBeNull {

	public final static boolean FILE_PATH = false;
	public final static boolean FILE_FILETYPE = false;
	public final static boolean FILE_FILENAME = false;
	public final static boolean FILE_METADATA = true;
	public final static boolean FILE_AUTHOR = true;
	public final static boolean FILE_UPLOADER = false;
	public final static boolean FILE_ISPRIVATE = false;
	public final static boolean FILE_EXPID = false;
	public final static boolean FILE_GRVERSION = true;

	public final static boolean ANNOTATION_LABEL = false;
	public final static boolean ANNOTATION_DATATYPE = false;
	public final static boolean	ANNOTATION_DEFAULTVALUE = true;
	public final static boolean	ANNOTATION_REQUIRED = false;
	public final static boolean ANNOTATION_VALUE = false;

	public final static boolean EXPID = false;

	public final static boolean USERNAME = false;
	public final static boolean PASSWORD = false;
	public final static boolean ROLE = false;

	public final static boolean WORKSPACEID = false;
	public final static boolean WORKSPACE_NAME = false;
	public final static boolean VPATH = false;

	public final static boolean FILEID = false;
	public final static boolean PUBLISHED_IN_PMID = false;


	public final static boolean GENOME_VERSION = false;
	public final static boolean GENOME_SPECIES = false;
	public final static boolean GENOME_FILEPATH = false;

	public final static boolean CHAIN_FILE_FROMVERSION = false;
	public final static boolean CHAIN_FILE_TOVERSION = false;
	public final static boolean CHAIN_FILE_FILEPATH = false;
}
