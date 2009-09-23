package gcom;

import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;
import gcom.interfaces.Group;

public class BasicCommunicationModule implements gcom.interfaces.CommunicationModule {

	private MessageOrderingModule mom;
	private Group group;
	
	public BasicCommunicationModule(MessageOrderingModule mom, Group group) {
		this.mom = mom;
		this.group = group;
	}
	
	@Override
	public void receive(Message m) {
		mom.queueMessage(m);
	}

	@Override
	public void send(Message message) {
		// TODO Auto-generated method stub
		for(Member m : group.listMembers()) {
			
		}
	}

}
