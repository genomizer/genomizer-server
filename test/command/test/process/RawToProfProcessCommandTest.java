package command.test.process;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.PutProcessCommands;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import org.junit.Test;
import server.RequestHandler;


public class RawToProfProcessCommandTest {
    private final Gson gson = new RequestHandler().getGson();

    /**
     * Test checks that exception can be thrown with an incorrect infile name.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectInfileName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtes&t1.fastq\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        PutProcessCommands processCommands = gson.fromJson(json, PutProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect infile size.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectInfileLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"asdws\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\""+s+"\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        PutProcessCommands processCommands = gson.fromJson(json, PutProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect outfile name.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectOutfileName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"a%wsd\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        PutProcessCommands processCommands = gson.fromJson(json, PutProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect outfile size.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectOutfileLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"asdws\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"2asd2\"," +
                        "\"outfile\":\""+s+"\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        PutProcessCommands processCommands = gson.fromJson(json, PutProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }


    /**
     * Test checks that exception can be thrown with an incorrect GenomeVersion name.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectGenomeVersionName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"the+R\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        PutProcessCommands processCommands = gson.fromJson(json, PutProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect GenomeVersion size.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectGenomeVersionLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"asdws\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"2asd2\"," +
                        "\"outfile\":\"asdwww\",\"genomeVersion\":\""+s+"\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        PutProcessCommands processCommands = gson.fromJson(json, PutProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with params being null.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnParamsNull() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"theR\"," +
                        "\"keepSam\":\"on\"}]}]}";
        PutProcessCommands processCommands = gson.fromJson(json, PutProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}