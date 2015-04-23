package server;

import command.ProcessCommand;
import command.ProcessStatus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by c12smi on 4/21/15.
 */
public class WorkPool {
    private LinkedList<ProcessCommand> processesList;
    private HashMap<ProcessCommand,ProcessStatus> processesStatus;

    private final Lock lock;
    private Condition notEmptyCond;


    public WorkPool() {
        processesList = new LinkedList<>();
        processesStatus = new HashMap<>();
        lock = new ReentrantLock();
        notEmptyCond = lock.newCondition();
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

    public void removeProcess(ProcessCommand processCommand) {
        lock.lock();

        try {
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
