package server.test;


import command.ProcessCommand;
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
    

}
