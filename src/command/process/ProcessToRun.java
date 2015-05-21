package command.process;

import com.google.gson.annotations.Expose;

/**
 * Created by dv13jen on 2015-05-20.
 */
public class ProcessToRun {

    @Expose
    protected String type = null;

    @Expose
    protected String params = null;

    @Expose
    protected String keepSam = null;


    public ProcessToRun() {

    }

    public String getType() {return type;}

    public String getParams() {
        return params;
    }

    public String getKeepSam() {return keepSam;}

}
