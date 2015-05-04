package database.containers;

/**
 * Created by nils on 2015-04-29.
 */
public class FileTupleTemplate extends AbstractFileTuple {

    public FileTupleTemplate() {
    }
/*
    public FileTuple toFileTuple(Integer id,
                                 String type,
                                 String filename,
                                 String uploader) {

        return  FileTuple.makeNew().fromType(type)
                .withAuthor(this.getAuthor())
                .withDate(this.getDate())
                .withGrVersion(this.getGrVersion())
                .withExpId(this.getExpId())
                .withInputFilePath(this.getInputFilePath())
                .withPath(this.getFolderPath() + filename)
                .withMetaData(this.getMetaData())
                .withProcessFlags(this.getProcessFlags())
                .withProcessName(this.getProcessName())
                .withProcessVersion(this.getProcessVersion())
                .withId(id)
                .withIsPrivate(this.isPrivate())
                .withUploader(uploader).build();
    }
*/

    public FileTuple toFileTuple(Integer id,
                                 String filePath,
                                 String inputFilePath) {

        FileTupleBuilder fb = FileTuple.makeNew().fromType(this.getType())
                .withAuthor(this.getAuthor())
                .withDate(this.getDate())
                .withGrVersion(this.getGrVersion())
                .withExpId(this.getExpId())
                .withInputFilePath(inputFilePath)
                .withPath(filePath)
                .withMetaData(this.getMetaData())
                .withProcessFlags(this.getProcessFlags())
                .withProcessName(this.getProcessName())
                .withProcessVersion(this.getProcessVersion())
                .withId(id)
                .withUploader(this.getUploader())
                .withIsPrivate(this.isPrivate());

        for (Integer i : this.getParents()) {
            fb.withParent(i);
        }
        return fb.build();
    }
    public String getFolderPath() {
        return path;
    }

    public Type getType() {
        return this.type;
    }
}
