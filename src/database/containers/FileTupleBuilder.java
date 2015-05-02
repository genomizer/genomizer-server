package database.containers;

import java.util.Date;

/**
 * Builder to create a FileTuple object.
 *
 * Created by dv13esn on 2015-04-28.
 */
public class FileTupleBuilder {
    
    private FileTuple product;

    public FileTupleBuilder rawFile(){
        this.product = new FileTuple();
        this.product.setType(FileTuple.Type.Raw);
        return this;
    }

    public FileTupleBuilder profileFile(){
        this.product = new FileTuple();
        this.product.setType(FileTuple.Type.Profile);
        return this;
    }

    public FileTupleBuilder regionFile(){
        this.product = new FileTuple();
        this.product.setType(FileTuple.Type.Region);
        return this;
    }

    public FileTupleBuilder otherFile(){
        this.product = new FileTuple();
        this.product.setType(FileTuple.Type.Other);
        return this;
    }

    public FileTupleBuilder fromType(FileTuple.Type t){
        switch (t) {
            case Raw:
                return this.rawFile();
            case Profile:
                return this.profileFile();
            case Region:
                return this.regionFile();
            case Other:
                return this.otherFile();
        }

        // dummy return
        return null;
    }

    public FileTupleBuilder fromType(String t){
        return this.fromType(FileTuple.Type.fromString(t));
    }

    public FileTupleBuilder fromType(int i){
        return this.fromType(FileTuple.Type.fromInt(i));
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
        FileTuple temp = this.product;
        this.product = null;
        return temp;
    }

    FileTupleBuilder(){

    }
}
