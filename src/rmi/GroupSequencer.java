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

	public GroupSequencer(MessageOrderingModule mom, SequencerCommunicationModule com) {
		this.com = com;
		this.mom = mom;
		mom.addMessageListener(this);
		messages = new Hashtable<Integer, Integer>();
		latest = 0;
	}

	public void sequence(Message m) {
		mom.queueMessage(m);
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
			stamp(m, number);
		}

	private void stamp(Message m, Integer number) {
		HashVectorClock clock = m.getClock();
		clock.put("serialNo", number);
		com.sendBack(m.getSource(), (Message)m.getMessage());
	}

}

