package process.test;

import static org.junit.Assert.*;

import command.ValidateException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import process.Pyicos;
import server.ServerSettings;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class PyicosTest {

    private static final String resourcesDir = "resources/pyicosTestData/";

    private static File inSamFile;
    private static File outWigFile;

    @BeforeClass
    public static void setupBeforeClass() throws IOException {
        inSamFile = new File(resourcesDir + "test.sam");
        outWigFile = new File(Util.replaceExtension(inSamFile.toString(), ".wig"));

        ServerSettings.pyicosLocation = new File("resources/pyicoteo/pyicos").getCanonicalPath();
    }

    @AfterClass
    public static void teardownAfterClass() throws IOException {
        try {
            Files.delete(outWigFile.toPath());
        }
        catch (NoSuchFileException ex) {
            // ignore
        }
    }

    @Test
    public void strcorrShouldSucceed()
            throws ValidateException, InterruptedException, IOException {
        int res = Pyicos.runStrcorr(inSamFile.getCanonicalPath());
        assertEquals(35, res);
    }

    @Test
    public void convertShouldSucceed()
            throws ValidateException, InterruptedException, IOException {
        String res = Pyicos.runConvert(inSamFile.getCanonicalPath(),
                outWigFile.getCanonicalPath());
        assertTrue(outWigFile.exists());
        assertTrue(new File(res).exists());
    }

}
