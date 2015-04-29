package database.containers;

import java.util.Date;

/**
 * Created by nils on 2015-04-29.
 */
public abstract class AbstractFileTuple {
    protected String path;
    protected String inputFilePath;
    protected Date date;
    protected String metaData;
    protected String author;
    protected String uploader;
    protected Boolean isPrivate;
    protected String expId;
    protected String grVersion;
    private String processName;
    private String processVersion;
    private String processFlags;

    public String getPath() {
        return path;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public Date getDate() {
        return date;
    }

    public String getMetaData() {
        return metaData;
    }

    public String getAuthor() {
        return author;
    }

    public String getUploader() {
        return uploader;
    }

    public Boolean isPrivate() {
        return isPrivate;
    }

    public String getExpId() {
        return expId;
    }

    public String getGrVersion() {
        return grVersion;
    }

    public String getProcessName() {
        return processName;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public String getProcessFlags() {
        return processFlags;
    }

    void setPath(String path) {
        this.path = path;
    }

    void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    void setDate(Date date) {
        this.date = date;
    }

    void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    void setUploader(String uploader) {
        this.uploader = uploader;
    }

    void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    void setExpId(String expId) {
        this.expId = expId;
    }

    void setGrVersion(String grVersion) {
        this.grVersion = grVersion;
    }

    void setProcessName(String pName) {
        this.processName = pName;
    }

    void setProcessVersion(String pVersion) {
        this.processVersion = pVersion;
    }

    void setProcessFlags(String pFlags) {
        this.processFlags = pFlags;
    }
}
