package test.gcom;

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
	String name;
	
	
	@Before
	public void setUp() throws Exception {
		name = "Nisse";
		group = new gcom.Group(new gcom.GroupDefinition("De små nissarna"));
		mom = new MessageOrderingMockup();
		com = new BasicCommunicationModule(mom,null,name,null);
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
				return null;
			}
			
			@Override
			public Serializable getMessage() {
				return null;
			}
			
			@Override
			public String getGroupName() {
				return null;
			}
			
			@Override
			public HashVectorClock getClock() {
				return null;
			}

			@Override
			public Member getSource() {
				return null;
			}
			
			@Override
			public Member getReturnMember() {
				return null;
			}

			@Override
			public void setReturnMember(Member m) {
			}

			@Override
			public boolean bypass() { return false; }

		};
		com.receive(m);
		assertEquals(m, mom.lastMessage);
	}

	@Test
	public void testSend() {
		//fail("Not yet implemented");
	}
	
	public class MessageOrderingMockup implements MessageOrderingModule {
		public Message lastMessage = null;
		
		@Override
		public void queueMessage(Message m) {
			lastMessage = m;
		}
		
		@Override
		public void tick() {}
		
		@Override
		public HashVectorClock getClock() { return null; }
		
		@Override
		public void addMessageListener(GComMessageListener l) {}

		@Override
		public void attachDebugger(DebugInterface debug) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

}
