package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;
import gcom.HashVectorClock;

import java.io.Serializable;
import java.util.Vector;
import org.apache.log4j.Level;

public class momNonOrdered implements MessageOrderingModule {
	private Vector<GComMessageListener> listeners;
	private HashVectorClock clock;

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
		// WE DO NOT CARE ABOUT TICKS AND CLOCKS
		// WE ARE UNORDERED
		// ORDER IS FOR THE WEAK
	}

	@Override
	public void addMessageListener(GComMessageListener listener) {
		listeners.add(listener);
	}

	private void sendToListeners(Message message) {
		// TODO: This needs to still be a Message
		for(GComMessageListener l : listeners) {
			Debug.log("gcom.momNonOrdered", Level.DEBUG, "Sent message to: " + l.toString());
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		Debug.log("gcom.momNonOrdered", Level.DEBUG, "Queued message: " + m.toString());
		sendToListeners(m);
	}

}
