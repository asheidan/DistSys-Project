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
		mom = new momTotal();
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
