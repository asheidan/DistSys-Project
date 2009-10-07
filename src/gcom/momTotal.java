package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;

import java.util.Hashtable;
import java.io.Serializable;
import java.util.Vector;

public class momTotal implements MessageOrderingModule {
	private Vector<MessageListener> listeners;
	private Vector<Message> messages;
	private Integer lastDelivered;

	public momTotal () {
		listeners = new Vector<MessageListener>();
		messages = new Vector<Message>();
	}
	
	@Override
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	private void sendToListeners(Message message) {
		Integer value = message.getClock().getValue("serialNo");
		this.lastDelivered++;

		for(MessageListener l : listeners) {
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
