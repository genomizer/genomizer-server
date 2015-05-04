package conversion;

import java.io.*;

/**
 * Handles conversion between different profile file types.
 * Files supported: bed, gff, sgr, wig
 * Conversions supported:   bed -> sgr
 *                          gff -> sgr
 *                          sgr -> wig
 *                          bed -> wig
 *                          gff -> wig
 *                          wig -> sgr
 *
 * Created 2015-04-30.
 *
 * @author Albin Råstander <c12arr@cs.umu.se>
 * @author Martin Larsson <dv13mln@cs.umu.se>
 */
public class ProfileDataConverter {
    private File outputPath;

    /**
     * Constructor
     * @param outputPath path to archive storing converted files
     */
    public ProfileDataConverter(String outputPath) {
        try {
            this.outputPath = new File(outputPath);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        if (!this.outputPath.exists())
            throw new IllegalArgumentException("Archive does not exist.");
    }

    /**
     * Converts from .bed to .sgr
     * @param inputPath path to input file
     * @return String path to output fle
     * @throws java.io.IOException
     * @throws IllegalArgumentException
     */
    public String bedToSgr(String inputPath)
            throws IOException, IllegalArgumentException {
        File inputFile;
        File outputFile = null;
        FileWriter fw;
        checkArguments(inputPath);

        if (!inputPath.endsWith(".bed")) {
            throw new IllegalArgumentException("File type not accepted " +
                    "for this conversion.");
        }

        inputFile = new File(inputPath);

        try {
            outputFile = File.createTempFile("bed2sgr", ".sgr", outputPath);
            fw = new FileWriter(outputFile);

            BufferedReader fr = new BufferedReader(new FileReader(inputFile));

            String line;
            String []columns;
            int center;

            while((line =fr.readLine()) != null) {
               columns = line.split("\t");

                center = Integer.parseInt(columns[1])+
                        (Integer.parseInt(columns[2])-
                                Integer.parseInt(columns[1]))/2;
                fw.write(columns[0]+"\t"+center+"\t"+columns[4]+"\n");
            }

            fw.close();
            fr.close();

            } catch (IOException e) {
                throw e;
        }

        return outputFile.getPath();
    }

    /**
     * Converts from .gff to .sgr
     * @param inputPath path to input file
     * @return String path to output fle
     * @throws java.io.IOException
     * @throws IllegalArgumentException
     */
    public String gffToSgr(String inputPath)
            throws IOException, IllegalArgumentException {
        File inputFile;
        File outputFile = null;
        FileWriter fw;

        checkArguments(inputPath);

        if (!inputPath.endsWith(".gff")) {
            throw new IllegalArgumentException("File type not " +
                    "accepted for this conversion.");
        }

        inputFile = new File(inputPath);

        try {
            outputFile = File.createTempFile("gff2sgr", ".sgr", outputPath);
            fw = new FileWriter(outputFile);

            BufferedReader fr = new BufferedReader(new FileReader(inputFile));

            String line;
            String []columns;
            int center;

            while((line =fr.readLine()) != null) {
                columns = line.split("\t");

                center = Integer.parseInt(columns[3])+
                        (Integer.parseInt(columns[4])-
                                Integer.parseInt(columns[3]))/2;
                fw.write(columns[0]+"\t"+center+"\t"+columns[5]+"\n");
            }
            fw.close();
            fr.close();

        } catch (IOException e) {
            throw e;
        }

        return outputFile.getPath();
    }

    /**
     * Converts from .sgr to .wig
     * @param inputPath path to input file
     * @return String path to output fle
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public String sgrToWig(String inputPath)
            throws IOException {
        File inputFile;
        File outputFile = null;
        FileWriter fw;

        checkArguments(inputPath);

        if (!inputPath.endsWith(".sgr")) {
            throw new IllegalArgumentException("File type not " +
                    "accepted for this conversion.");
        }

        inputFile = new File(inputPath);

        try {
            outputFile = File.createTempFile("sgr2wig", ".wig", outputPath);
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
            throw e;
        }

        return outputFile.getPath();
    }

    /**
     * Converts from .bed to .wig via a temporary conversion to .sgr
     * @param inputPath input file path
     * @return String path to output fle
     * @throws java.io.IOException
     * @throws IllegalArgumentException
     */
    public String bedToWig(String inputPath)
            throws IOException {
        File tempFile;
        String tempPath = null;
        String outputPath;

        checkArguments(inputPath);

        if (!inputPath.endsWith(".bed")) {
            throw new IllegalArgumentException("File type not " +
                    "accepted for this conversion.");
        }

        try {
            tempPath = bedToSgr(inputPath);
            outputPath = sgrToWig(tempPath);
        } catch (Exception e) {
            tempFile = new File(tempPath);
            if (tempFile.exists())
                tempFile.delete();
            throw e;
        }

        tempFile = new File(tempPath);
        if (tempFile.exists())
            tempFile.delete();

        return outputPath;
    }

    /**
     * Converts from .gff to .wig
     * @param inputPath
     * @return String path to output file
     * @throws IOException
     */
    public String gffToWig(String inputPath)
            throws IOException {
        File tempFile;
        String tempPath = null;
        String outputPath;

        checkArguments(inputPath);
        if (!inputPath.endsWith(".gff")) {
            throw new IllegalArgumentException("File type not " +
                    "accepted for this conversion.");
        }

        try {
            tempPath = gffToSgr(inputPath);
            System.out.println(tempPath);
            outputPath = sgrToWig(tempPath);
        } catch (Exception e){
            tempFile = new File(tempPath);
            if (tempFile.exists())
                tempFile.delete();
            throw e;
        }

        tempFile = new File(tempPath);
        if (tempFile.exists())
            tempFile.delete();

        return outputPath;
    }

    /**
     * Converts from .wig to .sgr
     * @param wigType wigtype: "bed", "fixedStep", "variableStep"
     * @param inputPath path to input file
     * @return String path to output file
     * @throws IOException
     */
    public String wigToSgr(String wigType, String inputPath)
            throws IOException {
        String outputFileString = null;
        switch (wigType) {
            case "bed":
                outputFileString = wigBedToSgr(inputPath);
                break;
            case "fixedStep":
                outputFileString = wigFixedStepToSgr(inputPath);
                break;
            case "variableStep":
                outputFileString = wigVariableStepToSgr(inputPath);
                break;
            default:
                throw new IllegalArgumentException("Unknown wig type.");
        }

        return outputFileString;
    }

    /**
     * Converts from .wig (of subtype bed) to .sgr
     * @param inputPath path to input file
     * @return String path to output file
     * @throws IOException
     */
    private String wigBedToSgr(String inputPath)
            throws IOException {
        File inputFile;
        File outputFile;
        FileWriter fw;

        checkArguments(inputPath);

        if (!inputPath.endsWith(".wig")) {
            throw new IllegalArgumentException("File type not " +
                    "accepted for this conversion.");
        }

        inputFile = new File(inputPath);

        try {
            outputFile = File.createTempFile("wigBed2sgr", ".sgr", outputPath);
            fw = new FileWriter(outputFile);

            BufferedReader fr = new BufferedReader(new FileReader(inputFile));

            String line;
            String []columns;

            while ((line = fr.readLine()) != null) {
                columns = line.split("\\s+");
                fw.write(columns[0]+"\t"+columns[1]+"\t"+columns[3]+"\n");
            }

            fw.close();
            fr.close();

        } catch (IOException e) {
        throw e;
        }

        return outputFile.getPath();
    }


    /**
     * Checks if arguments are valid file paths
     * @param path path to input file
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    private static void checkArguments(String path)
            throws FileNotFoundException {
        File file;

        try {
            file = new File(path);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Path can not be null.");
        }

        if(!file.exists())
            throw new FileNotFoundException("File doesn't exists.");
    }

    /**
     * Converts from .wig (of subtype fixedStep) to .sgr
     * @param inputPath path to input file
     * @return String path to output file
     * @throws IOException
     */
    private String wigFixedStepToSgr(String inputPath)
          throws IOException {
          File inputFile;
          File outputFile;
          FileWriter fw;

          checkArguments(inputPath);

          if (!inputPath.endsWith(".wig")) {
              throw new IllegalArgumentException("File type not " +
                      "accepted for this conversion.");
          }

          inputFile = new File(inputPath);

          try {
              outputFile = File.createTempFile("wigFixedstep2sgr", ".sgr", outputPath);
              fw = new FileWriter(outputFile);

              BufferedReader fr = new BufferedReader(new FileReader(inputFile));

              String line;
              String []columns;
              String chrom;
              int start;
              int step;


              fr.readLine();
              line = fr.readLine();

              columns = line.split("\\s+");

              if(!columns[0].equals("fixedStep")) {
                  throw new IllegalArgumentException("File is not of type" +
                          " fixed step.");
              }

              chrom = columns[1].substring(6);
              start = Integer.parseInt(columns[2].substring(6));
              step = Integer.parseInt(columns[3].substring(5));


              while ((line = fr.readLine()) != null) {
                  if(line.equals("")) {
                      continue;
                  }
                  fw.write(chrom + "\t" + start + "\t" + line + "\n");
                  start+=step;
              }

              fw.close();
              fr.close();
          } catch (IOException e) {
              throw e;
          }

      return outputFile.getPath();
    }

    /**
     * Converts from .wig (of subtype variableStep) to .sgr
     * @param inputPath path to input file
     * @return String path to output file
     * @throws IOException
     */
    private String wigVariableStepToSgr(String inputPath)
            throws IOException {
        File inputFile;
        File outputFile;
        FileWriter fw;

        checkArguments(inputPath);

        if (!inputPath.endsWith(".wig")) {
            throw new IllegalArgumentException("File type not " +
                    "accepted for this conversion.");
        }

        inputFile = new File(inputPath);

        try {
            outputFile = File.createTempFile("wigVariablestep2sgr", ".sgr", outputPath);
            fw = new FileWriter(outputFile);

            BufferedReader fr = new BufferedReader(new FileReader(inputFile));

            String line;
            String[] columns;
            String chrom;

            fr.readLine();
            line = fr.readLine();

            columns = line.split("\\s+");

            if (!columns[0].equals("variableStep")) {
                throw new IllegalArgumentException("File is not of type" +
                        " variable step.");
            }

            chrom = columns[1].substring(6);

            while ((line = fr.readLine()) != null) {
                if (line.equals("")) {
                    continue;
                }
                columns = line.split("\\s+");

                fw.write(chrom + "\t" + columns[0] + "\t" + columns[1] + "\n");
            }

            fw.close();
            fr.close();
        } catch (IOException e) {
            throw e;
        }

        return outputFile.getPath();
    }
}
