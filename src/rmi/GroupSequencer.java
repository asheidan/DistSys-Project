package rmi;

import gcom.Debug;
import gcom.interfaces.GComMessageListener;
import gcom.interfaces.MessageOrderingModule;
import gcom.interfaces.CommunicationModule;
import java.util.Hashtable;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.GroupDefinition;
import gcom.HashVectorClock;

public class GroupSequencer implements GComMessageListener {
	private Hashtable<Integer, Integer> messages;
	private int latest;
	private MessageOrderingModule mom;
	private SequencerCommunicationModule com;
	private Message recievedMessage;

	public GroupSequencer(MessageOrderingModule mom, SequencerCommunicationModule com) {
		this.com = com;
		this.mom = mom;
		mom.addMessageListener(this);
		messages = new Hashtable<Integer, Integer>();
		latest = 0;
	}

	public Message sequence(Message m) {
		mom.queueMessage(m);
		return recievedMessage;
	}

	@Override
		public void messageReceived(gcom.interfaces.Message m) {
			Integer hash = m.hashCode();
			Integer number = messages.get(hash);
			if(number == null) {
				latest++;
				number = latest;
				messages.put(hash, number);
			}
			recievedMessage = stamp(m, number);
		}

	private Message stamp(Message m, Integer number) {
		HashVectorClock clock = m.getClock();
		clock.put("serialNo", number);
		return m;
	}

}

