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


    @Test
    public void shouldGetNameRight(){
        String name = "AFileName";
        FileTuple f = FileTuple.makeNew()
                .rawFile().withPath(name).build();

        assertEquals(name,f.getFileName());
    }

    @Test
    public void shouldGetPathsRight() {
        String inputFP = "/this/is/a/test";
        String filepath = "/also/a/test";
        FileTuple f = FileTuple.makeNew()
                .rawFile().withPath(filepath)
                .withInputFilePath(inputFP).isPrivate(true).build();
        
        assertEquals(inputFP,f.getInputFilePath());
        assertEquals(filepath,f.getFullPath());
        assertTrue(f.isPrivate());
    }
}



