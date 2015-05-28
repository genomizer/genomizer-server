package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import process.Ratio;
import response.HttpStatusCode;
import process.Smooth;
import response.ProcessResponse;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Class is used to handle smooth processing. The command can include multiple file packages to run one at a time.
 */
public class SmoothingProcessCommand extends ProcessCommand {

    /**
     * Validate to make sure all input from clients is in correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        for (SmoothingFile file : files) {
            Command.validateName(file.getInfile(), MaxLength.FILE_FILENAME, "Infile");
            Command.validateName(file.getOutfile(), MaxLength.FILE_FILENAME, "Outfile");
            if(!file.getMeanOrMedian().equals("mean")&&!file.getMeanOrMedian().equals("median")){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST,
                        "Incorrect meanOrMedian, should be either 'mean' or 'median'.");
            }
            if(file.getMinSmooth()>=file.getWindowSize()){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Incorrect input: minSmooth: "+
                        file.getMinSmooth()+" must be smaller than windowSize: "+file.getWindowSize()+".");
            }
            if(file.getWindowSize()<0){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Incorrect input: windowSize can not be" +
                        "less than 0");
            }
            if(file.getMinSmooth()<0){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Incorrect input: minSmooth can not be" +
                        "less than 0");
            }
       }
    }

//    /**
//     * Run through the list of SmoothingFiles and run processing on each with filePath as parameter.
//     *
//     * @param rawFilesDir
//     * @param profileFilesDir
//     */
//    @Override
//    public void doProcess(String rawFilesDir, String profileFilesDir) {
//        for (SmoothingFile file : files) {
//            file.processFile(filePath);
//        }
//    }

    @Override
    protected Collection<Callable<Response>> getCallables() {
        Collection<Callable<Response>> callables = new ArrayList<>();
        for (SmoothingFile file: files) {
            callables.add(file.getCallable());
        }
        return callables;
    }

    public ArrayList<SmoothingFile> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "SmoothingCommand{" +
                "files=" + files +
                '}';
    }

    @Expose
    private ArrayList<SmoothingFile> files;

    public class SmoothingFile {

        /**
         * Class is used to start a smooth processing with correct parameters.
         */

        @Expose
        private String infile;

        @Expose
        private String outfile;

        @Expose
        private Integer windowSize;

        @Expose
        private String meanOrMedian;

        @Expose
        private Integer minSmooth;


        public String getInfile() {
            return infile;
        }

        public String getOutfile() {
            return outfile;
        }

        public Integer getWindowSize() {
            return windowSize;
        }

        public String getMeanOrMedian() {
            return meanOrMedian;
        }

        public Integer getMinSmooth() {
            return minSmooth;
        }

        @Override
        public String toString() {
            return "SmoothingFile{" +
                    "infile='" + infile + '\'' +
                    ", outfile='" + outfile + '\'' +
                    ", windowSize='" + windowSize + '\'' +
                    ", meanOrMedian='" + meanOrMedian + '\'' +
                    ", minSmooth=" + minSmooth +
                    '}';
        }

        /**
         * Call upon a single smooth processing with correct parameters.
         *
         * @param filePaths
         */
        public void processFile(Map.Entry<String, String> filePaths) {

            String infileWithPath = filePaths.getValue() + infile;
            String outfileWithPath = filePaths.getValue() + outfile;
            Integer meanOrMedian = null;

            if(getMeanOrMedian().equals("mean")){
                meanOrMedian = 0;
            }else if(getMeanOrMedian().equals("median")){
                meanOrMedian = 1;
            }else{
                throw new UnsupportedOperationException("Error during smoothing processing. Incorrect mean/median. " +
                        "Should be either 'mean' or 'median'.");
            }

            try {
                Smooth.runSmoothing(infileWithPath, getWindowSize(), meanOrMedian,  getMinSmooth(), 0 ,0, outfileWithPath);

            } catch (ValidateException e) {
                Debug.log("Error during smoothing processing: "+e.getMessage());
                throw new UnsupportedOperationException("Error during smoothing processing: "+e.getMessage());
            } catch (IOException e) {
                Debug.log("Error during smoothing processing: " + e.getMessage());
                throw new UnsupportedOperationException("Error during smoothing processing: "+e.getMessage());
            } catch (InterruptedException e) {
                Debug.log("Error during smoothing processing: " + e.getMessage());
                throw new UnsupportedOperationException("Error during smoothing processing: "+e.getMessage());
            }
        }

        public Callable<Response> getCallable() {
            return new Callable<Response>() {
                @SuppressWarnings("TryWithIdenticalCatches")
                @Override
                public Response call() throws Exception {
//                    try {
                        processFile(null);
                        return new ProcessResponse(HttpStatusCode.OK);
//                    } catch (ValidateException ve) {
//                        ve.printStackTrace();
//                    } catch (InterruptedException ie) {
//                        ie.printStackTrace();
//                    } catch (IOException ioe) {
//                        ioe.printStackTrace();
//                    }
//                    return new ProcessResponse(HttpStatusCode.INTERNAL_SERVER_ERROR);
                }
            };
        }
    }
}
