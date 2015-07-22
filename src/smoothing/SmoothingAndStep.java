package smoothing;


import process.ProcessException;
import server.Debug;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SmoothingAndStep {

    public static void main(String[] args) throws ProcessException, InterruptedException {
        int[] params = new int[5];
        for (int i=0; i<5; ++i) {
            params[i] = Integer.parseInt(args[i]);
        }

        SmoothingAndStep smoothingAndStep = new SmoothingAndStep();
        smoothingAndStep.smoothing(params, args[5], args[6], Integer.parseInt
                (args[7]));
    }


    private double readSumValue;
    private int noOfValues;
    private BufferedWriter bw;
    private BufferedReader br;
    private ArrayList<Tuple> data;
    private int stepSize;
    private int middleIndex;

    /*
     * Parameters:
     * Params:	  An array with 5 integers representing parameters.
     * params[0]: Window Size, the number of signal values that the smoothing
     * 		  should be calculated on.
     * params[1]: Whether the smoothing should be trimmed mean (0) or median (1)
     * params[2]: Minimum numbers to smooth. A number that says how many signal
     * 		  values the program at least need in order to smooth one row.
     * 		  This number must be smaller than windowSize
     * params[3]: Can either be 1 or 0. If 1 the program will calculate the
     * 		  total mean value for all rows and print those.
     * params[4]: Print zeroes. If the program should print rows where the
     * 		  signal value is 0 the flag should be (1), if (0) the program
     * 		  will not print the zeroes.
     *
     * inPath:	  A string that tells the location and name of the source file.
     * outPath:	  A string that tells the location and name of the output file.
     *
     * stepSize:  Flag that tells how much the program should step,
     * 		  1 if the user wants no stepping. Have to be larger than 0.
     *
     *
     *
     * 		Version 1.0.0, approved by Philge. If changes are made to this program
     * 		researchers at EpiCon needs to check the results and approve the new version.
     *
     */
    public double smoothing(int[] params, String inPath, String outPath,
                                 int stepSize) throws ProcessException, InterruptedException {
        validateInput(params, stepSize);

        data = new ArrayList<Tuple>();
        readSumValue = 0;
        noOfValues = 0;
        String strLine;
        this.stepSize = stepSize;
        this.middleIndex = getMiddleIndex(params[0]);

        if(params[2]<=(params[0]/2)){
            params[2] = (params[0]/2);
        }

        try {
            setupBufferReader(inPath);
            setupBufferWriter(outPath);

            for(int i = 0; i<params[0]; i++){

                if((strLine = br.readLine()) != null){
                    while(!addLine(strLine)){
                        strLine = br.readLine();
                    }
                }
            }

            for (int i = 0; i < middleIndex; i++){
                if(data.size() > params[2]){
                    //  }
                    if(((data.size()/2) + i + 2) > params[2]){
                        smoothOneRow(params, (data.size()/2)+i+1, i);
                    }
                }
            }
            smoothOneRow(params,params[0]);

            while((strLine = br.readLine()) != null){
                shiftLeft(strLine, params);
                smoothOneRow(params,params[0]);
            }
            if(data.size() > 0){
                data.remove(0);
            }

            if (params[0]%2==1){
                for (int i = 1;i<(params[0]-params[2]);i++){
                    smoothOneRow(params,params[0]-i);
                    if(data.size() > 0){
                        data.remove(0);
                    }
                }
                if(data.size() > 0){
                    data.remove(0);
                }
            }else{
                for (int i = 1;i<(params[0]-params[2]+1);i++){
                    smoothOneRow(params,params[0]-i);
                    if(data.size() > 0){
                        data.remove(0);
                    }
                }
            }

            for (int j = 0; j < params[0]/2 -1 ;j++){
                if(data.size() > 0){
                    data.remove(0);
                }
            }
            while(data.size()>0){
                data.remove(0);
            }

            tearDown();

        } catch (IOException e) {
            throw new ProcessException("IOException when reading/writing in Smoothing: "+ e.getMessage());
        }
        if(params[3] == 1){
            Debug.log("Total mean for file is: " + (readSumValue / noOfValues));
        }
        return readSumValue/noOfValues;
    }

    private void smoothOneRow(int[] params, int noOfRowsToSmooth, int index) throws IOException, ProcessException {
        if((noOfRowsToSmooth < data.size()+1)){
            if(params[1]==1){
                smoothMedian(noOfRowsToSmooth, params, index);
            } else if(params[1]==0){
                smoothTrimmedMean(noOfRowsToSmooth, params, index);
            }
            writeToFile(params, index);
        } else {
            throw new ProcessException("Error: File is shorter than window size.");
        }
    }

    private void smoothOneRow(int[] params, int noOfRowsToSmooth) throws IOException, ProcessException {
        if((noOfRowsToSmooth < data.size()+1)){
            if(params[1]==1){
                smoothMedian(noOfRowsToSmooth, params);
            } else if(params[1]==0){
                smoothTrimmedMean(noOfRowsToSmooth, params);
            }
            writeToFile(params);
        } else {
            throw new ProcessException("Error: File is shorter than window size.");
        }
    }


    private void smoothTrimmedMean(int noOfRowsToSmooth, int[] params, int index) {
        int minNrSigs = 1;

        double meanSignal = data.get(index).getSignal();
        double maxSig = meanSignal;
        double minSig = meanSignal;

        if(data.size() >= params[2]){
            for(int j = 0 ; j < noOfRowsToSmooth; j++){
                if(j != index){

                    meanSignal = meanSignal + data.get(j).getSignal();
                    minNrSigs++;
                    if(maxSig < data.get(j).getSignal()){
                        maxSig = data.get(j).getSignal();
                    }
                    if(minSig > data.get(j).getSignal()){
                        minSig = data.get(j).getSignal();
                    }
                }
            }
            if(data.get(index).getSignal() != 0){
                calcAndSetMeanSignal(params, minNrSigs, meanSignal, maxSig,
                        minSig, index);
            }
        }
    }

    private void smoothTrimmedMean(int noOfRowsToSmooth, int[] params) {
        int minNrSigs = 1;

        double meanSignal = data.get(middleIndex).getSignal();
        double maxSig = meanSignal;
        double minSig = meanSignal;

        if(data.size() >= params[2]){
            for(int j = 0 ; j < noOfRowsToSmooth; j++){
                if(j != middleIndex){

                    meanSignal = meanSignal + data.get(j).getSignal();
                    minNrSigs++;
                    if(maxSig < data.get(j).getSignal()){
                        maxSig = data.get(j).getSignal();
                    }
                    if(minSig > data.get(j).getSignal()){
                        minSig = data.get(j).getSignal();
                    }
                }
            }
            if(data.get(middleIndex).getSignal() != 0){
                calcAndSetMeanSignal(params, minNrSigs, meanSignal, maxSig,
                        minSig);
            }
        }
    }


    private void smoothMedian(int noOfRowsToSmooth, int[] params, int index) {

        double[] array = new double [noOfRowsToSmooth];

        if(data.size() >= params[2]){
            for(int j = 0 ; j < (noOfRowsToSmooth); j++){
                if(data.size() > j){
                    array[j] = data.get(j).getSignal();
                }
            }
            if(data.get(index).getSignal() != 0){
                data.get(index).setNewSignal(median(array));
            }
        }
    }

    private void smoothMedian(int noOfRowsToSmooth, int[] params) {

        double[] array = new double [noOfRowsToSmooth];

        if(data.size() >= params[2]){
            for(int j = 0 ; j < (noOfRowsToSmooth); j++){
                if(data.size() > j){
                    array[j] = data.get(j).getSignal();
                }
            }
            if(data.get(middleIndex).getSignal() != 0){
                data.get(middleIndex).setNewSignal(median(array));
            }
        }
    }

    private void writeToFile(int[] params) throws IOException {
        if(!((params[4] == 0) && (data.get(middleIndex).getNewSignal() == 0)) ){
            if(data.get(middleIndex).getPosition()%stepSize== 0){
                bw.write(data.get(middleIndex).toString());
            }
        }
        if (params[3] == 1){
            readSumValue = readSumValue+ data.get(middleIndex).getSignal();
            noOfValues++;
        }
    }

    private void shiftLeft(String strLine, int[] params) throws IOException, ProcessException  {
        while(!addLine(strLine)){
            strLine = br.readLine();
        }
        if(data.size() > 0){
            data.remove(0);
        }

        if(!data.get(data.size()-1).getChromosome().equals(data.get(data.size()-2).getChromosome())){
            chromosomeChange(params);
        }
    }

    private void chromosomeChange(int[] params) throws IOException, ProcessException {

        Tuple newChromo = data.get(data.size()-1);
        data.remove(data.size()-1);

        if (params[0]%2==1){
            for (int i = 1;i<(params[0]-params[2]);i++){
                smoothOneRow(params,params[0]-i);
                if(data.size() > 0){
                    data.remove(0);
                }
            }
            if(data.size() > 0){
                data.remove(0);
            }
        }else{
            for (int i = 1;i<(params[0]-params[2]+1);i++){
                smoothOneRow(params,params[0]-i);
                if(data.size() > 0){
                    data.remove(0);
                }
            }
        }
        for (int j = 0; j < params[0]/2 -1 ;j++){
            if(data.size() > 0){
                data.remove(0);
            }
        }

        while(data.size()>0){
            data.remove(0);
        }
        data.add(newChromo);

        String strLine;
        for(int i = 0; i<params[0]-1; i++){
            if((strLine = br.readLine()) != null){
                while(!addLine(strLine)){
                    strLine = br.readLine();
                }
            }
        }
        for (int i = 0; i < middleIndex; i++){
            if(data.size() > params[2]){
                if(((data.size()/2) + i + 2)> params[2]){
                    smoothOneRow(params, (data.size()/2)+i+1, i);
                }
            }
        }
    }

    private void writeToFile(int[] params, int i) throws IOException {
        if(!((params[4] == 0) && (data.get(i).getNewSignal() == 0)) ){
            if(data.get(i).getPosition()%stepSize== 0){
                bw.write(data.get(i).toString());
            }
        }
        if (params[3] == 1){
            readSumValue = readSumValue+ data.get(i).getSignal();
            noOfValues++;
        }
    }

    private boolean addLine(String strLine) {
        try{
            data.add(new Tuple(strLine));
        } catch(Exception e){
            return false;
        }
        return true;
    }

    private void setupBufferWriter(String outPath) throws IOException {
        File outFile = new File(outPath);
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        bw = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile()));
    }

    private void setupBufferReader(String inPath) throws FileNotFoundException{
        br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(inPath))));
    }

    private void calcAndSetMeanSignal(int[] params,
                                      int minNrSigs, double meanSignal, double maxSig, double minSig) {
        meanSignal = meanSignal - minSig;
        meanSignal = meanSignal - maxSig;
        if((minNrSigs-2) > 0){
            meanSignal = meanSignal / (minNrSigs - 2);
            data.get(middleIndex).setNewSignal(meanSignal);
        }
    }
    private void calcAndSetMeanSignal(int[] params,
                                      int minNrSigs, double meanSignal, double maxSig, double minSig, int index) {
        meanSignal = meanSignal - minSig;
        meanSignal = meanSignal - maxSig;
        if((minNrSigs-2) > 0){
            meanSignal = meanSignal / (minNrSigs - 2);
            data.get(index).setNewSignal(meanSignal);
        }
    }

    private double median (double[] array){
        Arrays.sort(array);
        return (array[(array.length-1)/2]+array[array.length/2])/2;
    }

    private void tearDown() throws IOException {
        bw.close();
        br.close();
        data.clear();
    }

    private int getMiddleIndex(int windowSize){
        return ((windowSize-1)/2);
    }

    private void validateInput(int[] params, int stepSize) throws ProcessException {
        if(params==null){
            throw new ProcessException("Params array is null");
        }
        if(!(params[1] == 0 || params[1] == 1)){
            throw new ProcessException("Undefined smoothing type, should be either 1 for median or 0 for trimmed mean");
        }
        if(params[0]<=params[2]){
            throw new ProcessException("Minimum positions to smooth should not be larger than window size");
        }
        if(stepSize < 1){
            throw new ProcessException("stepSize needs to be 1 for no stepping or larger than 1 for stepping");
        }
        if(params[0]==2 &&params[1]==0){
            throw new ProcessException("When calculating trimmed mean, window size needs to be atleast 3 or larger");
        }
        if(params[0] < 2){
            throw new ProcessException( "Window size needs to be atleast 2 or larger");
        }
        if(params[2] < 1){
            throw new ProcessException("Minimum positions to smooth needs to be positive");
        }
        if(!(params[3] == 0 || params[3] == 1)){
            throw new ProcessException("Print total mean flag must be either 0 or 1");
        }
        if(!(params[4] == 0 || params[4] == 1)){
            throw new ProcessException("The flag print zeroes should be either 1 for yes or 0 for no");
        }
    }
}
