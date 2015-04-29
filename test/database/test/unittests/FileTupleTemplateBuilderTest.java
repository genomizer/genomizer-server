package database.test.unittests;

import database.containers.FileTuple;
import database.containers.FileTupleBuilder;
import database.containers.FileTupleTemplate;
import database.containers.FileTupleTemplateBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by nils on 2015-04-29.
 */
public class FileTupleTemplateBuilderTest {

    private static FileTupleTemplateBuilder fbuild;

    @Before
    public void setUp() {

        fbuild = new FileTupleTemplateBuilder();
    }

    @Test
    public void shouldGetNameRight(){
        String path = "/a/path/";
        FileTupleTemplate f = fbuild.fileTupleTemplate().withPath(path).build();

        assertEquals(path,f.getPath());
    }

    @Test
    public void shouldGetFilesRight() {
        String inputFP = "/this/is/a/test";
        String path = "/also/a/test";
        FileTupleTemplate f = fbuild.fileTupleTemplate().withPath(path)
                .withInputFilePath(inputFP).isPrivate(true).build();

        assertEquals(inputFP,f.getInputFilePath());
        assertEquals(path,f.getPath());
        assertTrue(f.isPrivate());
    }

    @Test
    public void shouldGenerateCorrectFileTuple(){
        String inputFP = "/this/is/a/test";
        String path = "/also/a/test/";
        String name = "aName";
        FileTupleTemplate f = fbuild.fileTupleTemplate().withPath(path)
                .withInputFilePath(inputFP).isPrivate(true).build();

        FileTuple ft = f.toFileTuple(1,"raw",name,"Done");

        assertEquals(inputFP,ft.getInputFilePath());
        assertEquals(path + name,ft.getPath());
        assertTrue(ft.isPrivate());

        assertEquals("raw", ft.getType());
        assertEquals(path + name, ft.getPath());

    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptMalFormedPath(){
        String badpath = "/a/path/to/a/file";
        
        FileTupleTemplate f = fbuild.fileTupleTemplate().withPath(badpath);
    }
}
