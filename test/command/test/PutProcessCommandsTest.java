package command.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.ValidateException;
import command.process.PutProcessCommand;
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
        String json = "{\"expId\":\"not_an_expid\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\"dbfd\",\"genomeVersion\":\"hg38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
    }

    /**
     * Test can get a ValidateException with incorrect expId name
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateExpIDIncorrectName() throws ValidateException {
        String json = "{\"expId\":\"not_an_£\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\"dbfd\",\"genomeVersion\":\"hg38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

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
        String json = "{\"expId\":\""+s+"\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\"dbfd\",\"genomeVersion\":\"hg38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

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
        String json = "{\"expId\":\"not_an_expid\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest€1.fastq\",\"outfile\":\"dbfd\",\"genomeVersion\":\"hg38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

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
        String json = "{\"expId\":\"not_an_expid\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\""+s+"\",\"outfile\":\"dbfd\",\"genomeVersion\":\"hg38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";


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
        String json = "{\"expId\":\"not_an_expid\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\"dbf£d\",\"genomeVersion\":\"hg38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

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
        String json = "{\"expId\":\"not_an_expid\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\""+s+"\",\"genomeVersion\":\"hg38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect genomeVersion name
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateGenomeVersionIncorrectName() throws ValidateException {
        String json = "{\"expId\":\"not_an_expid\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\"dbfd\",\"genomeVersion\":\"hg$38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }
    /**
     * Test can get a ValidateException with incorrect genomeVersion length
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void testValidateGenomeVersionIncorrectLength() throws ValidateException {
        String s = "";

        for(int i = 0 ; i <MaxLength.FILE_EXPID+1; i++){
            s = s+"A";
        }
        String json = "{\"expId\":\"not_an_expid\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\"awsd\",\"genomeVersion\":\""+s+"\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }


    @Test
    public void testCorrect() throws ValidateException {
        String json = "{\"expId\":\"not_an_expid\",\"processCommands\":[{\"type\":\"bowtie\",\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\"dbfd\",\"genomeVersion\":\"hg38\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";
        PutProcessCommands c = gson.fromJson(json, PutProcessCommands.class);
        c.setFields(null, null, null, UserMethods.UserType.USER);
        c.validate();
    }

}