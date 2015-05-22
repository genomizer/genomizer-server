package command.process;
/**
 * File:        BowtieProcessCommand.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * TODO class description goes here...
 */
public class BowtieProcessCommand extends ProcessCommand {

    @Expose
    protected ArrayList<BowtieProcessFile> files;

    public class BowtieProcessFile {

        @Expose
        protected String infile;

        @Expose
        protected String outfile;

        @Expose
        protected String params;

        @Expose
        protected String genomeVersion;

        @Expose
        protected boolean keepSam;

        public String getInfile() {
            return infile;
        }

        public String getOutfile() {
            return outfile;
        }

        public String getParams() {
            return params;
        }

        public String getGenomeVersion() {
            return genomeVersion;
        }

        public boolean shouldKeepSam() {
            return keepSam;
        }

        @Override
        public String toString() {
            return "BowtieProcessFile{" +
                   "infile='" + infile + '\'' +
                   ", outfile='" + outfile + '\'' +
                   ", params='" + params + '\'' +
                   ", genomeVersion='" + genomeVersion + '\'' +
                   ", keepSam=" + keepSam +
                   '}';
        }
    }

    public ArrayList<BowtieProcessFile> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "BowtieProcessCommand{" +
               "files=" + files +
               '}';
    }
}
