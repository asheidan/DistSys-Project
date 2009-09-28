package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;

import java.util.Hashtable;
import java.io.Serializable;
import java.util.Vector;

public class momFIFO implements MessageOrderingModule {
	private Vector<MessageListener> listeners;
	private Vector<Message> messages;
	private Hashtable<Object, Integer> lastDelivered;

	public momFIFO () {
		listeners = new Vector<MessageListener>();
		messages = new Vector<Message>();
		lastDelivered = new Hashtable<Object, Integer>();
	}
	
	@Override
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	private void sendToListeners(Message message) {
		String key = message.getSource().getID();
		Integer value = message.getClock().getValue(key);
		lastDelivered.put(key, value);

		for(MessageListener l : listeners) {
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		String key = m.getSource().getID();
		Integer value = m.getClock().getValue(key);
		Integer highest = lastDelivered.get(key);
		if(highest == null) {
			sendToListeners(m);
			return;
		}
		messages.add(m);
		checkMessages();
	}
	
	private void checkMessages() {
		Vector<Message> remove = new Vector<Message>();

		for(Message m : messages) {
			String key = m.getSource().getID();
			Integer value = m.getClock().getValue(key);
			Integer highest = lastDelivered.get(key);
			if(value == highest+1) {
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
