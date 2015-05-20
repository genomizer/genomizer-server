package command.process;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by dv13jen on 2015-05-19.
 */
public class ProcessCommands {
    @Expose
    protected String type = null;

    @Expose
    protected ArrayList<RawToProfileFiles> files = new ArrayList<>();


    public String getType() {
        return type;
    }

    public ArrayList<RawToProfileFiles> getFiles() {
        return files;
    }
}
