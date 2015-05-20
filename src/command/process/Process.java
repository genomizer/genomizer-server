package command.process;

import com.google.gson.annotations.Expose;
import command.ValidateException;

import java.util.ArrayList;

import static command.Command.initDB;

/**
 * Created by dv13jen on 2015-05-19.
 */
public abstract class Process {

    @Expose
    protected String type = null;

    @Expose
    protected ArrayList<BowTieProcess> files = new ArrayList<>();


    public Process(String type, ArrayList<BowTieProcess> files) {
        this.type = type;
        this.files = files;
    }

    public abstract void runProcess();

}
