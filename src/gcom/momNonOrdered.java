package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;
import gcom.HashVectorClock;

import java.io.Serializable;
import java.util.Vector;

public class momNonOrdered implements MessageOrderingModule {
	private Vector<MessageListener> listeners;
	private HashVectorClock clock;

	public momNonOrdered(Member me) {
		listeners = new Vector<MessageListener>();
		this.clock = new HashVectorClock(me.getID());
	}

	@Override
	public HashVectorClock getClock() {
		return this.clock;	
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
