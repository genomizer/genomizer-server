package database.containers;

/**
 * Created by nils on 2015-04-29.
 */
public class FileTupleTemplate extends AbstractFileTuple {

    public FileTupleTemplate() {
    }

    public FileTuple toFileTuple(Integer id,
                                 String type,
                                 String filename,
                                 String status) {

        return  (new FileTupleBuilder()).fileTuple()
                .withAuthor(this.getAuthor())
                .withDate(this.getDate())
                .withGrVersion(this.getGrVersion())
                .withExpId(this.getExpId())
                .withInputFilePath(this.getInputFilePath())
                .withPath(this.getFolderPath() + filename)
                .withFilename(filename)
                .withMetaData(this.getMetaData())
                .withProcessFlags(this.getProcessFlags())
                .withProcessName(this.getProcessName())
                .withProcessVersion(this.getProcessVersion())
                .withStatus(status)
                .withType(type)
                .withId(id)
                .isPrivate(this.isPrivate())
                .withUploader(this.getUploader()).build();
    }

    public String getFolderPath() {
        return path;
    }

}
