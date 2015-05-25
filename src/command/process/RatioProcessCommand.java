package command.process;
/**
 * File:        RatioProcessCommand.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

import com.google.gson.annotations.Expose;

/**
 * TODO class description goes here...
 */
public class RatioProcessCommand extends ProcessCommand {

    @Expose
    private String infile1;

    @Expose
    private String infile2;

    @Override
    public String toString() {
        return "RatioProcessCommand{" +
               "infile1='" + infile1 + '\'' +
               ", infile2='" + infile2 + '\'' +
               '}';
    }
}
