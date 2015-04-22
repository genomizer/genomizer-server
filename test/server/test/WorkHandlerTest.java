package server.test;


import command.ProcessCommand;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import server.WorkHandler;
import server.WorkPool;

public class WorkHandlerTest {

    private WorkPool workPool;

    @Before
    public void setUp() {
        workPool = new WorkPool();
    }

    @Test
    public void testWorkPool() {
        workPool.addWork(new ProcessCommand());
        workPool.addWork(new ProcessCommand());
        workPool.addWork(new ProcessCommand());
        workPool.addWork(new ProcessCommand());


        assertTrue(workPool.availableProcesses() == 4);

    }

    @Test
    public void testWorkPoolMultipleThreads() {
        workPool.addWork(new ProcessCommand());
        workPool.addWork(new ProcessCommand());
        workPool.addWork(new ProcessCommand());
        workPool.addWork(new ProcessCommand());

        new Thread(new WorkHandler(workPool)).start();
        new Thread(new WorkHandler(workPool)).start();

    }
}
