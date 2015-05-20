package command.process;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by dv13jen on 2015-05-19.
 */
public abstract class Process {

    @Expose
    protected String type = null;

    public Process(String type) {
        this.type = type;
    }

    public abstract void runProcess();

}
