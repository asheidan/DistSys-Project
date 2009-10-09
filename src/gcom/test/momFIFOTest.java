package gcom.test;

import static org.junit.Assert.*;

import gcom.*;
import gcom.interfaces.MessageListener;
import gcom.interfaces.Message.TYPE_MESSAGE;
import gcom.interfaces.GComMessageListener;

import java.io.Serializable;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class momFIFOTest {

	private momFIFO mom;
	
	@Before
	public void setUp() {
		mom = new momFIFO("0");
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
		
		assertEquals("All messages recieved", 3, listener.recieved.size());
		assertEquals(listener.recieved.get(0), m0);
		assertEquals(listener.recieved.get(1), m1);
		assertEquals(listener.recieved.get(2), m2);
	}

	@Test
	public void testUnorderedMessagesDifferentSenders() {
		Member source1 = new Member("123", "test1");
		Member source2 = new Member("345", "test2");
		
		testMessageListener listener = new testMessageListener();
		mom.addMessageListener(listener);

		Message m1_0 = getMessage(source1, 0);
		Message m1_1 = getMessage(source1, 1);
		Message m1_2 = getMessage(source1, 2);
		Message m2_0 = getMessage(source2, 0);
		Message m2_1 = getMessage(source2, 1);
		Message m2_2 = getMessage(source2, 2);

		mom.queueMessage(m1_0);
		mom.queueMessage(m2_0);
		mom.queueMessage(m2_1);
		mom.queueMessage(m1_2);
		mom.queueMessage(m2_2);
		mom.queueMessage(m1_1);

		assertEquals("All messages recieved", 6, listener.recieved.size());
		assertEquals(m1_0, listener.recieved.get(0));
		assertEquals(m2_0, listener.recieved.get(1));
		assertEquals(m2_1, listener.recieved.get(2));
		assertEquals(m2_2, listener.recieved.get(3));
		assertEquals(m1_1, listener.recieved.get(4));
		assertEquals(m1_2, listener.recieved.get(5));
	}

	@Test
	public void testFirstMessageDelayed() {
		Member source1 = new Member("123", "test1");
		
		testMessageListener listener = new testMessageListener();
		mom.addMessageListener(listener);

		Message m0 = getMessage(source1, 0);
		Message m1 = getMessage(source1, 1);
		Message m2 = getMessage(source1, 2);

		mom.queueMessage(m1);
		mom.queueMessage(m0);
		mom.queueMessage(m2);

		assertEquals("All messages recieved", 2, listener.recieved.size());
		assertEquals(m1, listener.recieved.get(0));
		assertEquals(m2, listener.recieved.get(1));
	}

	private Message getMessage(Member source, int ticks) {
		HashVectorClock clock = new HashVectorClock(source.getID());
		for(int i=0; i<ticks; i++) {
			clock.tick();
		}
		Message m = new Message(clock, "group1", source, "test data"+ticks, TYPE_MESSAGE.APPLICATION);
		return m;
	}

	private class testMessageListener implements GComMessageListener {
		public Vector<Serializable> recieved;
		
		public testMessageListener() {
			recieved = new Vector<Serializable>();
		}

		@Override
		public void messageReceived(gcom.interfaces.Message message) {
			recieved.add(message);
		}	
	}
	
}
