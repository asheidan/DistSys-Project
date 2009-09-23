package gcom.test;

import static org.junit.Assert.*;
import gcom.BasicCommunicationModule;
import gcom.momNonOrdered;
import gcom.interfaces.Message;
import gcom.interfaces.MessageListener;
import gcom.interfaces.MessageOrderingModule;

import org.junit.Before;
import org.junit.Test;

public class BasicCommunicationModuleTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBasicCommunicationModule() {
		assertNotNull("Constructor should work", new BasicCommunicationModule(new momNonOrdered(), null));
	}

	@Test
	public void testReceive() {
		fail("Not yet implemented");
	}

	@Test
	public void testSend() {
		fail("Not yet implemented");
	}
	
	private class TestException extends Throwable {
		private Message message;
		public TestException(Message m) { this.message = m; }
		public Message getPayload() { return message; }
	}
	
	private class MessageOrderingModuleMockup implements MessageOrderingModule {
		@Override
		public void addMessageListener(MessageListener listener) {
		}

		@Override
		public void queueMessage(Message m) {
			throw new TestException(m);
		}
		
	}

}
