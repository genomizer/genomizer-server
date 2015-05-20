package command.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.ValidateException;
import command.process.PutProcessCommands;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import org.junit.Before;
import org.junit.Test;


public class PutProcessCommandsTest {
    public Gson gson = null;
    @Before
    public void setUp() throws Exception {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();

    }

    /**
     * Test that command can be created with one process
     * @throws ValidateException
     */
    @Test
    public void testOneProcessCommand() throws ValidateException {
        String json = "{\"expId\":\"exp43\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"something\"," +
                "\"outfile\":\"another\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\"hy17\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
    }
    /**
     * Test that command can be created with two processes
     * @throws ValidateException
     */
    @Test
    public void testTwoProcessCommand() throws ValidateException {
        String json = "{\"expId\":\"exp43\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"something\"," +
                "\"outfile\":\"another\",\"params\":\"stuffIsHere\",\"keepSam\":\"true\",\"genomeVersion\":\"hy17\"}" +
                ",{\"type\":\"bowtie\",\"infile\":\"asd\",\"outfile\":\"dsds\",\"params\":\"qqqs\",\"keepSam\":\"off\""+
                ",\"genomeVersion\":\"hy18\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
    }
    /**
     * Test can get a ValidateException with incorrect expId name
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateExpIDIncorrectName() throws ValidateException {
        String json = "{\"expId\":\"exp$\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"something\"," +
                "\"outfile\":\"another\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\"hy17\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect expId length
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateExpIDIncorrectLength() throws ValidateException {
        String s = "";

        for(int i = 0 ; i <MaxLength.EXPID+1; i++){
            s = s+"A";
        }
        String json = "{\"expId\":\""+s+"\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"something\"," +
                "\"outfile\":\"another\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\"hy17\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect infile name
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateInfileIncorrectName() throws ValidateException {
        String json = "{\"expId\":\"exp2\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"something$\"," +
                "\"outfile\":\"another\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\"hy17\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect infile length
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateInfileIncorrectLength() throws ValidateException {
        String s = "";

        for(int i = 0 ; i <MaxLength.FILE_EXPID+1; i++){
            s = s+"A";
        }
        String json = "{\"expId\":\"sasd\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\""+s+"\"," +
                "\"outfile\":\"another\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\"hy17\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect outfile name
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateOutfileIncorrectName() throws ValidateException {
        String json = "{\"expId\":\"exp2\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"something\"," +
                "\"outfile\":\"another@\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\"hy17\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect outfile length
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateOutfileIncorrectLength() throws ValidateException {
        String s = "";

        for(int i = 0 ; i <MaxLength.FILE_EXPID+1; i++){
            s = s+"A";
        }
        String json = "{\"expId\":\"sasd\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"asds\"," +
                "\"outfile\":\""+s+"\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\"hy17\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect outfile name
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateGenomeVersionIncorrectName() throws ValidateException {
        String json = "{\"expId\":\"exp2\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"something\"," +
                "\"outfile\":\"another\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\"hy1£\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect outfile length
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateGenomeVersionIncorrectLength() throws ValidateException {
        String s = "";

        for(int i = 0 ; i <MaxLength.FILE_EXPID+1; i++){
            s = s+"A";
        }
        String json = "{\"expId\":\"sasd\",\"processCommands\":[{\"type\":\"bowtie\",\"infile\":\"asds\"," +
                "\"outfile\":\"asdw\",\"params\":\"stuffIsHere\",\"keepSam\":\"on\",\"genomeVersion\":\""+s+"\"}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
}