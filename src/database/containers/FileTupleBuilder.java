package database.containers;

import java.util.Date;

/**
 * Builder to create a FileTuple object.
 *
 * Created by dv13esn on 2015-04-28.
 */
public class FileTupleBuilder {
    
    private FileTuple product;
    
    public FileTupleBuilder fileTuple(){
        product = new FileTuple();
        return this;
    }

    /**
     *
     * @param id The ID of the File contained by the FileTuple.
     * @return
     */
    public FileTupleBuilder withId(Integer id) {
        product.setId(id);
        return this;
    }

    /**
     *
     * @param path The path to the File
     * @return
     */
    public FileTupleBuilder withPath(String path) {
        product.setPath(path);
        return this;
    }

    public FileTupleBuilder withInputFilePath(String inputFilePath) {
        product.setInputFilePath(inputFilePath);
        return this;
    }

    public FileTupleBuilder withType(String type) {
        product.setType(type);
        return this;
    }

    public FileTupleBuilder withFilename(String filename) {
        product.setFilename(filename);
        return this;
    }

    public FileTupleBuilder withDate(Date date) {
        product.setDate(date);
        return this;
    }

    public FileTupleBuilder withMetaData(String metaData) {
        product.setMetaData(metaData);
        return this;
    }

    public FileTupleBuilder withAuthor(String author) {
        product.setAuthor(author);
        return this;
    }

    public FileTupleBuilder withUploader(String uploader) {
        product.setUploader(uploader);
        return this;
    }

    public FileTupleBuilder withIsPrivate(Boolean isPrivate) {
        product.setIsPrivate(isPrivate);
        return this;
    }

    public FileTupleBuilder withExpId(String expId) {
        product.setExpId(expId);
        return this;
    }

    public FileTupleBuilder withGrVersion(String grVersion) {
        product.setGrVersion(grVersion);
        return this;
    }

    public FileTupleBuilder withStatus(String status) {
        product.setStatus(status);
        return this;
    }

    public FileTuple build(){
        return product;
    }
    
}
