package server;

import command.Process;
import response.HttpStatusCode;
import response.Response;
import util.Util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProcessPool {

    // Five days.
    private static final long statusTimeToLive = 5*1000*60*60*24;

    // PID to metadata and Future<Response> maps.
    private HashMap<UUID, Process> processStatusMap;
    private HashMap<UUID, Future<Response>> processFutureMap;

    // Synchronization object,
    private final Lock lock;

    // Thread pool.
    private ExecutorService executor;


    public ProcessPool(int threads) {
        processStatusMap = new HashMap<>();
        processFutureMap = new HashMap<>();
        lock = new ReentrantLock();

        executor = Executors.newFixedThreadPool(threads);
    }

    // Number of days in the past after which processes are considered stale.
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
            List<Process> processStatusesList = new ArrayList<>();

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
     * Adds a new process to the process pool.
     *
     * @param process  - process metadata.
     * @param callable - function to execute.
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
     * Cancels a given process.
     * Cancelling a crashed/finished process removes it from the process list.
     *
     * @param processID - the id of the process to be cancelled
     */
    public void cancelProcess(UUID processID) {
        lock.lock();

        try {
            Future<Response> future = processFutureMap.get(processID);

            if (future != null) {
                future.cancel(true);
                processStatusMap.remove(processID);
                processFutureMap.remove(processID);
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
     * NB: can return null.
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
     * Used internally to clean up old processes that had either finished
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
                processStatusMap.remove(UUID.fromString(process.PID));
                processFutureMap.remove(UUID.fromString(process.PID));
            }
        } finally {
            lock.unlock();
        }



    }

    private static class ProcessHandler implements Callable<Response> {

        private Callable<Response> callable;
        private Process process;

        public ProcessHandler(Callable<Response> callable,
                              Process process) {
            this.callable = callable;
            this.process = process;
        }

        @Override
        public Response call() {

            Response response = null;

            if (callable != null && process != null) {
                Debug.log("Execution of process with id " + process.PID
                        + " in experiment "
                        + process.experimentName + " has begun.");

                process.status = Process.STATUS_STARTED;
                process.timeStarted = System.currentTimeMillis();


                try {
                    /* Execute the process command */
                    response = callable.call();

                    if (response.getCode() == HttpStatusCode.CREATED ||
                        response.getCode() == HttpStatusCode.OK) {
                        process.status = Process.STATUS_FINISHED;
                        String successMsg = "Execution of process with id " + process.PID
                                + " in experiment "
                                + process.experimentName + " has finished.";
                        Debug.log(successMsg);
                        ErrorLogger.log("PROCESS", successMsg);
                    } else {
                        System.out.println("Process status: " + response.getCode());
                        process.status = Process.STATUS_CRASHED;
                        String crashedMsg = "FAILURE! Execution of process with id "
                                + process.PID + " in experiment "
                                + process.experimentName + " has crashed.";
                        Debug.log(crashedMsg);
                        ErrorLogger.log("PROCESS", crashedMsg);
                    }

                } catch (Exception e) {
                    process.status = Process.STATUS_CRASHED;
                }

                process.timeFinished = System.currentTimeMillis();

                String timeMsg = "PID: " + process.PID + "\nElapsed time: " +
                        Util.formatTimeDifference((process.timeFinished - process.timeStarted) / 1000) ;
                Debug.log(timeMsg);
                ErrorLogger.log("PROCESS", timeMsg);

            }

            Debug.log("PID: " + process.PID);
            Debug.log("Process response: " + response.getMessage());

            return response;

        }

    }
}
