package gcom.test;

import static org.junit.Assert.*;

import java.io.Serializable;

import gcom.BasicCommunicationModule;
import gcom.HashVectorClock;
import gcom.interfaces.*;

import org.junit.Before;
import org.junit.Test;

public class BasicCommunicationModuleTest {
	Group group;
	MessageOrderingMockup mom;
	BasicCommunicationModule com;
	
	
	@Before
	public void setUp() throws Exception {
		group = new gcom.Group(new gcom.GroupDefinition("De små nissarna"));
		mom = new MessageOrderingMockup();
		com = new BasicCommunicationModule(mom,group);
	}

	@Test
	public void testBasicCommunicationModule() {
		assertNotNull("Constructor should work", com);
	}

	@SuppressWarnings("serial")
	@Test
	public void testReceive() {
		Message m = new Message() {
			
			@Override
			public TYPE_MESSAGE getMessageType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Serializable getMessage() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getGroupName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HashVectorClock getClock() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		com.receive(m);
		assertEquals(m, mom.lastMessage);
	}

	@Test
	public void testSend() {
		fail("Not yet implemented");
	}
	
	public class MessageOrderingMockup implements MessageOrderingModule {
		public Message lastMessage = null;
		
		@Override
		public void addMessageListener(MessageListener listener) {
		}

		@Override
		public void queueMessage(Message m) {
			lastMessage = m;
		}
		
	}

}
