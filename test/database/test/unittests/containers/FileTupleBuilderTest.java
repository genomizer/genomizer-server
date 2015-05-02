package database.test.unittests.containers;

import database.containers.FileTuple;
import database.containers.FileTupleBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by nils on 2015-04-28.
 */
public class FileTupleBuilderTest {

    private static FileTupleBuilder fbuild;

    @Before
    public void setUp() {

        fbuild = new FileTupleBuilder();
    }

    @Test
    public void shouldGetNameRight(){
        String name = "AFileName";
        FileTuple f = fbuild.fileTuple().withFilename(name).build();

        assertEquals(name,f.getFilename());
    }

    @Test
    public void shouldGetFilesRight() {
        String inputFP = "/this/is/a/test";
        String filename = "/also/a/test";
        FileTuple f = fbuild.fileTuple().withFilename(filename)
                .withInputFilePath(inputFP).isPrivate(true).build();
        
        assertEquals(inputFP,f.getInputFilePath());
        assertEquals(filename,f.getFilename());
        assertTrue(f.isPrivate());
    }
}



