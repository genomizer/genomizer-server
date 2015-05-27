package server;

import command.Process;
import command.process.PutProcessCommand;
import response.Response;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProcessPool {

    private static final long statusTimeToLive = 2*1000*60*60*24;

    // Process to status and response maps
    private HashMap<UUID, Process> processStatusMap;
    private HashMap<UUID, Future<Response>> processFutureMap;

    // Synchronization objects
    private final Lock lock;

    // Thread pool
    private ExecutorService executor;


    public ProcessPool(int threads) {
        processStatusMap = new HashMap<>();
        processFutureMap = new HashMap<>();
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

    // Number of days in the past to retrieve processes
    private static final int days = 30;

    /**
     * Gets a list of all submitted process commands
     * except those that are considered stale
     * (submitted more than $days days ago).
     *
     * @return a linked list with elements of type {@link command.Process}
     */
    public List<Process> getProcesses() {
        lock.lock();

        try {
            List<Process> processStatusesList = new LinkedList<>();

            Calendar pastCal = Calendar.getInstance();
            pastCal.setTimeInMillis(System.currentTimeMillis());
            pastCal.add(Calendar.DAY_OF_MONTH, -days);

            Calendar addedCal = Calendar.getInstance();

            for (Process process: processStatusMap.values()) {
                addedCal.setTimeInMillis(process.timeAdded);

                if (addedCal.after(pastCal)) {
                    processStatusesList.add(process);
                }
            }
            return processStatusesList;

        }  finally {
            lock.unlock();
        }

    }


    /**
     * Adds a process command to the process pool
     *
     * @param process  - process metada.
     * @param callabel - the function that executes the process.
     */
    public void addProcess(Process process, Callable<Response> callable) {
        lock.lock();

        try {
            UUID uuid = UUID.randomUUID();
            process.PID = uuid.toString();

            // Create a process command to process status mapping
            processStatusMap.put(uuid, process);

            // Submit the process with a new work handler for execution
            Future<Response> response = executor.submit(
                    new ProcessHandler(callable, process));

            if (response != null) {
                // Create a process command to process response mapping
                processFutureMap.put(uuid, response);
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

        lock.lock();

        try {

            // Get current time
            long currentTime = System.currentTimeMillis();

            // List to store processes to be removed
            ArrayList<Process> toBeRemoved = new ArrayList<>();

            // Loop through all processes and check statuses.
            for (Process process : processStatusMap.values()) {

                String statusString = process.status;

                // Check if a process has finished or crashed.
                if (statusString.equals(Process.STATUS_FINISHED)
                        || statusString.equals(Process.STATUS_CRASHED)) {

                    long processTimeAdded = process.timeAdded;
                    long timeDifference = currentTime - processTimeAdded;

                    if (timeDifference > statusTimeToLive) {
                        toBeRemoved.add(process);
                    }
                }
            }

            for (Process process : toBeRemoved) {
                Debug.log("Removing old process status: "
                        + process.PID);
                processStatusMap.remove(process.PID);
                processFutureMap.remove(process.PID);
            }
        } finally {
            lock.unlock();
        }



    }

}
