package command.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.Command;
import command.ConvertFileCommand;
import command.ValidateException;
import database.constants.MaxLength;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests Convertfilecommand
 *
 * @author dv13thg
 * @version 1.0
 */

public class ConvertFileCommandTest {

    public Gson gson = null;

    /**
     * Setup method to initiate gson builder.
     */
    @Before
    public void setUp() {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();
    }

    /**
     * Used to test that ValidateException is thrown if
     * format is an empty string.
     *
     * @throws command.ValidateException
     */
    @Test(expected = ValidateException.class)
    public void testValidateFormatEmptyString() throws ValidateException {
        String json = "{\"fileid\":\"255\",\"toformat\":\"\"}";
        final Command convertFileCommand = gson.fromJson(json, ConvertFileCommand.class);
        convertFileCommand.validate();
    }

    /**
     * Used to test that ValidateException is thrown if
     * file id is an empty string.
     *
     * @throws command.ValidateException
     */
    @Test(expected = ValidateException.class)
    public void testValidateFileIDEmptyString() throws ValidateException {
        String json = "{\"fileid\":\"\",\"toformat\":\"wig\"}";
        final Command convertFileCommand = gson.fromJson(json, ConvertFileCommand.class);
        convertFileCommand.validate();
    }

    /**
     * Test used to check that ValidateException is thrown when
     * format is null.
     *
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void testValidateFormatNull() throws ValidateException {
        String json = "{\"fileid\":\"255\"}";
        final Command convertFileCommand = gson.fromJson(json, ConvertFileCommand.class);
        convertFileCommand.validate();
    }

    /**
     * Test used to check that ValidateException is thrown when
     *  file id is null.
     *
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void testValidateFileIDNull() throws ValidateException {
        String json = "{\"toformat\":\"wig\"}";
        final Command convertFileCommand = gson.fromJson(json, ConvertFileCommand.class);
        convertFileCommand.validate();
    }

    /**
     * Test used to check that ValidateException is
     * when format length is too long
     *
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateInvalidFormatLength() throws ValidateException {
        String format = "";
        for(int i = 0; i < MaxLength.FILE_FILETYPE + 1; i++) {
            format += "i";
        }
        String json = "{\"fileid\":\"255\",\"toformat\":\""+format+"\"}";
        final Command convertFileCommand = gson.fromJson(json, ConvertFileCommand.class);
        convertFileCommand.validate();
    }

    /**
     * Test used to check that ValidateException is
     * when fileid length is too long
     *
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateInvalidFileIDLength() throws ValidateException {
        String fileID = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME+ 1; i++) {
            fileID += "i";
        }
        String json = "{\"fileid\":\""+fileID+"\",\"toformat\":\"wig\"}";
        final Command convertFileCommand = gson.fromJson(json, ConvertFileCommand.class);
        convertFileCommand.validate();
    }

    /**
     * Test used to check that validate does not throw
     * exception when json is properly formatted.
     *
     * @throws ValidateException
     */
    @Test
    public void testValidateProperlyFormatted() throws ValidateException {
        String json = "{\"fileid\":\"255\",\"toformat\":\"wig\"}";
        final Command convertFileCommand = gson.fromJson(json, ConvertFileCommand.class);
        convertFileCommand.validate();
    }
}