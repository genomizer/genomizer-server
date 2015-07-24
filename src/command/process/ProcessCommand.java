package command.process;

import command.Process;
import command.ValidateException;
import response.HttpStatusCode;
import response.Response;
import server.ProcessPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * File:        ProcessCommand.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

public abstract class ProcessCommand {

    protected String expID;

    public void doProcess(ProcessPool pool, String rawFilesDir, String profileFilesDir)
            throws ExecutionException, InterruptedException {

        Collection<Future<Response>> futures = new ArrayList<>();
        for (Callable<Response> callable : getCallables(
                rawFilesDir,
                profileFilesDir)) {
            UUID uuid = pool.addProcess(
                    new Process(expID, "UNKNOWN_AUTHOR"),
                    callable);
            futures.add(pool.getFuture(uuid));
        }
        try {
            for (Future<Response> future : futures) {
                if (future.get().getCode() != HttpStatusCode.OK) {
                    throw new InterruptedException(future.get().getMessage());
                }
            }
        } catch (CancellationException e){
            throw new InterruptedException(e.getMessage());
        }
    }

    public void setExpID(String expID) {
        this.expID = expID;
    }

    protected abstract Collection<Callable<Response>> getCallables(
            String rawFilesDir,
            String profileFilesDir);

    public abstract void validate() throws ValidateException;
}
