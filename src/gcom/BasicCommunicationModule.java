package gcom;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;
import gcom.interfaces.Group;

public class BasicCommunicationModule implements gcom.interfaces.CommunicationModule {
	private Logger logger = Logger.getLogger("gcom.CommunicationModule.Basic");
	
	private MessageOrderingModule mom;
	private GroupManagementModule gmm;
	private String group;
	
	public BasicCommunicationModule(MessageOrderingModule mom, GroupManagementModule gmm, String groupName) {
		this.mom = mom;
		this.gmm = gmm;
		this.group = groupName;
	}
	
	@Override
	public void receive(Message m) {
		/* Everything needs to get ordered (don't want messages *
		 * from parted members                                  */
		mom.queueMessage(m);
	}

	@Override
	public void send(Message message) {
		for(Member m : gmm.listGroupMembers(group)) {
			logger.debug("Sending message to: " + m.toString());
			try {
				m.getRemoteObject().send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
