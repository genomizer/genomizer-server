package server;

import command.ProcessCommand;
import command.ProcessStatus;
import response.Response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProcessPool {

    private static final long statusTimeToLive = 2*1000*60*60*24;

    // Process to status and response maps
    private HashMap<ProcessCommand, ProcessStatus> processStatusMap;
    private HashMap<ProcessCommand, Future<Response>> processFutureMap;

    // Synchronization objects
    private final Lock lock;

    // Thread pool
    private ExecutorService executor;


    public ProcessPool(int threads) {
        processStatusMap = new HashMap<>();
        processFutureMap = new HashMap<>();
        lock = new ReentrantLock();

        executor = Executors.newFixedThreadPool(threads);
    }

    /**
     * Gets a list of all submitted process commands
     *
     * @return a linked list with elements of type {@link command.ProcessCommand}
     */
    public LinkedList<ProcessCommand> getProcesses() {
        lock.lock();

        try {
            return new LinkedList<>(processFutureMap.keySet());

        }  finally {
            lock.unlock();
        }

    }


    /**
     * Adds a process command to the work pool
     *
     * @param processCommand - the process command to be added
     */
    public void addProcess(ProcessCommand processCommand) {
        lock.lock();

        try {
            ProcessStatus processStatus = new ProcessStatus(processCommand);

            // TODO Add id
            //processCommand.setId();

            // Create a process command to process status mapping
            processStatusMap.put(processCommand, processStatus);

            // Submit the process with a new work handler for execution
            Future<Response> response = executor.submit(
                    new ProcessHandler(processCommand, processStatus));

            if (response != null) {
                // Create a process command to process response mapping
                processFutureMap.put(processCommand, response);
            }

        } finally {
            lock.unlock();
        }
    }

    /**
     * Attempts to cancel the specified process if it is not completed or if
     * it has not been cancelled already.
     *
     * @param processCommand - the process command to be cancelled
     */
    public void cancelProcess(ProcessCommand processCommand) {
        lock.lock();

        try {

           Future<Response> response = processFutureMap.get(processCommand);

            if (response != null) {
                // Attempt to cancel if not done or cancelled already
                if (!response.isDone() && !response.isCancelled()) {
                    response.cancel(true);
                }
            }

            // Cleanup the mappings of stale processes
            removeOldProcesses();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the process status for the specified process command.
     *
     * @param processCommand - the process command which status to be retrieved
     * @return processStatus - the process status of the specified process
     * command
     */
    public ProcessStatus getProcessStatus(ProcessCommand processCommand) {
        lock.lock();

        try {

            // Change status of the process if it has completed
            if (processFutureMap.get(processCommand).isDone() && !processFutureMap
                    .get(processCommand).isCancelled()) {

                processStatusMap.get(processCommand).status = ProcessStatus
                        .STATUS_FINISHED;
            }

            return processStatusMap.get(processCommand);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the process response for the specified process command.
     *
     * @param processCommand
     * @return processresponse - the response if the process has finished
     * execution else null.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Response getProcessResponse(ProcessCommand processCommand) throws
            InterruptedException, ExecutionException {

        if (processStatusMap.get(processCommand).status
                .equals(ProcessStatus.STATUS_FINISHED)) {
            return processFutureMap.get(processCommand).get();
        }
        return null;
    }


    /**
     * Used internally to cleanup old processes that had either finished
     * gracefully, crashed or were cancelled.
     */
    private void removeOldProcesses() {

        // Get current time
        long currentTime = System.currentTimeMillis();

        // List to store processes to be removed
        LinkedList<ProcessCommand> toBeRemoved = new LinkedList<>();

        LinkedList<ProcessCommand> processesList =
                new LinkedList<>(processFutureMap.keySet());

		/* Loop through all processes and check statuses */
        for (ProcessCommand processCommand : processesList) {

            ProcessStatus processStatus = processStatusMap.get(processCommand);
            String statusString = processStatus.status;

            // Check if it has finished or crashed
            if (statusString.equals(ProcessStatus.STATUS_FINISHED)
                    || statusString.equals(ProcessStatus.STATUS_CRASHED)) {

                long processTimeAdded = processStatus.timeAdded;
                long timeDifference = currentTime - processTimeAdded;

                if (timeDifference > statusTimeToLive) {
                    toBeRemoved.add(processCommand);
                }
            }
        }

        for (ProcessCommand processCommand : toBeRemoved) {
            Debug.log("Removing old process status: "
                    + processCommand.getExpId());
            processStatusMap.remove(processCommand);
            processFutureMap.remove(processCommand);
        }

    }

}
