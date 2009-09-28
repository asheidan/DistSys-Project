package gcom.test;

import static org.junit.Assert.*;

import java.io.Serializable;

import gcom.HashVectorClock;
import gcom.RemoteObject;
import gcom.interfaces.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoteObjectTest {
	RemoteObject ro;
	CommunicationMockup com;
	
	@Before
	public void setUp() throws Exception {
		com = new CommunicationMockup();
		ro = new RemoteObject(com);
		ro.start();
	}

	@Test
	public void testRemoteObject() {
		assertNotNull(ro);
	}

	@Test
	public void testSend() throws InterruptedException {
		Message m = new MessageMockup();
		ro.send(m);
		Thread.sleep(1000);
		assertEquals(m, com.lastMessage);
	}
	
	@After
	public void tearDown() {
		ro.stop();
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
