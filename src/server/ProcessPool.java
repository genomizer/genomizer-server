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

    // Process to status and response maps
    private HashMap<ProcessCommand, ProcessStatus> processesStatus;
    private HashMap<ProcessCommand, Future<Response>> processFutureMap;

    // Synchronization objects
    private final Lock lock;

    // Thread pool
    private ExecutorService executor;


    public ProcessPool() {
        processesStatus = new HashMap<>();
        processFutureMap = new HashMap<>();
        lock = new ReentrantLock();

        executor = Executors.newFixedThreadPool(
                ServerSettings.nrOfProcessThreads);
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
            // Create a process command to process status mapping
            processesStatus.put(processCommand, new ProcessStatus(processCommand));

            // Submit the process with a new work handler for execution
            Future<Response> response = executor.submit(new ProcessHandler(this,
             processCommand));

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

            // Cleanup the maps from stale processes
            processesStatus.remove(processCommand);
            processFutureMap.remove(processCommand);
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

                processesStatus.get(processCommand).status = ProcessStatus
                        .STATUS_FINISHED;
            }

            return processesStatus.get(processCommand);
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

        if (processesStatus.get(processCommand).status
                .equals(ProcessStatus.STATUS_FINISHED)) {
            return processFutureMap.get(processCommand).get();
        }
        return null;
    }

}
