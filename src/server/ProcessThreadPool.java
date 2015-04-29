package server;


public class ProcessThreadPool {

    private WorkHandler[] workHandlers;
    private Thread[] threads;

    public ProcessThreadPool(WorkPool workPool) {

        int totalThreads = ServerSettings.nrOfProcessThreads;

        // Create arrays of work handlers and threads
        workHandlers = new WorkHandler[totalThreads];
        threads = new Thread[totalThreads];

        // Create a new work handler and a thread for each array element
        for (int i=0; i<totalThreads; i++) {
            workHandlers[i] = new WorkHandler(workPool);
            threads[i] = new Thread(workHandlers[i]);
            threads[i].setName("Process thread: "+  i);
            threads[i].start();
        }
    }
}
