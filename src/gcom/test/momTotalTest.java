package gcom.test;

import static org.junit.Assert.*;

import gcom.*;
import gcom.interfaces.MessageListener;
import gcom.interfaces.Message.TYPE_MESSAGE;

import java.io.Serializable;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class momTotalTest {

	private momTotal mom;
	
	@Before
	public void setUp() {
		Member me = new Member("0", "test0");
		mom = new momTotal(me);
	}
	
	@Test
	public void testFirstMessageDelayed() {
		Member source1 = new Member("123", "test1");
		
		testMessageListener listener = new testMessageListener();
		mom.addMessageListener(listener);

		Message m0 = getMessage(source1, 0);
		Message m1 = getMessage(source1, 1);
		Message m2 = getMessage(source1, 2);
		Message m3 = getMessage(source1, 3);

		mom.queueMessage(m1);
		mom.queueMessage(m0);
		mom.queueMessage(m3);
		mom.queueMessage(m2);

		assertEquals("All messages recieved", 3, listener.recieved.size());
		assertEquals(m1, listener.recieved.get(0));
		assertEquals(m2, listener.recieved.get(1));
		assertEquals(m3, listener.recieved.get(2));
	}
	
	@Test
	public void testTwoSources() {
		Member source1 = new Member("1", "test1");
		Member source2 = new Member("2", "test2");
		
		testMessageListener listener = new testMessageListener();
		mom.addMessageListener(listener);

		Message m0 = getMessage(source1, 0);
		Message m1 = getMessage(source2, 1);
		Message m2 = getMessage(source1, 2);
		Message m3 = getMessage(source2, 3);

		mom.queueMessage(m0);
		mom.queueMessage(m1);
		mom.queueMessage(m2);
		mom.queueMessage(m3);

		assertEquals("All messages recieved", 4, listener.recieved.size());
		assertEquals(m0, listener.recieved.get(0));
		assertEquals(m1, listener.recieved.get(1));
		assertEquals(m2, listener.recieved.get(2));
		assertEquals(m3, listener.recieved.get(3));
	}

	private Message getMessage(Member source, int ticks) {
		HashVectorClock clock = new HashVectorClock("serialNo");
		for(int i=0; i<ticks; i++) {
			clock.tick();
		}
		Message m = new Message(clock, "group1", source, "test data"+ticks, TYPE_MESSAGE.APPLICATION);
		return m;
	}

	private class testMessageListener implements MessageListener {
		public Vector<Serializable> recieved;
		
		public testMessageListener() {
			recieved = new Vector<Serializable>();
		}

		@Override
		public void messageReceived(Serializable message) {
			recieved.add(message);
		}	
	}
	
}
