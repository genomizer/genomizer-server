package command.process;

import command.ValidateException;
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

    public void doProcess(String rawFilesDir, String profileFilesDir)
            throws ExecutionException, InterruptedException {

        ProcessPool pool = Doorman.getProcessPool();
        Collection<Future<Response>> futures = new ArrayList<>();
        for (Callable<Response> callable: getCallables(rawFilesDir, profileFilesDir)) {
            UUID uuid = pool.addProcess(callable);
            futures.add(pool.getFuture(uuid));
        }
        for (Future<Response> future: futures) {
            future.get();
        }
    }

    protected abstract Collection<Callable<Response>> getCallables(
            String rawFilesDir,
            String profileFilesDir);

    public abstract void validate() throws ValidateException;
}
