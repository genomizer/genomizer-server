package database.containers;

import java.io.File;
import java.util.Date;

/**
 * Created by nils on 2015-04-29.
 */
public class FileTupleTemplateBuilder {

    private FileTupleTemplate product;

    private boolean hasUploader;

    public FileTupleTemplateBuilder fileTupleTemplate(){
        product = new FileTupleTemplate();
        return this;
    }

    /**
     *
     * @param path The path to the File
     * @return
     */
    public FileTupleTemplateBuilder withFolderPath(String path) {
        nullCheck("withFolderPath()");
        if(!path.endsWith(File.separator))
            throw new IllegalArgumentException("FileTupleTemplates must point to "
                    + "folders, NOT files!");
        product.setPath(path);
        return this;
    }

    /**
     *
     * @param inputFilePath File path to the Raw input data-file in .fastq-format
     * @return
     */
    public FileTupleTemplateBuilder withInputFilePath(String inputFilePath) {
        nullCheck("withInputFilePath()");
        product.setInputFilePath(inputFilePath);
        return this;
    }


    public FileTupleTemplateBuilder withDate(Date date) {
        nullCheck("withDate()");
        product.setDate(date);
        return this;
    }

    /**
     *
     * @param metaData Specify the parameters used to process the file.
     * @return
     */
    public FileTupleTemplateBuilder withMetaData(String metaData) {
        nullCheck("withMetaData()");
        product.setMetaData(metaData);
        return this;
    }

    public FileTupleTemplateBuilder withAuthor(String author) {
        nullCheck("withAuthor()");
        product.setAuthor(author);
        return this;
    }

    public FileTupleTemplateBuilder withIsPrivate(Boolean isPrivate) {
        nullCheck("withIsPrivate()");
        product.setIsPrivate(isPrivate);
        return this;
    }

    public FileTupleTemplateBuilder withExpId(String expId) {
        nullCheck("withExpId()");
        product.setExpId(expId);
        return this;
    }

    /**
     *
     * @param grVersion Specifies which Genome Release was used to process the file.
     * @return
     */
    public FileTupleTemplateBuilder withGrVersion(String grVersion) {
        nullCheck("withGrVersion()");
        product.setGrVersion(grVersion);
        return this;
    }

    public FileTupleTemplateBuilder withProcessName(String pName) {
        nullCheck("withProcessName()");
        product.setProcessName(pName);
        return this;
    }

    public FileTupleTemplateBuilder withProcessVersion(String pVersion) {
        nullCheck("withProcessVersion()");
        product.setProcessVersion(pVersion);
        return this;
    }

    public FileTupleTemplateBuilder withProcessFlags(String pFlags) {
        nullCheck("withProcessFlags()");
        product.setProcessFlags(pFlags);
        return this;
    }

    public FileTupleTemplate build(){
        nullCheck("build()");
        FileTupleTemplate temp = product;
        this.product = null;
        return temp;
    }

    private void nullCheck(String from) {
        if (product == null) {
            throw new IllegalStateException("FileTupleBuilder: Illegal call to "
                    + from + "; construction must start with " +
                    "FileTupleBuilder.<filetype>File() or " +
                    "FileTupleBuilder.fromType(..)");
        }
    }


}
