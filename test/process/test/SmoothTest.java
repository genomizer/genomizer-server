package process.test;

import command.ValidateException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import process.Smooth;
import util.PathUtils;

import java.io.File;
import java.io.IOException;

public class SmoothTest {

    @Test
    public void testSmoothing()
            throws ValidateException, IOException, InterruptedException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String outDir = PathUtils.join(tmpDir, "smoothOutDir/");
        try {
            new File(outDir).mkdirs();

            Smooth.runSmoothing("resources/smoothTestData/SGR-testdata-2.sgr", 1, 1, 1, 0, 0,
                    PathUtils.join(outDir, "SGR-testdata-2-smoothed.sgr"));
            Smooth.runSmoothing("resources/smoothTestData/SGR-testdata-3.sgr", 2, 1, 1, 0, 0,
                    PathUtils.join(outDir, "SGR-testdata-3-smoothed.sgr"));
            Smooth.runSmoothing("resources/smoothTestData/SGR-testdata-4.sgr", 2, 1, 1, 0, 0,
                    PathUtils.join(outDir, "SGR-testdata-4-smoothed.sgr"));

            File [] finalFiles = new File(outDir).listFiles();
            final int numFilesInOutDir = finalFiles.length;

            assertEquals(3, numFilesInOutDir);

            for (File file : finalFiles) {
                assertTrue(file.length() > 0);
            }
        }
        finally {
            FileUtils.deleteDirectory(new File("resources/smoothTestData/smoothed"));
            FileUtils.deleteDirectory(new File(outDir));
        }
    }
}
