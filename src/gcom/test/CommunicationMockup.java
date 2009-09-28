package gcom.test;

import gcom.interfaces.CommunicationModule;
import gcom.interfaces.Message;

public class CommunicationMockup implements CommunicationModule {
	public Message lastMessage = null;
	@Override
	public void receive(Message m) {
		lastMessage = m;
	}

	@Override
	public void send(Message m) {
		throw new java.lang.RuntimeException("You're using a mockup!");
	}
	
}

