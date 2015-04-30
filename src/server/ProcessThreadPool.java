package server;


import command.ProcessCommand;
import response.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Deprecated
public class ProcessThreadPool {

    private WorkHandler[] workHandlers;
    private ExecutorService executor;
    private int maxThreads;


    public ProcessThreadPool(WorkPool workPool) {

        maxThreads = ServerSettings.nrOfProcessThreads;

        /* Create a thread pool that spawns threads on demand */
        executor = Executors.newFixedThreadPool(maxThreads);

        /* Create arrays of work handlers and threads */
        workHandlers = new WorkHandler[maxThreads];

        /* Create a new work handler and a thread for each array element */
        for (int i=0; i<maxThreads; i++) {
            workHandlers[i] = new WorkHandler(workPool);
            executor.execute(workHandlers[i]);
        }
    }

    public void cancelWork(ProcessCommand procComm) {

        /* Loop through all work handlers to check which handler posseses
         * the sought work */
        for (int i=0; i<maxThreads; i++) {
            ProcessCommand currentProcComm = workHandlers[i].getCurrentWork();

            /* Make sure it is the right process command */
            if (currentProcComm != null && currentProcComm.equals(procComm)) {
                Future<Response> submission = workHandlers[i]
                        .getFutureProcess();

                /* Make sure it is not already done or cancelled then cancel */
                if (!submission.isDone() && !submission.isCancelled()) {
                    submission.cancel(true);
                }

                break;
            }
        }
    }
}
