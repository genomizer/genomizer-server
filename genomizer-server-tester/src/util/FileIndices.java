package util;

import api.commands.CommandTester;
import model.ErrorLogger;
import requests.RetrieveExperimentRequest;
import responses.ResponseParser;

import java.util.ArrayList;

/**
 * Class which changes the index of the current file.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class FileIndices {

    private static ArrayList<String> fileID = new ArrayList<>();

    /**
     * Gets all the files and adds the references for them.
     * @param key The experiment to get the file from.
     */
    public FileIndices(String key){

        fileID = new ArrayList<>();

        try {
            CommandTester.conn.sendRequest(new RetrieveExperimentRequest(key),
                    CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {

                ExperimentData ed = ResponseParser.parseRetrieveExp(CommandTester.conn.getResponseBody());
                for(FileData fd : ed.files) {
                    fileID.add(fd.id);
                }
            }
        } catch (RequestException e) {
            ErrorLogger.log(e.getMessage());
        }
    }

    /**
     * Returns the number of files in the experiment.
     * @return The number of files in the experiment.
     */
    public static int getSize(){
        return fileID.size();
    }


    /**
     * Returns the file-id for the file at the given index.
     * @param index The index for the file to get.
     * @return The file-id for the file to get.
     */
    public String getFileID(int index){

        if (fileID.isEmpty())
            return "";

        return fileID.get(index);
    }
}
