package gcom.test;

import static org.junit.Assert.*;

import gcom.*;
import gcom.interfaces.MessageListener;
import gcom.interfaces.Message.TYPE_MESSAGE;

import java.io.Serializable;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class momFIFOTest {

	private momFIFO mom;
	
	@Before
	public void setUp() {
		mom = new momFIFO();
	}
	
	@Test
	public void testOrderedMessages() {
		Member source = new Member("123", "test");
		HashVectorClock clock0 = new HashVectorClock(source.getID());
		HashVectorClock clock1 = new HashVectorClock(source.getID());
		HashVectorClock clock2 = new HashVectorClock(source.getID());
		
		clock1.tick();
		clock2.tick();
		clock2.tick();

		Message m0 = new Message(clock0, "group1", source, "test data0", TYPE_MESSAGE.APPLICATION);
		Message m1 = new Message(clock1, "group1", source, "test data1", TYPE_MESSAGE.APPLICATION);
		Message m2 = new Message(clock2, "group1", source, "test data2", TYPE_MESSAGE.APPLICATION);
		
		testMessageListener listener = new testMessageListener();
		mom.addMessageListener(listener);

		mom.queueMessage(m0);
		mom.queueMessage(m1);
		mom.queueMessage(m2);
		
		assertEquals(listener.recieved.get(0), m0);
		assertEquals(listener.recieved.get(1), m1);
		assertEquals(listener.recieved.get(2), m2);
	}

	@Test
	public void testUnorderedMessages() {
		Member source = new Member("123", "test");
		HashVectorClock clock0 = new HashVectorClock(source.getID());
		HashVectorClock clock1 = new HashVectorClock(source.getID());
		HashVectorClock clock2 = new HashVectorClock(source.getID());

		clock1.tick();
		clock2.tick();
		clock2.tick();

		Message m0 = new Message(clock0, "group1", source, "test data0", TYPE_MESSAGE.APPLICATION);
		Message m1 = new Message(clock1, "group1", source, "test data1", TYPE_MESSAGE.APPLICATION);
		Message m2 = new Message(clock2, "group1", source, "test data2", TYPE_MESSAGE.APPLICATION);
		
		testMessageListener listener = new testMessageListener();
		mom.addMessageListener(listener);

		mom.queueMessage(m0);
		mom.queueMessage(m2);
		mom.queueMessage(m1);
		
		assertEquals(listener.recieved.get(0), m0);
		assertEquals(listener.recieved.get(1), m1);
		assertEquals(listener.recieved.get(2), m2);
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
