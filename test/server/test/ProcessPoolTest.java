package server.test;

import command.Process;
import static org.junit.Assert.*;

import org.junit.*;

import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import server.ProcessPool;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ProcessPoolTest {

    private static ProcessPool processPool;

    private static Process makeDummyProcess() {
        return new Process();
    }

    private static Callable<Response> makeDummyCallable() {
        return new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                Thread.sleep(5*60*1000);
                return new ProcessResponse(HttpStatusCode.OK);
            }
        };
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        processPool = new ProcessPool(5);
    }

    @Before
    public void setUp() {
        for (int i = 0; i < 9; ++i) {
            processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        }
    }

    @After
    public void tearDown() {
        cancelAllProcesses();
    }

    private void cancelAllProcesses() {
        List<Process> processList = processPool.getProcesses();
        for (Process process : processList) {
            UUID uuid = UUID.fromString(process.PID);
            processPool.cancelProcess(uuid);
        }
    }

    @Test
    public void testProcessSubmission() {
        List<Process> processList = processPool.getProcesses();
        assertEquals(9, processList.size());
    }

    @Test
    public void testProcessCancellation() {
        List<Process> processList = processPool.getProcesses();
        UUID uuid = UUID.fromString(processList.get(0).PID);
        processPool.cancelProcess(uuid);

        processList = processPool.getProcesses();
        assertEquals(8, processList.size());

        cancelAllProcesses();

        processList = processPool.getProcesses();
        assertEquals(0, processList.size());
    }

    @Test
    public void testProcessStatus() throws InterruptedException {
        cancelAllProcesses();

        UUID pid = processPool.addProcess(makeDummyProcess(), makeDummyCallable());
        Thread.sleep(100);

        assertEquals(Process.STATUS_STARTED, processPool.getProcessStatus(pid).status);
        processPool.cancelProcess(pid);
        assertEquals(0, processPool.getProcesses().size());

        pid = processPool.addProcess(makeDummyProcess(), new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                throw new NullPointerException();
            }
        });
        Thread.sleep(200);
        assertEquals(Process.STATUS_CRASHED, processPool.getProcessStatus(pid).status);
    }

}
