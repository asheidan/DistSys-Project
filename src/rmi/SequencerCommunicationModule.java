package rmi;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import gcom.interfaces.MessageOrderingModule;
import gcom.interfaces.GCom.TYPE_MESSAGEORDERING;
import gcom.momNonOrdered;
import gcom.momCausal;

import gcom.Debug;
import javax.swing.JOptionPane;
import gcom.interfaces.RemoteObject;
import gcom.GroupDefinition;
import gcom.HashVectorClock;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.GComViewChangeListener;
import gcom.interfaces.Message.TYPE_MESSAGE;

import java.util.Hashtable;

public class SequencerCommunicationModule implements gcom.interfaces.CommunicationModule {

	private Hashtable<String, GroupSequencer> sequencers;
	private Registry registry;

	public SequencerCommunicationModule(Registry registry) {
		sequencers = new Hashtable<String, GroupSequencer>();
		this.registry = registry;
	}

	public void receive(Message m) {
		if(m.getMessageType() == TYPE_MESSAGE.SEQUENCE) {
			Debug.log(this, Debug.DEBUG, "Got: " + m.toString());
			Message msg = (Message)m.getMessage();
			GroupSequencer seq = sequencers.get(msg.getGroupName());
			if(seq == null) {
				seq = new GroupSequencer(getMom(msg.getGroupName()), this);
				sequencers.put(msg.getGroupName(), seq);
			}
			seq.sequence(m);
		}
		else {
			Debug.log(this, Debug.WARN, "Got unwanted messagetype: " + m.getMessageType().toString());
		}
	}

	private MessageOrderingModule getMom(String groupName) {
		MessageOrderingModule mom = null;
		try {
			RemoteObject ro = (RemoteObject)registry.lookup(groupName);
			GroupDefinition gd = (GroupDefinition)ro.getDefinition();
			TYPE_MESSAGEORDERING ordering = gd.getMessageOrderingType();
			if(ordering == TYPE_MESSAGEORDERING.CAUSALTOTAL) {
				mom = new momCausal("sequencer");
			} else {
				mom = new momNonOrdered("sequencer");
			}
		} catch(Exception e) {
			Debug.log(this, Debug.WARN, "Got exception when creating mom in sequencer.", e);
		}
		return mom;
	}

	public void send(Message message) {
		Debug.log(this, Debug.WARN, "Using unimplemented method send()");
	}

	public void send(Member member, Message message) {
		Debug.log(this, Debug.WARN, "Using unimplemented method send()");
	}

	public void sendBack(Member member, Message message) {
		try {
			Debug.log("Sequencer",Debug.ERROR, "Member: " + member + " Message: " + message);
			member.getRemoteObject().send(message);
		} catch(Exception e) {
			Debug.log("Sequencer",Debug.ERROR, "Got other exception", e);
		}
	}
	public void addGComViewChangeListener(GComViewChangeListener listener) {}
}
