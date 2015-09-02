package process.test;

import command.ValidateException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import process.SmoothJava;
import util.PathUtils;

import java.io.File;
import java.io.IOException;

public class SmoothJavaTest {

    @Test
    public void testJavaSmoothing()
            throws ValidateException, IOException, InterruptedException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String outDir = PathUtils.join(tmpDir, "smoothJavaOutDir/");
        try {
            new File(outDir).mkdirs();

            SmoothJava.runSmoothing("resources/smoothTestData/SGR-testdata-2.sgr", 2, 1, 1, 0, 0,
                    PathUtils.join(outDir, "SGR-testdata-2-smoothed.sgr"), 1);
            SmoothJava.runSmoothing("resources/smoothTestData/SGR-testdata-3.sgr", 2, 1, 1, 0, 0,
                    PathUtils.join(outDir, "SGR-testdata-3-smoothed.sgr"), 1);
            SmoothJava.runSmoothing("resources/smoothTestData/SGR-testdata-4.sgr", 2, 1, 1, 0, 0,
                    PathUtils.join(outDir, "SGR-testdata-4-smoothed.sgr"), 1);

            File [] finalFiles = new File(outDir).listFiles();
            final int numFilesInOutDir = finalFiles.length;

            assertEquals(3, numFilesInOutDir);

            for (File file : finalFiles) {
                assertTrue(file.length() > 0);
            }
        }
        finally {
            FileUtils.deleteDirectory(new File(outDir));
        }
    }
}