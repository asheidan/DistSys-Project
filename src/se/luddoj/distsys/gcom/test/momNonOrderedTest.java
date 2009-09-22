package se.luddoj.distsys.gcom.test;

import static org.junit.Assert.*;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import se.luddoj.distsys.gcom.*;
import se.luddoj.distsys.gcom.interfaces.MessageListener;
import se.luddoj.distsys.gcom.interfaces.MessageInterface.TYPE_MESSAGE;

public class momNonOrderedTest {

	private momNonOrdered mom;
	
	@Before
	public void setUp() {
		mom = new momNonOrdered();
	}
	
	@Test
	public void testAddMessageListener() {
		HashVectorClock clock = new HashVectorClock(0);
		Member source = new Member("123", "test");
		final Message m = new Message(clock, "group1", source, "test data", TYPE_MESSAGE.APPLICATION);
		testMessageListener listener = new testMessageListener();
		
		mom.addMessageListener(listener);
		mom.queueMessage(m);
		
		assertEquals(listener.recieved, m);
	}

	private class testMessageListener implements MessageListener {
		public Serializable recieved;
		
		@Override
		public void messageReceived(Serializable message) {
			recieved = message;
		}	
	}
	
}
