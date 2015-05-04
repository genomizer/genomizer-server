package database.containers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nils on 2015-04-29.
 */
public abstract class AbstractFileTuple {

    public enum Type {
        Raw(0), Profile(1), Region(2), Other(3);

        public final int val;

        Type(int i) {
            this.val = i;
        }

        public static Type fromInt(int i) {
            for (Type t : Type.values()) {
                if (t.val == i) {
                    return t;
                }
            }
            throw new IllegalArgumentException("Unrecongized filetype!");
        }

        public static Type fromString(String s) {
            for (Type t : Type.values()) {
                if (s.equalsIgnoreCase(t.name())) {
                    return t;
                }
            }
            throw new IllegalArgumentException("Unrecognized file type!");
        }
    }


    protected Type type;
    protected String path;
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

    private List<Integer> parents = new ArrayList<>();



    /**
     * Gets the timestamp associated with this tuple.
     * @return a Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the metadata (actually the process flags right now).
     * @return metadata as a string
     */
    public String getMetaData() {
        return metaData;
    }

    /**
     * Author getter.
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Uploader getter.
     * @return the uploader
     */
    public String getUploader() {
        return uploader;
    }

    /**
     * Determines whether this file is private.
     * @return a boolean
     */
    public Boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Gets the associated experiment id.
     * @return expid
     */
    public String getExpId() {
        return expId;
    }

    /**
     * Gets the associated genome release version.
     * @return a genome release version string
     */
    public String getGrVersion() {
        return grVersion;
    }

    /**
     * Gets the name of the process that generated this file (if applicable).
     * @return process name
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Gets the version number/string of the process that generated this file (if applicable).
     * @return the process version
     */
    public String getProcessVersion() {
        return processVersion;
    }

    /**
     * Gets the process flags.
     * @return flags as a string.
     */
    public String getProcessFlags() {
        return processFlags;
    }

    /**
     * Gets the path of the folder containing the file associated with this tuple.
     * @return a path
     */
    public String getFolderPath() {
        int filenameIndex = path.lastIndexOf(File.separator);
        return path.substring(0, filenameIndex + 1);
    }

    /**
     * Gets a {@linkplain List} of fileid corresponding to the
     * "parents" of this file (if applicable).
     * @return a list of integers
     */
    public List<Integer> getParents() {
        return parents;
    }


    void setType(Type type) {
        this.type = type;

    }

    void setPath(String path) {
        this.path = path;
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

    /**
     * Add a new parent to this tuple.
     * @param newParentId the parent to add
     */
    public void addParent(Integer newParentId) {
        this.parents.add(newParentId);
    }
}
