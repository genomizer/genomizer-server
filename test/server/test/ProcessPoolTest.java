package server.test;


import command.process.PutProcessCommand;
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
        processPool.addProcess(new PutProcessCommand());
        processPool.addProcess(new PutProcessCommand());
        processPool.addProcess(new PutProcessCommand());
        processPool.addProcess(new PutProcessCommand());
        processPool.addProcess(new PutProcessCommand());
        processPool.addProcess(new PutProcessCommand());
        processPool.addProcess(new PutProcessCommand());
        processPool.addProcess(new PutProcessCommand());
        processPool.addProcess(new PutProcessCommand());
    }

}
