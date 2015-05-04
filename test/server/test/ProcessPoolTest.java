package server.test;


import org.junit.Before;
import org.junit.Ignore;

import server.ProcessPool;


public class ProcessPoolTest {

    private ProcessPool processPool;

    @Ignore
    @Before
    public void setUp() {
        processPool = new ProcessPool(1);
    }


}
