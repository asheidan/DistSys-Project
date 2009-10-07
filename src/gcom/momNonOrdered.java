package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;

import java.io.Serializable;
import java.util.Vector;

public class momNonOrdered implements MessageOrderingModule {
	private Vector<MessageListener> listeners;
	
	public momNonOrdered() {
		listeners = new Vector<MessageListener>();
	}
	
	@Override
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	private void sendToListeners(Message message) {
		// TODO: This needs to still be a Message
		for(MessageListener l : listeners) {
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		sendToListeners(m);
	}

}
