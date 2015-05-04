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


public class WorkPool {

    // Process storage objects
    private LinkedList<ProcessCommand> processesList;
    private HashMap<ProcessCommand, ProcessStatus> processesStatus;
    private HashMap<ProcessCommand, Future<Response>> processFutureMap;

    // Synchronization objects
    private final Lock lock;

    // Thread pool
    private ExecutorService executor;


    public WorkPool() {
        processesList = new LinkedList<>();
        processesStatus = new HashMap<>();
        processFutureMap = new HashMap<>();
        lock = new ReentrantLock();

        executor = Executors.newFixedThreadPool(
                ServerSettings.nrOfProcessThreads);
    }

    public LinkedList<ProcessCommand> getProcesses() {
        lock.lock();

        try {
            LinkedList<ProcessCommand> commandList =
                    new LinkedList<>(processFutureMap.keySet());
            commandList.addAll(processesList);

            return commandList;

        }  finally {
            lock.unlock();
        }

    }

    public void addWork(ProcessCommand command) {
        lock.lock();

        try {
            processesList.add(command);
            processesStatus.put(command, new ProcessStatus(command));

            Future<Response> response = executor.submit(new WorkHandler(this));

            if (response != null) {
                processFutureMap.put(command, response);
            }

        } finally {
            lock.unlock();
        }
    }

    public ProcessCommand pollProcess() {
        lock.lock();

        ProcessCommand processCommand = null;

        try {
            processCommand = processesList.poll();
        } finally {
            lock.unlock();
        }

        return processCommand;

    }

    public void cancelProcess(ProcessCommand processCommand) {
        lock.lock();

        try {

           Future<Response> response = processFutureMap.get(processCommand);

            if (response != null) {
                if (!response.isDone() && !response.isCancelled()) {
                    response.cancel(true);
                }
            }

            processesList.remove(processCommand);
            processesStatus.remove(processCommand);
        } finally {
            lock.unlock();
        }
    }

    public ProcessStatus getProcessStatus(ProcessCommand process) {
        lock.lock();

        try {

            if (processFutureMap.get(process).isDone() && !processFutureMap
                    .get(process).isCancelled()) {
                processesStatus.get(process).status = ProcessStatus
                        .STATUS_FINISHED;
            }

            return processesStatus.get(process);
        } finally {
            lock.unlock();
        }
    }

    public Response getProcessResponse(ProcessCommand processCommand) throws
            InterruptedException, ExecutionException {

        if (processesStatus.get(processCommand).status
                .equals(ProcessStatus.STATUS_FINISHED)) {
            return processFutureMap.get(processCommand).get();
        }
        return null;
    }

}
