package command.test.process;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.ProcessCommands;
import database.subClasses.UserMethods;
import org.junit.Test;
import server.RequestHandler;


public class StepProcessCommandTest {
    private final Gson gson = new RequestHandler().getGson();

    @Test
    public void canCreateWithCorrectInput() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"step\"," +
                        "\"files\":[{\"infile\":\"bigte1.wig\"," +
                        "\"outfile\":\"awe\",\"stepSize\":\"3\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}