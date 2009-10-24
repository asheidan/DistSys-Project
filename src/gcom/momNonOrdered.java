package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;
import gcom.HashVectorClock;
import gcom.interfaces.Message.*; 

import java.io.Serializable;
import java.util.Vector;

public class momNonOrdered implements MessageOrderingModule {
	protected Vector<GComMessageListener> listeners;
	protected HashVectorClock clock;

	public momNonOrdered(String id) {
		listeners = new Vector<GComMessageListener>();
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

	protected void sendToListeners(Message message) {
		for(GComMessageListener l : listeners) {
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		sendToListeners(m);
	}

}
