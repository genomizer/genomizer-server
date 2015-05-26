package command.process;
/**
 * File:        RatioProcessCommand.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

import com.google.gson.annotations.Expose;
import command.ValidateException;
import response.Response;

import java.util.Map;

/**
 * TODO class description goes here...
 */
public class RatioProcessCommand extends ProcessCommand {
    @Override
    public void validate() throws ValidateException {

    }

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

    @Override
    public void doProcess(Map.Entry<String, String> filePath) throws UnsupportedOperationException{

    }
}
