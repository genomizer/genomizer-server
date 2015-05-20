package command.process;

import com.google.gson.annotations.Expose;

/**
 * Created by dv13jen on 2015-05-20.
 */
public class BowTieProcess {
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

    public BowTieProcess(String infile, String outfile, String params, String keepSam, String genomeVersion) {
        this.infile = infile;
        this.outfile = outfile;
        this.params = params;
        this.keepSam = keepSam;
        this.genomeVersion = genomeVersion;
    }

    public String getOutfile() {
        return outfile;
    }

    public String getParams() {
        return params;
    }

    public String getKeepSam() {
        return keepSam;
    }

    public String getGenomeVersion() {
        return genomeVersion;
    }

    public String getInfile() {

        return infile;
    }

    @Override
    public String toString() {
        return "BowTieProcess{" +
                "infile='" + infile + '\'' +
                ", outfile='" + outfile + '\'' +
                ", params='" + params + '\'' +
                ", keepSam='" + keepSam + '\'' +
                ", genomeVersion='" + genomeVersion + '\'' +
                '}';
    }
}
