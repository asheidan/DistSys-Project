package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;
import gcom.HashVectorClock;

import java.util.Hashtable;
import java.io.Serializable;
import java.util.Vector;

public class momTotal implements MessageOrderingModule {
	private Vector<GComMessageListener> listeners;
	private Vector<Message> messages;
	private Integer lastDelivered;
	private HashVectorClock clock;

	public momTotal(String id) {
		listeners = new Vector<GComMessageListener>();
		messages = new Vector<Message>();
		this.clock = new HashVectorClock(id);
	}

	@Override
	public HashVectorClock getClock() {
		return this.clock;	
	}
	
	@Override
	public void tick() {
		// Are we the sequencer?
		//this.clock.tick();
	}
	
	@Override
	public void addMessageListener(GComMessageListener listener) {
		listeners.add(listener);
	}

	private void sendToListeners(Message message) {
		Integer value = message.getClock().getValue("serialNo");
		this.lastDelivered++;

		for(GComMessageListener l : listeners) {
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		Integer value = m.getClock().getValue("serialNo");
		if(this.lastDelivered == null) {
			this.lastDelivered = value-1;
			sendToListeners(m);
			return;
		}
		if(value < this.lastDelivered) { return; }
		messages.add(m);
		checkMessages();
	}
	
	private void checkMessages() {
		Vector<Message> remove = new Vector<Message>();
		
		for(Message m : messages) {
			Integer value = m.getClock().getValue("serialNo");
			if(value == this.lastDelivered+1) {
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
