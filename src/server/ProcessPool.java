package server;

import command.Process;
import response.Response;
import command.process.PutProcessCommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProcessPool {

    private static final long statusTimeToLive = 2*1000*60*60*24;

    // Process to status and response maps
    private HashMap<UUID, Process> processStatusMap;
    private HashMap<UUID, Future<Response>> processFutureMap;
    private LinkedList<PutProcessCommand> processesList;

    // Synchronization objects
    private final Lock lock;

    // Thread pool
    private ExecutorService executor;


    public ProcessPool(int threads) {
        processStatusMap = new HashMap<>();
        processFutureMap = new HashMap<>();
        processesList = new LinkedList<>();
        lock = new ReentrantLock();

        executor = Executors.newFixedThreadPool(threads);

        // Start a cleanup thread that will remove stale processes every 10 mins
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    removeOldProcesses();
                    try {
                        Thread.sleep(600000);
                    } catch (InterruptedException ex) {
                        Debug.log("Process purging thread was unexpectedly " +
                                "interrupted");
                    }

                }
            }
        }).start();*/
    }

    /**
     * Gets a list of all submitted process commands
     *
     * @return a linked list with elements of type {@link command
     * .PutProcessCommand}
     */
    public LinkedList<PutProcessCommand> getProcesses() {
        lock.lock();

        try {
            return new LinkedList<>(processesList);

        }  finally {
            lock.unlock();
        }

    }


    /**
     * Adds a process command to the process pool
     *
     * @param processCommand - the process command to be added
     */
    public void addProcess(PutProcessCommand processCommand) {
        lock.lock();

        try {
            Process process = new Process(processCommand);

            processCommand.setPID(UUID.randomUUID());

            processesList.add(processCommand);

            // Create a process command to process status mapping
            processStatusMap.put(processCommand.getPID(), process);

            // Submit the process with a new work handler for execution
            Future<Response> response = executor.submit(
                    new ProcessHandler(processCommand, process));

            if (response != null) {
                // Create a process command to process response mapping
                processFutureMap.put(processCommand.getPID(), response);
            }

        } finally {
            lock.unlock();
        }
    }

    /**
     * Attempts to cancel the specified process if it is not completed or if
     * it has not been cancelled already.
     *
     * @param processID - the id of the process to be cancelled
     */
    public void cancelProcess(UUID processID) {
        lock.lock();

        try {

           Future<Response> response = processFutureMap.get(processID);

            if (response != null) {
                // Attempt to cancel if not done or cancelled already
                if (!response.isDone() && !response.isCancelled()) {
                    response.cancel(true);
                }
            }

        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the process status for the specified process id.
     *
     * @param processID - the id of the process which status to be retrieved
     * @return processStatus - the process status of the specified process
     * command
     */
    public Process getProcessStatus(UUID processID) {
        lock.lock();

        try {

            return processStatusMap.get(processID);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the process response for the specified process id.
     *
     * @param processID
     * @return processresponse - the response if the process has finished
     * execution else null.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Response getProcessResponse(UUID processID) throws
            InterruptedException, ExecutionException {

        lock.lock();

        try {
            if (processStatusMap.get(processID).status
                    .equals(Process.STATUS_FINISHED)) {
                return processFutureMap.get(processID).get();
            }
            return null;
        } finally {
            lock.unlock();
        }
    }


    /**
     * Used internally to cleanup old processes that had either finished
     * gracefully, crashed or were cancelled.
     */
    public void removeOldProcesses() {

        // Get current time
        long currentTime = System.currentTimeMillis();

        // List to store processes to be removed
        LinkedList<PutProcessCommand> toBeRemoved = new LinkedList<>();

        lock.lock();

        try {

		/* Loop through all processes and check statuses */
            for (PutProcessCommand processCommand : processesList) {

                Process processStatus = processStatusMap.get(processCommand);
                String statusString = processStatus.status;

                // Check if it has finished or crashed
                if (statusString.equals(Process.STATUS_FINISHED)
                        || statusString.equals(Process.STATUS_CRASHED)) {

                    long processTimeAdded = processStatus.timeAdded;
                    long timeDifference = currentTime - processTimeAdded;

                    if (timeDifference > statusTimeToLive) {
                        toBeRemoved.add(processCommand);
                    }
                }
            }

            for (PutProcessCommand processCommand : toBeRemoved) {
                Debug.log("Removing old process status: "
                        + processCommand.getExpId());
                processStatusMap.remove(processCommand);
                processFutureMap.remove(processCommand);
                processesList.remove(processCommand);
            }
        } finally {
            lock.unlock();
        }



    }

}
