package rmi;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import gcom.Debug;
import javax.swing.JOptionPane;
import gcom.RemoteObject;
import gcom.GroupDefinition;
import gcom.HashVectorClock;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.GComViewChangeListener;

import java.util.Hashtable;

public class Sequencer extends RMIServer {

	public static void main(String [] args) {
		try {
			new Sequencer(Integer.valueOf(args[0]));
			JOptionPane.showMessageDialog(null,"RMIRegistry is launched\nListening on port " + args[0],"RMI:" + args[0],JOptionPane.INFORMATION_MESSAGE);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			Debug.log("RMIServer",Debug.ERROR, "You must supply a portnumber");
		}
		catch(RemoteException e) {
			Debug.log("RMIServer",Debug.ERROR, "Got remote exception", e);
		}
		System.exit(0);
	}

	public Sequencer(int port) throws RemoteException {
		super(port);
		Debug.log(this,Debug.DEBUG,"Returned from superconstructor");

		SequencerCommunicationModule com = new SequencerCommunicationModule();
		GroupDefinition group = new gcom.GroupDefinition("sequencer");
		RemoteObject ro = new RemoteObject(com, group);
		try {
			registry.bind("sequencer", UnicastRemoteObject.exportObject(ro, 0));
		} catch(Exception e) {
			Debug.log("Sequencer",Debug.ERROR, "Got exception", e);
		}
	}

	private class SequencerCommunicationModule implements gcom.interfaces.CommunicationModule {

		private Hashtable<String, groupSequencer> sequencers;

		public SequencerCommunicationModule() {
			sequencers = new Hashtable<String, groupSequencer>();
		}

		public void receive(Message m) {
			groupSequencer seq = sequencers.get(m.getGroupName());
			if(seq == null) {
				seq = new groupSequencer();
				sequencers.put(m.getGroupName(), seq);
			}
			send(seq.sequence(m));
		}

		public void send(Message message) {
			Member member = message.getSource();
			try {
				member.getRemoteObject().send(message);
			} catch(Exception e) {
				Debug.log("Sequencer",Debug.ERROR, "Got other exception", e);
			}
		}
		public void addGComViewChangeListener(GComViewChangeListener listener) {}
	}

	private class groupSequencer {
		private Hashtable<Integer, Integer> messages;
		private int latest;

		public groupSequencer() {
			messages = new Hashtable<Integer, Integer>();
			latest = 0;
		}

		public Message sequence(Message m) {
			Integer hash = m.hashCode();
			Integer number = messages.get(hash);
			if(number == null) {
				latest++;
				number = latest;
				messages.put(hash, number);
			}
			return stamp(m, number);
		}

		private Message stamp(Message m, Integer number) {
			HashVectorClock clock = m.getClock();
			clock.put("serialNo", number);
			return m;
		}

	}
}
