package server;

import command.Process;
import command.process.PutProcessCommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by c12smi on 4/21/15.
 */
public class WorkPool {
    private LinkedList<PutProcessCommand> processesList;
    private HashMap<PutProcessCommand, Process> processesStatus;

    private final Lock lock;
    private Condition notEmptyCond;


    public WorkPool() {
        processesList = new LinkedList<>();
        processesStatus = new HashMap<>();
        lock = new ReentrantLock();
        notEmptyCond = lock.newCondition();
    }

    public LinkedList<PutProcessCommand> getProcesses() {
        lock.lock();

        try {
            return new LinkedList<>(processesList);
        }  finally {
            lock.unlock();
        }

    }

    public void addWork(PutProcessCommand command) {
        lock.lock();

        try {
            processesList.add(command);
            processesStatus.put(command, new Process(command));
            notEmptyCond.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public PutProcessCommand getProcess() {
        lock.lock();

        PutProcessCommand putProcessCommand = null;

        try {
            while (processesList.size() == 0) {
                notEmptyCond.await();
            }
            putProcessCommand = processesList.poll();
        }
        catch (InterruptedException ex) {
            ErrorLogger.log("SYSTEM", "Error acquiring processes: " +
                    ex.getMessage());
        }
        finally {
            lock.unlock();
        }

        return putProcessCommand;

    }

    public void removeProcess(PutProcessCommand putProcessCommand) {
        lock.lock();

        try {
            processesList.remove(putProcessCommand);
            processesStatus.remove(putProcessCommand);
        } finally {
            lock.unlock();
        }
    }

    public command.Process getProcessStatus(PutProcessCommand process) {
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
