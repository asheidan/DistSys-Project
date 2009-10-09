package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;
import gcom.HashVectorClock;

import java.util.Hashtable;
import java.io.Serializable;
import java.util.Vector;

public class momFIFO implements MessageOrderingModule {
	private Vector<GComMessageListener> listeners;
	private Vector<Message> messages;
	//private Hashtable<Object, Integer> lastDelivered;
	private HashVectorClock clock;

	public momFIFO(String id) {
		listeners = new Vector<GComMessageListener>();
		messages = new Vector<Message>();
		//lastDelivered = new Hashtable<Object, Integer>();
		this.clock = new HashVectorClock(id);
	}

	@Override
	public HashVectorClock getClock() {
		return this.clock;	
	}
	
	@Override
	public void tick() {
		this.clock.tick();
	}
	
	@Override
	public void addMessageListener(GComMessageListener listener) {
		listeners.add(listener);
	}

	private void sendToListeners(Message message) {
		String key = message.getSource().getID();
		Integer value = message.getClock().getValue(key);
		//lastDelivered.put(key, value);
		clock.put(key, value);

		for(GComMessageListener l : listeners) {
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		String key = m.getSource().getID();
		Integer value = m.getClock().getValue(key);
		Integer last = clock.getValue(key);
		if(last == null) {
			sendToListeners(m);
			return;
		}
		if(value < last) { return; }
		messages.add(m);
		checkMessages();
	}
	
	private void checkMessages() {
		Vector<Message> remove = new Vector<Message>();

		for(Message m : messages) {
			String key = m.getSource().getID();
			Integer value = m.getClock().getValue(key);
			Integer last = clock.getValue(key);
			if(value == last+1) {
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
