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

public class momCausalTest {

	private momCausal mom;
	
	@Before
	public void setUp() {
		mom = new momCausal("0");
	}
	
	@Test
	public void testFirstMessageDelayed() {
		Member source1 = new Member("1", "test1");
		
		testMessageListener listener = new testMessageListener();
		mom.addMessageListener(listener);

		Message m0 = getMessage(source1, 1);
		Message m1 = getMessage(source1, 2);
		Message m2 = getMessage(source1, 3);

		mom.queueMessage(m1);
		mom.queueMessage(m0);
		mom.queueMessage(m2);

		assertEquals("All messages recieved", 2, listener.recieved.size());
		assertEquals(m1, listener.recieved.get(0));
		assertEquals(m2, listener.recieved.get(1));
	}

	@Test
	public void testThreeSenders() {
		Member source1 = new Member("1", "test1");
		Member source2 = new Member("2", "test2");
		Member source3 = new Member("3", "test3");
		
		testMessageListener listener = new testMessageListener();
		mom.addMessageListener(listener);

		Member[] s = {source1, source2, source3};
		Message m0_1_0 = getMessageArray(source2, s, new int[] {0,1,0});
		Message m1_2_0 = getMessageArray(source1, s, new int[] {1,2,0});
		Message m1_3_0 = getMessageArray(source2, s, new int[] {1,3,0});
		Message m0_2_0 = getMessageArray(source2, s, new int[] {0,2,0});

		mom.queueMessage(m0_1_0);
		mom.queueMessage(m1_2_0);
		mom.queueMessage(m1_3_0);
		mom.queueMessage(m0_2_0);

		assertEquals("All messages recieved", 4, listener.recieved.size());
		assertEquals(m0_1_0, listener.recieved.get(0));
		assertEquals(m0_2_0, listener.recieved.get(1));
		assertEquals(m1_2_0, listener.recieved.get(2));
		assertEquals(m1_3_0, listener.recieved.get(3));
	}

	private Message getMessage(Member source, int ticks) {
		HashVectorClock clock = new HashVectorClock(source.getID());
		for(int i=0; i<ticks; i++) {
			clock.tick();
		}
		Message m = new Message(clock, "group1", source, "test data"+ticks, TYPE_MESSAGE.APPLICATION);
		return m;
	}

	private Message getMessageArray(Member source, Member[] sources, int[] ticks) {
		HashVectorClock clock = new HashVectorClock(source.getID());
		for(int i=0; i<sources.length; i++) {
			for(int j=0; j<ticks[i]; j++) {
				clock.tickKey(sources[i].getID());
			}
		}
		Message m = new Message(clock, "group1", source, "test data", TYPE_MESSAGE.APPLICATION);
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
