package server.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Queue;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import server.WorkHandler;

import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;

public class HeavyWorkQueueTest {



	private WorkHandler queue;

	@Before
	public void setUp() {

		queue = new WorkHandler();
	}


	@Test
	public void shouldHaveEmptyQueue(){

	}




}
