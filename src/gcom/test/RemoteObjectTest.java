package gcom.test;

import static org.junit.Assert.*;

import java.io.Serializable;

import gcom.HashVectorClock;
import gcom.RemoteObject;
import gcom.interfaces.*;

import org.junit.Before;
import org.junit.Test;

public class RemoteObjectTest {
	RemoteObject ro;
	CommunicationMockup com;
	
	@Before
	public void setUp() throws Exception {
		com = new CommunicationMockup();
		ro = new RemoteObject(com);
	}

	@Test
	public void testRemoteObject() {
		assertNotNull(ro);
	}

	@Test
	public void testSend() {
		Message m = new MessageMockup();
		ro.send(m);
		assertEquals(m, com.lastMessage);
	}
	
	@SuppressWarnings("serial")
	private class MessageMockup implements Message {

		@Override
		public HashVectorClock getClock() {
			return null;
		}

		@Override
		public String getGroupName() {
			return null;
		}

		@Override
		public Serializable getMessage() {
			return null;
		}

		@Override
		public TYPE_MESSAGE getMessageType() {
			return null;
		}
		
	}
	
}
