package server.test;


import org.junit.Before;
import org.junit.Ignore;

import server.ProcessPool;

@Ignore
public class ProcessPoolTest {

    private ProcessPool processPool;

    @Before
    public void setUp() {
        processPool = new ProcessPool(1);
    }


}
