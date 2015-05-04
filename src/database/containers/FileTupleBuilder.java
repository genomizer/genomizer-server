package database.containers;

import java.util.Date;

/**
 * Builder to create a FileTuple object.
 *
 * Created by dv13esn on 2015-04-28.
 */
public class FileTupleBuilder {
    
    private FileTuple product;

    private boolean hasPath;
    private boolean hasUploader;
    private boolean hasIsPrivate;

    public FileTupleBuilder rawFile(){
        if (product == null) {
            this.product = new FileTuple();
        }

        this.product.setType(FileTuple.Type.Raw);
        return this;
    }

    public FileTupleBuilder profileFile(){
        if (product == null) {
            this.product = new FileTuple();
        }
        this.product.setType(FileTuple.Type.Profile);
        return this;
    }

    public FileTupleBuilder regionFile(){
        if (product == null) {
            this.product = new FileTuple();
        }
        this.product.setType(FileTuple.Type.Region);
        return this;
    }

    public FileTupleBuilder otherFile(){
        if (product == null) {
            this.product = new FileTuple();
        }
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
        nullCheck("withId()");
        product.setFileID(id);
        return this;
    }

    /**
     *
     * @param path The path to the File
     * @return
     */
    public FileTupleBuilder withPath(String path) {
        nullCheck("withPath()");
        product.setPath(path);
        this.hasPath = true;
        return this;
    }

    /**
     *
     * @param inputFilePath File path to the Raw input data-file in .fastq-format
     * @return
     */
    public FileTupleBuilder withInputFilePath(String inputFilePath) {
        nullCheck("withInputFilePath()");
        product.setInputFilePath(inputFilePath);
        return this;
    }

    public FileTupleBuilder withDate(Date date) {
        nullCheck("withDate()");
        product.setDate(date);
        return this;
    }

    /**
     *
     * @param metaData Specify the parameters used to process the file.
     * @return
     */
    public FileTupleBuilder withMetaData(String metaData) {
        nullCheck("withMetaData()");
        product.setMetaData(metaData);
        return this;
    }

    public FileTupleBuilder withAuthor(String author) {
        nullCheck("withAuthor()");
        product.setAuthor(author);
        return this;
    }

    public FileTupleBuilder withUploader(String uploader) {
        nullCheck("withUploader()");
        product.setUploader(uploader);
        this.hasUploader = true;
        return this;
    }

    public FileTupleBuilder withIsPrivate(Boolean isPrivate) {
        nullCheck("withIsPrivate()");
        product.setIsPrivate(isPrivate);
        this.hasIsPrivate = true;
        return this;
    }

    public FileTupleBuilder withExpId(String expId) {
        nullCheck("withExpId()");
        product.setExpId(expId);
        return this;
    }

    /**
     *
     * @param grVersion Specifies which Genome Release was used to process the file.
     * @return
     */
    public FileTupleBuilder withGrVersion(String grVersion) {
        nullCheck("withGrVersion()");
        product.setGrVersion(grVersion);
        return this;
    }

    /**
     *
     * @param status Set the status of the file (Default: "In progress")
     * @return
     */
    public FileTupleBuilder withStatus(String status) {
        nullCheck("withStatus()");
        product.setStatus(status);
        return this;
    }

    public FileTupleBuilder withProcessName(String pName) {
        nullCheck("withProcessName()");
        product.setProcessName(pName);
        return this;
    }

    public FileTupleBuilder withProcessVersion(String pVersion) {
        nullCheck("withProcessVersion()");
        product.setProcessVersion(pVersion);
        return this;
    }

    public FileTupleBuilder withProcessFlags(String pFlags) {
        nullCheck("withProcessFlags()");
        product.setProcessFlags(pFlags);
        return this;
    }

    public FileTuple build(){
        nullCheck("build()");
        if (!hasPath)
            fail("no path specified");
        if (!hasUploader)
            fail("no uploader specified");
        if (!hasIsPrivate)
            fail("withIsPrivate flag not set");
        FileTuple temp = this.product;
        this.product = null;

        // reset state

        this.hasPath = false;
        this.hasUploader = false;
        this.hasIsPrivate = false;

        return temp;
    }

    FileTupleBuilder(){
        this.hasPath = false;
        this.hasUploader = false;
        this.hasIsPrivate = false;
    }

    private void nullCheck(String from) {
        if (product == null) {
            throw new IllegalStateException("FileTupleBuilder: Illegal call to "
                    + from + "; construction must start with " +
                    "FileTupleBuilder.<filetype>File() or " +
                    "FileTupleBuilder.fromType(..)");
        }
    }

    private void fail(String reason) throws IllegalStateException {
        throw new IllegalStateException("FileTupleBuilder: Cannot instatiate " +
                "FileTuple; " + reason);
    }

}
