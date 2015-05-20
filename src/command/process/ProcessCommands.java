package command.process;

import com.google.gson.annotations.Expose;

/**
 * Created by dv13jen on 2015-05-19.
 */
public class ProcessCommands {
    @Expose
    protected String type = null;

    @Expose
    protected String infile = null;

    @Expose
    protected String outfile = null;

    @Expose
    protected String params = null;

    @Expose
    protected String keepSam = null;

    @Expose
    protected String genomeVersion = null;

    public String getType() {
        return type;
    }

    public String getInfile() {
        return infile;
    }

    public String getOutfile() {
        return outfile;
    }

    public String getParams() {return params;}

    public String getKeepSam() {
        return keepSam;
    }

    public String getGenomeVersion() {
        return genomeVersion;
    }

}
