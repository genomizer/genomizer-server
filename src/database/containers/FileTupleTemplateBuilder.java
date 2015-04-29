package database.containers;

import java.util.Date;

/**
 * Created by nils on 2015-04-29.
 */
public class FileTupleTemplateBuilder {

    private FileTupleTemplate product;

    public FileTupleTemplateBuilder fileTupleTemplate(){
        product = new FileTupleTemplate();
        return this;
    }

    /**
     *
     * @param path The path to the File
     * @return
     */
    public FileTupleTemplateBuilder withPath(String path) {
        product.setPath(path);
        return this;
    }

    /**
     *
     * @param inputFilePath File path to the RAW input data-file in .fastq-format
     * @return
     */
    public FileTupleTemplateBuilder withInputFilePath(String inputFilePath) {
        product.setInputFilePath(inputFilePath);
        return this;
    }


    public FileTupleTemplateBuilder withDate(Date date) {
        product.setDate(date);
        return this;
    }

    /**
     *
     * @param metaData Specify the parameters used to process the file.
     * @return
     */
    public FileTupleTemplateBuilder withMetaData(String metaData) {
        product.setMetaData(metaData);
        return this;
    }

    public FileTupleTemplateBuilder withAuthor(String author) {
        product.setAuthor(author);
        return this;
    }

    public FileTupleTemplateBuilder withUploader(String uploader) {
        product.setUploader(uploader);
        return this;
    }

    public FileTupleTemplateBuilder isPrivate(Boolean isPrivate) {
        product.setIsPrivate(isPrivate);
        return this;
    }

    public FileTupleTemplateBuilder withExpId(String expId) {
        product.setExpId(expId);
        return this;
    }

    /**
     *
     * @param grVersion Specifies which Genome Release was used to process the file.
     * @return
     */
    public FileTupleTemplateBuilder withGrVersion(String grVersion) {
        product.setGrVersion(grVersion);
        return this;
    }

    public FileTupleTemplateBuilder withProcessName(String pName) {
        product.setProcessName(pName);
        return this;
    }

    public FileTupleTemplateBuilder withProcessVersion(String pVersion) {
        product.setProcessVersion(pVersion);
        return this;
    }

    public FileTupleTemplateBuilder withProcessFlags(String pFlags) {
        product.setProcessFlags(pFlags);
        return this;
    }

    public FileTupleTemplate build(){
        return product;
    }

}
