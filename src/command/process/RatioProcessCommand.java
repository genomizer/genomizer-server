package command.process;

import com.google.gson.annotations.Expose;
import command.ValidateException;
import response.Response;

import java.util.Map;

/**
 * Dummy class that needs to be implemented. Should handle a ratio processing command.
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
