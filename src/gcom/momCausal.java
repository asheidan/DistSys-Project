package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;

import java.io.Serializable;
import java.util.Vector;

public class momCausal implements MessageOrderingModule {
	private Vector<MessageListener> listeners;
	private Vector<Message> messages;
	private HashVectorClock clock;
	

	public momCausal () {
		listeners = new Vector<MessageListener>();
		messages = new Vector<Message>();
	}
	
	@Override
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	public void setVectorClock(HashVectorClock clock) {
		this.clock = clock;
	}

	private void sendToListeners(Message message) {
		this.clock.tickKey(message.getSource().getID());
		for(MessageListener l : listeners) {
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		if(this.clock.getValue(m.getSource().getID()) == null) {
			String id = m.getSource().getID();
			Integer val = m.getClock().getValue(id);
			this.clock.put(id, val-1);
		}
		messages.add(m);
		checkMessages();
	}
	
	private void checkMessages() {
		Vector<Message> remove = new Vector<Message>();

		for(Message m : messages) {
			String id = m.getSource().getID();
			HashVectorClock m_clock = m.getClock();
			if(m_clock.getValue(id) == this.clock.getValue(id)+1 && this.clock.excludedCompareTo(m_clock, id) >= 0) {
				sendToListeners(m);
				remove.add(m);
			}
		}

		for(Message m : remove) {
			messages.remove(m);
		}
		if(remove.size() > 0) { checkMessages(); }
	}

}
