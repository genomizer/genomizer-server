package command.process;

import command.ValidateException;
import response.HttpStatusCode;
import response.Response;
import server.Doorman;
import server.ProcessPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * File:        ProcessCommand.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

public abstract class ProcessCommand {

    public void doProcess(ProcessPool pool, String rawFilesDir, String profileFilesDir)
            throws ExecutionException, InterruptedException {

        Collection<Future<Response>> futures = new ArrayList<>();
        for (Callable<Response> callable : getCallables(
                rawFilesDir,
                profileFilesDir)) {
            UUID uuid = pool.addProcess(callable);
            futures.add(pool.getFuture(uuid));
        }
        for (Future<Response> future : futures) {
            if (future.get().getCode() != HttpStatusCode.OK) {
                throw new InterruptedException(future.get().getMessage());
            }
        }
    }

    protected abstract Collection<Callable<Response>> getCallables(
            String rawFilesDir,
            String profileFilesDir);

    public abstract void validate() throws ValidateException;
}
