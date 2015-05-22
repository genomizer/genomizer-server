package command.process;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class ProcessCommands_new {

    @Expose
    private String expId;

    @Expose
    private ArrayList<ProcessCommand> processCommands;

    public String getExpId() {
        return expId;
    }

    public ArrayList<ProcessCommand> getProcessCommands() {
        return processCommands;
    }

    @Override
    public String toString() {
        return "ProcessCommands_new{" +
               "expId='" + expId + '\'' +
               ", processCommands=" + processCommands +
               '}';
    }
}
