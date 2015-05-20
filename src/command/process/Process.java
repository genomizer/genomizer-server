package command.process;

import com.google.gson.annotations.Expose;
import command.ValidateException;
import static command.Command.initDB;

/**
 * Created by dv13jen on 2015-05-19.
 */
public abstract class Process {

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

    public Process(String type, String infile, String outfile, String params, String keepSam, String genomeVersion) {
        this.type = type;
        this.infile= infile;
        this.outfile = outfile;
        this.params = params;
        this.keepSam = keepSam;
        this.genomeVersion = genomeVersion;
    }

    public abstract void runProcess();

}
