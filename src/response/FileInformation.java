package response;

import java.util.Date;

import database.containers.FileTuple;

/**
 * Class that contains the file information.
 *
 * @author
 * @version 1.0
 */
public class FileInformation {

    public final Integer id;
    public final String path;
    public final String url;
    public final String type;
    public final String filename;
    public final Date date;
    public final String metaData;
    public final String author;
    public final String uploader;
//    public final Boolean isPrivate;
    public final String expId;
    public final String grVersion;


    /**
     * Creates a FileInformation object from a FileTuple object.
     * @param src The FileTuple object to convert from.
     */
    public FileInformation(FileTuple src) {

    	id = src.id;
    	path = src.path;
    	url = src.getDownloadURL();
    	type = src.type;
    	filename = src.filename;
    	date = src.date;
    	metaData = src.metaData;
    	author = src.author;
    	uploader = src.uploader;
//    	isPrivate = src.isPrivate;
    	expId = src.expId;
    	grVersion = src.grVersion;
    }
}
