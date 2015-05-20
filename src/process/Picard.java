package process;
/**
 * File:        Picard.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-20
 */

import com.sun.corba.se.spi.activation.Server;
import command.ValidateException;
import server.ServerSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * TODO class description goes here...
 */
public class Picard extends Executor{
    private final String command;
    private final String inFile;
    private final String outFile;
    private final String inFormat;
    private final String outFormat;
    private final String [] params;

    public Picard(String command, String inFile, String outFile,
                  String inFormat, String outFormat, String [] params) {
        this.command    = command;
        this.inFile     = inFile;
        this.outFile    = outFile;
        this.inFormat   = inFormat;
        this.outFormat  = outFormat;
        this.params     = params;


    }

    public void validate() throws ValidateException {
        if (!(command.equals("MarkDuplicates") || command.equals("SortSam"))) {
            throw new ValidateException(1, "Unsupported command");
        }

     /*   if(!new File(inFile).exists()) {
            throw new ValidateException(1, "Input file ["+
                                           inFile+"] does not exist");
        }
*/
    }

    public String execute() throws IOException, InterruptedException {
        ArrayList<String>args = new ArrayList<>();
        args.add("java");
        args.add("-jar");
        args.add(ServerSettings.picardLocation +"/picard.jar");
        args.add(command);
        args.add("I="+inFile);
        args.add("O="+outFile);
        for (String param : params) {
            args.add(param);
        }

        return executeProgram(args.toArray(new String []{}));
    }

    public static String runRemoveDuplicates(String inFile, String outFile)
            throws ValidateException, IOException, InterruptedException {
        Picard markDuplicates = new Picard("MarkDuplicates", inFile, outFile,
                ".sam", ".sam",
                new String[]{"REMOVE_DUPLICATES=true", "METRICS_FILE=/dev/null"});

        markDuplicates.validate();
        markDuplicates.execute();

        return outFile;

    }

    public static String runSortSam(String inFile, String outFile)
            throws ValidateException, IOException, InterruptedException {
        Picard sortSam = new Picard("SortSam", inFile, outFile,".sam", ".sam",
                new String[]{"SO=coordinate"});
        sortSam.validate();
        sortSam.execute();

       return outFile;
    }


}
