package server;

import command.Process;
import response.HttpStatusCode;
import response.Response;
import util.Util;

import java.util.*;
import java.util.concurrent.*;

public class ProcessPool {

    /** Five days. */
    private static final long STATUS_TTL = 5 * 1000 * 60 * 60 * 24;

    /** Number of days after which a process is considered stale. */
    private static final int STALE_DAYS = 30;

    private final ConcurrentHashMap<UUID, Process> processStatuses =
            new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Future<Response>> processFutures =
            new ConcurrentHashMap<>();

    private final ExecutorService executor;

    /**
     * @param threads The number of threads the ProcessPool will use for the
     *                processes.
     * @throws java.lang.IllegalArgumentException If threads <= 0.
     */
    public ProcessPool(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    /**
     * Gets a list of all submitted process commands
     * except those that are considered stale
     * (submitted more than $days days ago).
     *
     * @return a linked list with elements of type {@link command.Process}
     */
    public List<Process> getProcesses() {
        List<Process> processStatusesList = new ArrayList<>();

        Calendar pastCal = Calendar.getInstance();
        pastCal.setTimeInMillis(System.currentTimeMillis());
        pastCal.add(Calendar.DAY_OF_MONTH, -STALE_DAYS);

        Calendar addedCal = Calendar.getInstance();

        for (Process process : processStatuses.values()) {
            addedCal.setTimeInMillis(process.timeAdded);

            if (addedCal.after(pastCal)) {
                processStatusesList.add(process);
            }
        }
        return processStatusesList;
    }


    /**
     * Adds a new process to the process pool.
     *  @param process  - process metadata.
     *  @param callable - function to execute.
     *  @return PID of the newly-created process.
     */
    @Deprecated
    public UUID addProcess(Process process, Callable<Response> callable) {
        UUID uuid = UUID.randomUUID();
        process.PID = uuid.toString();

        // Create a process command to process status mapping
        processStatuses.put(uuid, process);

        // Submit the process with a new work handler for execution
        Future<Response> response = executor.submit(
                new ProcessWrapper(callable, process));

        // Create a process command to process response mapping
        processFutures.put(uuid, response);

        return uuid;
    }


    /**
     * Adds a new process to the process pool.
     *  @param callable - function to execute.
     *  @return PID of the newly-created process.
     */
    public UUID addProcess(Callable<Response> callable) {
        UUID uuid = UUID.randomUUID();
        Process process = new Process();
        process.PID = uuid.toString();

        // Create a process command to process status mapping
        processStatuses.put(uuid, process);

        // Submit the process with a new work handler for execution
        Future<Response> response = executor.submit(
                new ProcessWrapper(callable, process));

        // Create a process command to process response mapping
        processFutures.put(uuid, response);

        return uuid;
    }

    public Future<Response> getFuture(UUID uuid) {
        return processFutures.get(uuid);
    }

    /**
     * Cancels a given process.
     * Cancelling a crashed/finished process removes it from the process list.
     *
     * @param processID - the id of the process to be cancelled
     */
    public void cancelProcess(UUID processID) {
        Future<Response> future = processFutures.get(processID);

        if (future != null) {
            future.cancel(true);
            processStatuses.remove(processID);
            processFutures.remove(processID);
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
        return processStatuses.get(processID);
    }

    /**
     * Retrieves the process response for the specified process id.
     * NB: can return null.
     *
     * @param processID The id for the process whose response to retrieve.
     * @return The response if the process has finished, otherwise null.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Response getProcessResponse(UUID processID)
            throws InterruptedException, ExecutionException {

        if (processStatuses.get(processID).status
                .equals(Process.STATUS_FINISHED)) {
            return processFutures.get(processID).get();
        }
        return null;
    }


    /**
     * Used internally to clean up old processes that had either finished
     * gracefully, crashed or were cancelled.
     */
    public void removeOldProcesses() {

        // Get current time
        long currentTime = System.currentTimeMillis();

        // List to store processes to be removed
        ArrayList<Process> toBeRemoved = new ArrayList<>();

        // Loop through all processes and check statuses.
        for (Process process : processStatuses.values()) {

            String statusString = process.status;

            // Check if a process has finished or crashed.
            if (statusString.equals(Process.STATUS_FINISHED)
                || statusString.equals(Process.STATUS_CRASHED)) {

                long processTimeAdded = process.timeAdded;
                long timeDifference = currentTime - processTimeAdded;

                if (timeDifference > STATUS_TTL) {
                    toBeRemoved.add(process);
                }
            }
        }

        for (Process process : toBeRemoved) {
            Debug.log(
                    "Removing old process status: "
                    + process.PID);
            processStatuses.remove(UUID.fromString(process.PID));
            processFutures.remove(UUID.fromString(process.PID));
        }
    }

    // A wrapper around a Callable that takes care of
    // setting process metadata (status, timestamps) to right values.
    private static class ProcessWrapper implements Callable<Response> {

        private final Callable<Response> callable;
        private final Process process;

        public ProcessWrapper(
                Callable<Response> callable,
                Process process) {
            if (callable == null) {
                throw new NullPointerException("Callable must not be null");
            }
            if (process == null) {
                throw new NullPointerException("Callable must not be null");
            }
            this.callable = callable;
            this.process = process;
        }

        @Override
        public Response call() {

            Response response = null;

            Debug.log(
                    "Execution of process with id " + process.PID
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
                    String successMsg =
                            "Execution of process with id " + process.PID
                            + " in experiment "
                            + process.experimentName + " has finished.";
                    Debug.log(successMsg);
                    ErrorLogger.log("PROCESS", successMsg);
                } else {
                    System.out.println(
                            "Process status: " + response.getCode());
                    process.status = Process.STATUS_CRASHED;
                    String crashedMsg =
                            "FAILURE! Execution of process with id "
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
                             Util.formatTimeDifference(
                                     (process.timeFinished -
                                      process.timeStarted) / 1000);
            Debug.log(timeMsg);
            ErrorLogger.log("PROCESS", timeMsg);

            Debug.log("PID: " + process.PID);
            if (response != null) {
                Debug.log("Process response: " + response.getMessage());
            }

            return response;
        }
    }
}
