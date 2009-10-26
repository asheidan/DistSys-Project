package gcom;


import gcom.interfaces.DebugInterface;
import java.util.Vector;

import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.GComViewChangeListener;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;
import java.rmi.ConnectException;
import java.rmi.RemoteException;

public class BasicCommunicationModule implements gcom.interfaces.CommunicationModule {
	
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
		Vector<Member> lostMembers = new Vector<Member>();
		Debug.log(this, Debug.DEBUG, "Sending message to group: " + gmm.listGroupMembers(group));
		Debug.log(this, Debug.DEBUG, "Sending: " + message.toString());
		for(Member m : gmm.listGroupMembers(group)) {
			Debug.log(this, Debug.DEBUG, "Sending message to: " + m.toString());
			try {
				m.getRemoteObject().send(message);
			}
			catch(ConnectException e) {
				Debug.log(this, Debug.DEBUG, "Connection refused to " + m);
				lostMembers.add(m);
			}
			catch(RemoteException e) {
				e.printStackTrace();
			}
		}
		for(Member m : lostMembers) {
			looseMember(m);
		}
	}
	
	@Override
	public void send(Member member, Message msg) {
		try {
			member.getRemoteObject().send(msg);
		}
		catch(ConnectException e) {
			Debug.log(this, Debug.DEBUG, "Connection refused to " + member);
			looseMember(member);
		}
		catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	private void looseMember(Member member) {
		for(GComViewChangeListener listener : listeners) {
			listener.lostMember(group, member);
		}
	}

	@Override
	public void addGComViewChangeListener(GComViewChangeListener listener) {
		listeners.add(listener);
	}

	public void attachDebugger(DebugInterface debug) {
		debug.attachDebugger(mom);
		mom = debug;
	}

}
