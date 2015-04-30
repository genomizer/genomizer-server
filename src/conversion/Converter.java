package conversion;

import java.io.*;
import java.text.DecimalFormat;

/**
 * Created by c12arr on 2015-04-29.
 */
public class Converter {


    /**
     * Converts from .bed to .sgr
     * @param inputPath
     * @param outputPath
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public static void bedToSgr(String inputPath, String outputPath) throws FileNotFoundException {
        File inputFile;
        File outputFile;
        FileWriter fw = null;
        checkArguments(inputPath, outputPath);

        if (!inputPath.endsWith(".bed") || !outputPath.endsWith(".sgr")) {
            throw new IllegalArgumentException("Filetype not accepted for this conversion.");
        }

        inputFile = new File(inputPath);
        outputFile = new File(outputPath);

        try {
            outputFile.createNewFile();
            fw = new FileWriter(outputFile);


            BufferedReader fr = new BufferedReader(new FileReader(inputFile));
            String line;
            String []columns;
            int center;
            while((line =fr.readLine()) != null) {
               columns = line.split("\t");

                center = Integer.parseInt(columns[1])+(Integer.parseInt(columns[2])-Integer.parseInt(columns[1]))/2;
                fw.write(columns[0]+"\t"+center+"\t"+columns[4]+"\n");
            }
            fw.close();
            fr.close();
            } catch (IOException e) {
                e.printStackTrace();
        }

    }

    /**
     * Converts from .gff to .sgr
     * @param inputPath
     * @param outputPath
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public static void gffToSgr(String inputPath, String outputPath) throws FileNotFoundException {
        File inputFile;
        File outputFile;
        FileWriter fw = null;
        checkArguments(inputPath, outputPath);

        if (!inputPath.endsWith(".gff") || !outputPath.endsWith(".sgr")) {
            throw new IllegalArgumentException("Filetype not accepted for this conversion.");
        }

        inputFile = new File(inputPath);
        outputFile = new File(outputPath);

        try {
            outputFile.createNewFile();
            fw = new FileWriter(outputFile);

            BufferedReader fr = new BufferedReader(new FileReader(inputFile));
            String line;
            String []columns;
            int center;
            while((line =fr.readLine()) != null) {
                columns = line.split("\t");

                center = Integer.parseInt(columns[3])+(Integer.parseInt(columns[4])-Integer.parseInt(columns[3]))/2;
                fw.write(columns[0]+"\t"+center+"\t"+columns[5]+"\n");
            }
            fw.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts from .sgr to .wig
     * @param inputPath
     * @param outputPath
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public static void sgrToWig(String inputPath, String outputPath) throws FileNotFoundException {
        File inputFile;
        File outputFile;
        FileWriter fw = null;

        checkArguments(inputPath, outputPath);

        if (!inputPath.endsWith(".sgr") || !outputPath.endsWith(".wig")) {
            throw new IllegalArgumentException("Filetype not accepted for this conversion.");
        }

        inputFile = new File(inputPath);
        outputFile = new File(outputPath);

        try {
            outputFile.createNewFile();
            fw = new FileWriter(outputFile);

            BufferedReader fr = new BufferedReader(new FileReader(inputFile));
            String line;
            String []columns;
            String tempChr, chr="chr";
            int tempStart, start=-1;
            String sig="0", tempSig;

            while ((line = fr.readLine()) != null && !line.equals("")) {
                columns = line.split("\\s+");
                tempChr = columns[0];
                tempStart = Integer.parseInt(columns[1])-1;
                tempSig = columns[2];
                if (!chr.equals(tempChr)) {
                    chr = tempChr;
                    start = tempStart+1;
                    sig = tempSig;
                }
                else {
                    fw.write(chr+"\t"+start+"\t"+tempStart+"\t"+sig+"\n");
                    start = tempStart+1;
                    sig = tempSig;
                }
            }

            fw.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts from .bed to .wig via a temporary conversion to .sgr
     * @param inputPath
     * @param outputPath
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public static void bedToWig(String inputPath, String outputPath) throws FileNotFoundException {
        String tempPath = "resources/conversionTestData/bed2sgrTemp.sgr";
        File tempFile;
        try {
            bedToSgr(inputPath, tempPath);
            sgrToWig(tempPath, outputPath);
        } catch (Exception e) {
            tempFile = new File(tempPath);
            if (tempFile.exists())
                tempFile.delete();
            throw e;
        }

        tempFile = new File(tempPath);
        if (tempFile.exists())
            tempFile.delete();
    }

    public static void gffToWig(String inputPath, String outputPath)
            throws FileNotFoundException {
        File tempFile;
        String tempPath = "resources/conversionTestData/gff2sgrTemp.sgr";
        try {
            gffToSgr(inputPath, tempPath);
            sgrToWig(tempPath, outputPath);
        } catch (Exception e){
            tempFile = new File(tempPath);
            if (tempFile.exists())
                tempFile.delete();
            throw e;
        }

        tempFile = new File(tempPath);
        if (tempFile.exists())
            tempFile.delete();
    }

    /**
     * Checks if arguments are valid file paths
     * @param inputPath
     * @param outputPath
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    private static void checkArguments(String inputPath, String outputPath) throws FileNotFoundException {
        File inputFile = null;
        File outputFile = null;
        try {
            inputFile = new File(inputPath);
            outputFile = new File(outputPath);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null path/paths");
        }
        if(!inputFile.exists()) {
            throw new FileNotFoundException("Input file doesn't exists");
        }

        if(outputFile.exists()) {
            throw new IllegalArgumentException("Output file already exists");
        }
    }
}
