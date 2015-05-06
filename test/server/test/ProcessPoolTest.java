package server.test;


import command.ProcessCommand;
import org.junit.Before;
import org.junit.Ignore;

import org.junit.Test;
import server.ProcessPool;

@Ignore
public class ProcessPoolTest {

    private ProcessPool processPool;

    @Before
    public void setUp() {
        processPool = new ProcessPool(1);
    }

    @Test
    public void testProcessSubmission() {
        processPool.addProcess(new ProcessCommand());
        processPool.addProcess(new ProcessCommand());
        processPool.addProcess(new ProcessCommand());
        processPool.addProcess(new ProcessCommand());
        processPool.addProcess(new ProcessCommand());
        processPool.addProcess(new ProcessCommand());
        processPool.addProcess(new ProcessCommand());
        processPool.addProcess(new ProcessCommand());
        processPool.addProcess(new ProcessCommand());
    }

}
