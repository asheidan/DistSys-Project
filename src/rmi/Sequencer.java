package rmi;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import gcom.RemoteObject;
import gcom.GroupDefinition;
import gcom.HashVectorClock;
import gcom.interfaces.Member;
import gcom.interfaces.Message;

import java.util.Hashtable;

public class Sequencer extends RMIServer {

	public Sequencer(int port) {
		super(port);

		SequencerCommunicationModule com = new SequencerCommunicationModule();
		GroupDefinition group = new gcom.GroupDefinition("sequencer");
		RemoteObject ro = new RemoteObject(com, group);
		try {
			registry.bind("sequencer", UnicastRemoteObject.exportObject(ro, 0));
		} catch(Exception e) {

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
			
			}
		}

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
