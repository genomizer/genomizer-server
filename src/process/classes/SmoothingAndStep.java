package process.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class SmoothingAndStep {

    private double readSumValue;
    private int noOfValues;
    private BufferedWriter bw;
    private BufferedReader br;
    private ArrayList<Tuple> data;
    private int stepSize;


    /*
     * Parameters:
     * Params:	  An array with 5 integers representing parameters.
     * params[0]: Window Size, the number of signal values that the smoothing
     * 		  should be calculated on.
     * params[1]: Wether the smoothing should be trimmed mean (0) or median (1)
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
     * stepSize:  Flag that tells if the user wants to do stepping. Should be
     * 		  1 if the user wants no stepping. Have to be larger than 0.
     */
    public double smoothing(int[] params, String inPath, String outPath, int stepSize) throws IllegalAccessException, IOException, IllegalArgumentException {
	validateInput(params, stepSize);

	data = new ArrayList<Tuple>();
	readSumValue = 0;
	noOfValues = 0;
	String strLine;
	this.stepSize = stepSize;

	setupBuffertReader(inPath);
	setupBuffertWriter(outPath);

	for(int i = 0; i<params[0]; i++){
	    if((strLine = br.readLine()) != null){
		addLine(strLine);
	    }
	}
	smoothOneRow(params,params[0]);

	while((strLine = br.readLine()) != null){
	    shiftLeft(strLine, params);
	    smoothOneRow(params,params[0]);
	}

	data.remove(0);
	int dataSize = data.size();
	for (int i = 0;i<(dataSize-params[2]);i++){
	    smoothOneRow(params,dataSize-i);
	    data.remove(0);
	}
	dataSize = data.size();
	for (int j = 0; j < dataSize;j++){
	    writeToFile(params);

	    data.remove(0);
	}
	tearDown();

	return readSumValue/noOfValues;
    }

    private void smoothOneRow(int[] params, int noOfRowsToSmooth) throws IllegalAccessException, IOException {
	if(params[1]==1){
	    smoothMedian(noOfRowsToSmooth, params);
	} else if(params[1]==0){
	    smoothTrimmedMean(noOfRowsToSmooth, params);
	} else {
	    throw new IllegalAccessException();
	}
	writeToFile(params);
    }

    private void smoothTrimmedMean(int noOfRowsToSmooth, int[] params) {
	int minNrSigs = 1;
	double meanSignal = data.get(0).getSignal();
	double maxSig = meanSignal;
	double minSig = meanSignal;

	if(data.size() >= params[2]){
	    for(int j = 1 ; j < noOfRowsToSmooth; j++){
		meanSignal = meanSignal + data.get(j).getSignal();
		minNrSigs++;
		if(maxSig < data.get(j).getSignal()){
		    maxSig = data.get(j).getSignal();
		}
		if(minSig > data.get(j).getSignal()){
		    minSig = data.get(j).getSignal();
		}
	    }
	    calcAndSetMeanSignal(params, minNrSigs, meanSignal, maxSig,
		    minSig);
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
	    data.get(0).setNewSignal(median(array));
	}
    }

    private void writeToFile(int[] params) throws IOException {
	if(!((params[4] == 0) && (data.get(0).getNewSignal() == 0)) ){
	    if(data.get(0).getPosition()%stepSize== 0){
		bw.write(data.get(0).toString());
	    }
	}
	if (params[3] == 1){
	    readSumValue = readSumValue+ data.get(0).getSignal();
	    noOfValues++;
	}
    }

    private void shiftLeft(String strLine, int[] params) throws IllegalAccessException, IOException {
	addLine(strLine);
	data.remove(0);

	if(!data.get(data.size()-1).getChromosome().equals(data.get(data.size()-2).getChromosome())){
	    chromosomeChange(params);
	}
    }

    private void chromosomeChange(int[] params) throws IllegalAccessException, IOException {

	for (int i = 0;i<(params[0]-params[2]-1);i++){
	    smoothOneRow(params,params[0]-i);
	    data.remove(0);
	}
	for (int j = 0; j < params[2];j++){
	    writeToFile(params);
	    data.remove(0);
	}
	String strLine;
	for(int i = 0; i<params[0]-1; i++){
	    if((strLine = br.readLine()) != null){
		addLine(strLine);
	    }
	}
    }

    private void addLine(String strLine) {
	data.add(new Tuple(strLine));
    }

    private void setupBuffertWriter(String outPath) throws IOException {
	File outFile = new File(outPath);
	if (!outFile.exists()) {
	    outFile.createNewFile();
	}
	bw = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile()));
    }

    private void setupBuffertReader(String inPath) throws FileNotFoundException{
	br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(inPath))));
    }

    private void calcAndSetMeanSignal(int[] params,
	    int minNrSigs, double meanSignal, double maxSig, double minSig) {
	if((minNrSigs - 2) > params[2]){
	    meanSignal = meanSignal - minSig;
	    meanSignal = meanSignal - maxSig;
	    meanSignal = meanSignal / (minNrSigs - 2);
	    data.get(0).setNewSignal(meanSignal);
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

    private void validateInput(int[] params, int stepSize) throws IllegalArgumentException {
	if (params==null||params[2]>=params[0] || stepSize < 1 || params[0] < 1 ){
	    throw new IllegalArgumentException();
	}
    }
}


