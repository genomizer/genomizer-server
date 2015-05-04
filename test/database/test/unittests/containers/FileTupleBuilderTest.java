package database.test.unittests.containers;

import database.containers.FileTuple;
import database.containers.FileTupleBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Intended to test {@link FileTupleBuilder}.
 *
 * Created by nils on 2015-04-28.
 * @author yakrar / tm08ngn / Nils Gustafsson
 */
public class FileTupleBuilderTest {

    private static String fpath;
    private static String name;
    private static String uploader;

    @BeforeClass
    public static void setUpClass() throws Exception {
        fpath = "/a/folder/";
        name = "AFileName";
        uploader = "file uploadsson";
    }

    @Test
    public void shouldGetNameRight(){

        FileTuple f = FileTuple.makeNew()
                .rawFile()
                .withPath(fpath + name)
                .withDate(new Date())
                .withUploader(uploader)
                .withIsPrivate(false)
                .build();

        assertEquals(name, f.getFileName());
    }

    @Test
    public void shouldGetPathsRight() {
        String inputFP = "/this/is/a/test";
        String filepath = "/also/a/test";
        FileTuple f = FileTuple.makeNew()
                .rawFile()
                .withPath(filepath)
                .withDate(new Date())
                .withInputFilePath(inputFP)
                .withUploader(uploader)
                .withIsPrivate(true)
                .build();

        assertEquals(inputFP,f.getInputFilePath());
        assertEquals(filepath,f.getFullPath());
        assertTrue(f.isPrivate());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRequirePath() throws Exception {
        FileTuple.makeNew()
                .otherFile()
                .withDate(new Date())
                .withUploader(uploader)
                .withIsPrivate(false)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRequireUploader() throws Exception {
        FileTuple.makeNew()
                .otherFile()
                .withDate(new Date())
                .withPath(fpath + name)
                .withIsPrivate(false)
                .build();

    }

    @Test(expected = IllegalStateException.class)
    public void shouldRequireIsPrivateFlag() throws Exception {
        FileTuple.makeNew()
                .profileFile()
                .withUploader(uploader)
                .withPath(fpath + name)
                .withDate(new Date())
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRequireType() throws Exception {
        FileTuple.makeNew().build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldResetAfterBuild() throws Exception {
        FileTupleBuilder ftb = FileTuple.makeNew()
                .rawFile()
                .withPath(fpath + name)
                .withUploader(uploader)
                .withDate(new Date())
                .withIsPrivate(false);

        FileTuple ft = ftb.build();

        // The above is equivalent to
        // the shouldGetNameRight test,
        // and should not fail.
        // Failure should happen here:
        ftb.build();
    }

    @Test
    public void shouldGetFileIDRight() throws Exception {
        int id = 123;
        FileTuple f = FileTuple.makeNew()
                .rawFile()
                .withPath(fpath + name)
                .withDate(new Date())
                .withUploader(uploader)
                .withIsPrivate(false)
                .withId(id).build();

        assertEquals(id, f.getFileId().intValue());


    }

}

