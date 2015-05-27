package server.test;


import command.*;
import command.Process;
import command.process.PutProcessCommand;
import org.junit.Before;
import org.junit.Ignore;

import org.junit.Test;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import server.ProcessPool;

import java.util.concurrent.Callable;

@Ignore
public class ProcessPoolTest {

    private ProcessPool processPool;

    @Before
    public void setUp() {
        processPool = new ProcessPool(1);
    }

    private Process makeDummyProcess() {
        return new Process();
    }

    private Callable<Response> makeDummyCallable() {
        return new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                return new ProcessResponse(HttpStatusCode.OK);
            }
        };
    }

    @Test
    public void testProcessSubmission() {
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        processPool.addProcess(makeDummyProcess(), makeDummyCallable());
    }

}
