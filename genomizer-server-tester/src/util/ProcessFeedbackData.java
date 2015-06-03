package util;


/**
 * @author c11dkn
 * @version 1.0
 * 16 May 2014
 */
public class ProcessFeedbackData {

    public String experimentName;
    public String PID;
    public String status;
    public String author;
    public String[] outputFiles;
    public long timeAdded;
    public long timeStarted;
    public long timeFinished;

    public ProcessFeedbackData(String experimentName,String PID, String status, String author
            , String[] outputFiles, long timeAdded, long timeFinished, long timeStarted) {
        this.experimentName = experimentName;
        this.PID = PID;
        this.status = status;
        this.author = author;
        this.outputFiles = outputFiles;
        this.timeAdded = timeAdded;
        this.timeFinished = timeFinished;
        this.timeStarted = timeStarted;
    }

    // TODO: Where is this code even used ? (OO)
//    public static ProcessFeedbackData[] getExample() {
//        String[] names = { "Kalle", "Pelle", "Anna", "Nils", "Olle" };
//        String[] statuses = { "Finished", "Crashed", "Started", "Waiting"};
//        Random rand = new Random();
//        ProcessFeedbackData[] data = new ProcessFeedbackData[10];
//        for(int i=0; i<10; i++) {
//            String expName = "experiment" + i;
//            String author = names[rand.nextInt(5)];
//            String status = statuses[rand.nextInt(4)];
//            String[] files = new String[] {"file1","file2","file3"};
//            data[i] = new ProcessFeedbackData(expName, status, author, files, 100,100,0);
//        }
//        return data;
//    }

}

