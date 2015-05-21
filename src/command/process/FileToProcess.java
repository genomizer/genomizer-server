package command.process;

import com.google.gson.annotations.Expose;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dv13jen on 2015-05-19.
 */
public class FileToProcess {

    @Expose
    protected String infile = null;

    @Expose
    protected String outfile = null;

    @Expose
    protected String genomeVersion = null;

    @Expose
    protected ArrayList<ProcessToRun> processes = new ArrayList<>();

    public FileToProcess(){

    }

    public String getOutfile() {
        return outfile;
    }

    public String getGenomeVersion() {
        return genomeVersion;
    }

    public String getInfile() {return infile;}

    public ArrayList<ProcessToRun> getProcesses() {return processes;}
}
