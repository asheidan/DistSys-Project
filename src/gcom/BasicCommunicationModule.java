package gcom;

import java.rmi.RemoteException;
import java.rmi.ConnectException;

import java.util.Vector;

import org.apache.log4j.Logger;

import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.GComViewChangeListener;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;

public class BasicCommunicationModule implements gcom.interfaces.CommunicationModule {
	private Logger logger = Logger.getLogger("gcom.CommunicationModule.Basic");
	private MessageOrderingModule mom;
	private GroupManagementModule gmm;
	private String group;
	private String processID;

	private Vector<GComViewChangeListener> listeners = new Vector<GComViewChangeListener>();
	
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
					// CHANGED: Removes user from group and tell other members
					looseMember(m);
				}
				catch(RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void looseMember(Member member) {
		for(GComViewChangeListener listener : listeners) {
			listener.lostMember(group, member);
		}
	}

	public void addGComViewChangeListener(GComViewChangeListener listener) {
		listeners.add(listener);
	}
}
