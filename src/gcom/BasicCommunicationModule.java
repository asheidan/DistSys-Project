package gcom;

import org.apache.log4j.Logger;

import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;
import gcom.interfaces.Group;

public class BasicCommunicationModule implements gcom.interfaces.CommunicationModule {
	private Logger logger = Logger.getLogger("gcom.CommunicationModule.Basic");
	
	private MessageOrderingModule mom;
	private Group group;
	
	public BasicCommunicationModule(MessageOrderingModule mom, Group group) {
		this.mom = mom;
		this.group = group;
	}
	
	@Override
	public void receive(Message m) {
		/* Everything needs to get ordered (don't want messages *
		 * from parted members                                  */
		mom.queueMessage(m);
	}

	@Override
	public void send(Message message) {
		for(Member m : group.listMembers()) {
			logger.debug("Sending message to: " + m.toString());
			m.getRemoteObject().send(message);
		}
	}

}
