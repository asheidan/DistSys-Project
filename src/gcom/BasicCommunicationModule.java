package gcom;

import java.rmi.RemoteException;
import java.rmi.ConnectException;

import org.apache.log4j.Logger;

import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;

public class BasicCommunicationModule implements gcom.interfaces.CommunicationModule {
	private Logger logger = Logger.getLogger("gcom.CommunicationModule.Basic");
	
	private MessageOrderingModule mom;
	private GroupManagementModule gmm;
	private String group;
	private String processID;
	
	public BasicCommunicationModule(MessageOrderingModule mom, GroupManagementModule gmm, String groupName, String processID) {
		this.mom = mom;
		this.gmm = gmm;
		this.group = groupName;
		this.processID = processID;
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
			if(!processID.equals(m.getID())) {
				logger.debug("Sending message to: " + m.toString());
				try {
					m.getRemoteObject().send(message);
				}
				catch(ConnectException e) {
					Debug.log(this, Debug.DEBUG, "Connection refused to " + m);
					// TODO: Remove user from group and tell other members of group
				}
				catch(RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
