package server;

import command.ProcessCommand;
import command.ProcessStatus;
import response.Response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class WorkPool {

    // Process storage objects
    private LinkedList<ProcessCommand> processesList;
    private HashMap<ProcessCommand, ProcessStatus> processesStatus;

    // Synchronization objects
    private final Lock lock;
    private Condition notEmptyCond;

    // Thread pool objects
    private WorkHandler[] workHandlers;
    private ExecutorService executor;
    private int maxThreads;


    public WorkPool() {
        processesList = new LinkedList<>();
        processesStatus = new HashMap<>();
        lock = new ReentrantLock();
        notEmptyCond = lock.newCondition();
    }

    public void constructThreadPool() {
        maxThreads = ServerSettings.nrOfProcessThreads;

        /* Create a thread pool that spawns threads on demand */
        executor = Executors.newFixedThreadPool(maxThreads);

        /* Create arrays of work handlers and threads */
        workHandlers = new WorkHandler[maxThreads];

        /* Create a new work handler and a thread for each array element */
        for (int i=0; i<maxThreads; i++) {
            workHandlers[i] = new WorkHandler(this);
            executor.execute(workHandlers[i]);
        }
    }

    public LinkedList<ProcessCommand> getProcesses() {
        lock.lock();

        try {
            return new LinkedList<>(processesList);
        }  finally {
            lock.unlock();
        }

    }

    public void addWork(ProcessCommand command) {
        lock.lock();

        try {
            processesList.add(command);
            processesStatus.put(command, new ProcessStatus(command));
            notEmptyCond.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public ProcessCommand getProcess() {
        lock.lock();

        ProcessCommand processCommand = null;

        try {
            while (processesList.size() == 0) {
                notEmptyCond.await();
            }
            processCommand = processesList.poll();
        }
        catch (InterruptedException ex) {
            ErrorLogger.log("SYSTEM", "Error acquiring processes: " +
                    ex.getMessage());
        }
        finally {
            lock.unlock();
        }

        return processCommand;

    }

    public void cancelProcess(ProcessCommand processCommand) {
        lock.lock();

        try {
             /* Loop through all work handlers to check which handler possesses
             * the sought work */
            for (int i=0; i<maxThreads; i++) {
                ProcessCommand currentProcComm = workHandlers[i].getCurrentWork();

                /* Make sure it is the right process command */
                if (currentProcComm != null && currentProcComm.equals(processCommand)) {
                    Future<Response> submission = workHandlers[i]
                            .getFutureProcess();

                /* Make sure it is not already done or cancelled then cancel */
                    if (!submission.isDone() && !submission.isCancelled()) {
                        submission.cancel(true);
                    }

                    break;
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
            return processesStatus.get(process);
        } finally {
            lock.unlock();
        }
    }

    public int availableProcesses() {
        lock.lock();

        try {
            return processesList.size();
        } finally {
            lock.unlock();
        }
    }
}
