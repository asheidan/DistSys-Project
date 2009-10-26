package test.gcom;

import static org.junit.Assert.*;

import gcom.*;
import gcom.interfaces.MessageListener;
import gcom.interfaces.Message.TYPE_MESSAGE;
import gcom.interfaces.GComMessageListener;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

public class momNonOrderedTest {

	private momNonOrdered mom;
	
	@Before
	public void setUp() {
		mom = new momNonOrdered("0");
	}
	
	@Test
	public void testAddMessageListener() {
		Member source = new Member("123", "test");
		HashVectorClock clock = new HashVectorClock(source.getID());
		final Message m = new Message(clock, "group1", source, "test data", TYPE_MESSAGE.APPLICATION);
		testMessageListener listener = new testMessageListener();
		
		mom.addMessageListener(listener);
		mom.queueMessage(m);
		
		assertEquals(listener.recieved, m);
	}

	private class testMessageListener implements GComMessageListener {
		public Serializable recieved;
		
		@Override
		public void messageReceived(gcom.interfaces.Message message) {
			recieved = message;
		}	
	}
	
}
