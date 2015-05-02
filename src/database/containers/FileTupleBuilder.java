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

    /**
     *
     * @param inputFilePath File path to the Raw input data-file in .fastq-format
     * @return
     */
    public FileTupleBuilder withInputFilePath(String inputFilePath) {
        product.setInputFilePath(inputFilePath);
        return this;
    }

    /**
     *
     * @param type The type of data. Can be "Raw", "Profile", etc.
     * @return
     */
    public FileTupleBuilder withType(FileTuple.Type type) {
        product.setType(type);
        return this;
    }

    public FileTupleBuilder withType(String str) {
        return this.withType(FileTuple.Type.fromString(str));
    }

    public FileTupleBuilder withFilename(String filename) {
        product.setFilename(filename);
        return this;
    }

    public FileTupleBuilder withDate(Date date) {
        product.setDate(date);
        return this;
    }

    /**
     *
     * @param metaData Specify the parameters used to process the file.
     * @return
     */
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

    public FileTupleBuilder isPrivate(Boolean isPrivate) {
        product.setIsPrivate(isPrivate);
        return this;
    }

    public FileTupleBuilder withExpId(String expId) {
        product.setExpId(expId);
        return this;
    }

    /**
     *
     * @param grVersion Specifies which Genome Release was used to process the file.
     * @return
     */
    public FileTupleBuilder withGrVersion(String grVersion) {
        product.setGrVersion(grVersion);
        return this;
    }

    /**
     *
     * @param status Set the status of the file (Default: "In progress")
     * @return
     */
    public FileTupleBuilder withStatus(String status) {
        product.setStatus(status);
        return this;
    }

    public FileTupleBuilder withProcessName(String pName) {
        product.setProcessName(pName);
        return this;
    }

    public FileTupleBuilder withProcessVersion(String pVersion) {
        product.setProcessVersion(pVersion);
        return this;
    }

    public FileTupleBuilder withProcessFlags(String pFlags) {
        product.setProcessFlags(pFlags);
        return this;
    }

    public FileTuple build(){
        return product;
    }

}
