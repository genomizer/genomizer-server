package conversion.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Help class for comparing files in tests.
 * Created 2015-04-29.
 *
 * @author Albin Råstander <c12arr>
 * @author Martin Larsson <dv13mln>
 */
public class ConversionResultCompare {
    /**
     *	Private method that uses checksum to compare two files.
     *	returns true if equal false otherwise.
     */
    public boolean compareFiles(File fileA, File fileB) throws IOException, NoSuchAlgorithmException {
        return (Arrays.equals(fileChecksum(fileA), fileChecksum(fileB)));
    }

    /**
     * 	Private method that calculates a checksum for a file, used to compare
     * 	files.
     *
     * 	returns the checksum in a byte array.
     */
    private byte[] fileChecksum(File inputFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA1");

        FileInputStream fileStream = new FileInputStream(inputFile);

        byte[] dataBytes = new byte[1024];
        int nrOfLines;

        while((nrOfLines = fileStream.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nrOfLines);

        }

        return md.digest();
    }
}
