package database.containers;

import java.util.Date;

/**
 * Created by dv13esn on 2015-04-28.
 */
public class FileTupleBuilder {
    
    private FileTuple product;

    private Integer id;
    private String path;
    private String inputFilePath;
    private String type;
    private String filename;
    private Date date;
    private String metaData;
    private String author;
    private String uploader;
    private Boolean isPrivate;
    private String expId;
    private String grVersion;
    private String status;
    
    public FileTupleBuilder fileTuple(){
        product = new FileTuple();
        return this;
    } 

    public FileTupleBuilder setId(Integer id) {
        product.setId(id);
        return this;
    }

    public FileTupleBuilder setPath(String path) {
        product.setPath(path);
        return this;
    }

    public FileTupleBuilder setInputFilePath(String inputFilePath) {
        product.setInputFilePath(inputFilePath);
        return this;
    }

    public FileTupleBuilder setType(String type) {
        product.setType(type);
        return this;
    }

    public FileTupleBuilder setFilename(String filename) {
        product.setFilename(filename);
        return this;
    }

    public FileTupleBuilder setDate(Date date) {
        product.setDate(date);
        return this;
    }

    public FileTupleBuilder setMetaData(String metaData) {
        product.setMetaData(metaData);
        return this;
    }

    public FileTupleBuilder setAuthor(String author) {
        product.setAuthor(author);
        return this;
    }

    public FileTupleBuilder setUploader(String uploader) {
        product.setUploader(uploader);
        return this;
    }

    public FileTupleBuilder setIsPrivate(Boolean isPrivate) {
        product.setIsPrivate(isPrivate);
        return this;
    }

    public FileTupleBuilder setExpId(String expId) {
        product.setExpId(expId);
        return this;
    }

    public FileTupleBuilder setGrVersion(String grVersion) {
        product.setGrVersion(grVersion);
        return this;
    }

    public FileTupleBuilder setStatus(String status) {
        product.setStatus(status);
        return this;
    }

    public FileTuple build(){
        return product;
    }
    
}
