package server.test;


import command.process.PutProcessCommand;
import org.junit.Before;
import org.junit.Test;
import server.WorkPool;

import static org.junit.Assert.assertTrue;

public class WorkHandlerTest {

    private WorkPool workPool;

    @Before
    public void setUp() {
        workPool = new WorkPool();
    }

    @Test
    public void testWorkPool() {
        workPool.addWork(new PutProcessCommand());
        workPool.addWork(new PutProcessCommand());
        workPool.addWork(new PutProcessCommand());
        workPool.addWork(new PutProcessCommand());

        assertTrue(workPool.availableProcesses() == 4);
    }
    

}
