package server;

import command.ProcessCommand;
import command.ProcessStatus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by c12smi on 4/21/15.
 */
public class WorkPool {
    private Queue<ProcessCommand> workQueue;
    private HashMap<ProcessCommand,ProcessStatus> processStatus;


    public WorkPool() {
        workQueue = new LinkedList<>();
        processStatus = new HashMap<>();
    }

    public synchronized HashMap<ProcessCommand,ProcessStatus> getProcesses() {
        return processStatus;
    }

    public synchronized void addWork(ProcessCommand command) {
        workQueue.add(command);
        processStatus.put(command, new ProcessStatus(command));
    }

    public synchronized ProcessCommand getProcess() {
        return workQueue.poll();
    }

    public synchronized void removeProcess(ProcessCommand processCommand) {
        workQueue.remove(processCommand);
        processStatus.remove(processCommand);
    }

    public synchronized ProcessStatus getProcessStatus(ProcessCommand process) {
        return processStatus.get(process);
    }


}
