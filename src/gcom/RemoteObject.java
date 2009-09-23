package gcom;

import gcom.interfaces.CommunicationModule;
import gcom.interfaces.Message;

public class RemoteObject implements gcom.interfaces.RemoteObject {
	private CommunicationModule com;
	
	public RemoteObject(CommunicationModule com) {
		this.com = com;
	}
	
	@Override
	public void send(Message m) {
		com.receive(m);
	}

}
