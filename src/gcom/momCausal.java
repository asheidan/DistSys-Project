package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;
import gcom.HashVectorClock;

import java.io.Serializable;
import java.util.Vector;

public class momCausal extends momNonOrdered {
	private Vector<Message> messages;

	public momCausal (String id) {
		super(id);
		messages = new Vector<Message>();
	}

	@Override
	public void tick() {
		this.clock.tick();
	}

	private void sendMessage(Message message) {
		this.clock.tickKey(message.getSource().getID());
		sendToListeners(message);
	}

	@Override
	public void queueMessage(Message m) {
		if(checkBypass(m)) { 
			sendToListeners(m);
			return;
		}
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
				sendMessage(m);
				remove.add(m);
			}
		}

		for(Message m : remove) {
			messages.remove(m);
		}
		if(remove.size() > 0) { checkMessages(); }
	}

}
