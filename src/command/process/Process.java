package command.process;

import com.google.gson.annotations.Expose;
import command.ValidateException;
import static command.Command.initDB;
/**
 + * Created by dv13jen on 2015-05-19.
 + */
public abstract class Process {

    protected String type = null;

    public Process(String type) {
        this.type = type;
        }

    public abstract void runProcess();

}