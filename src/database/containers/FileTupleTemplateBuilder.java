package database.containers;

import java.io.File;
import java.util.Date;

/**
 * Created by nils on 2015-04-29.
 */
public class FileTupleTemplateBuilder {

    private FileTupleTemplate product;

    private boolean hasUploader;


    public FileTupleTemplateBuilder rawFile() {
        if (product == null) {
            this.product = new FileTupleTemplate();
        }

        this.product.setType(FileTuple.Type.Raw);
        return this;
    }

    public FileTupleTemplateBuilder profileFile() {
        if (product == null) {
            this.product = new FileTupleTemplate();
        }
        this.product.setType(FileTuple.Type.Profile);
        return this;
    }

    public FileTupleTemplateBuilder regionFile() {
        if (product == null) {
            this.product = new FileTupleTemplate();
        }
        this.product.setType(FileTuple.Type.Region);
        return this;
    }

    public FileTupleTemplateBuilder otherFile() {
        if (product == null) {
            this.product = new FileTupleTemplate();
        }
        this.product.setType(FileTuple.Type.Other);
        return this;
    }

    public FileTupleTemplateBuilder fromType(FileTuple.Type t) {
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

    public FileTupleTemplateBuilder fromType(String t) {
        return this.fromType(FileTuple.Type.fromString(t));
    }

    public FileTupleTemplateBuilder fromType(int i) {
        return this.fromType(FileTuple.Type.fromInt(i));
    }


    /**
     *
     * @param path The path to the File
     * @return
     */
    public FileTupleTemplateBuilder withFolderPath(String path) {
        nullCheck("withPath()");
        if(!path.endsWith(File.separator))
            throw new IllegalArgumentException("FileTupleTemplates must point to "
                    + "folders, NOT files!");
        product.setPath(path);
        return this;
    }


    public FileTupleTemplateBuilder withDate(Date date) {
        nullCheck("withDate()");
        product.setDate(date);
        return this;
    }

    public FileTupleTemplateBuilder withUploader(String uploader) {
        nullCheck("withUploader()");
        product.setUploader(uploader);
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
