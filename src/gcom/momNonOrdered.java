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
		// WE DO NOT CARE ABOUT TICKS AND CLOCKS
		// WE ARE UNORDERED
		// ORDER IS FOR THE WEAK
	}

	@Override
	public void addMessageListener(GComMessageListener listener) {
		listeners.add(listener);
	}

	protected void sendToListeners(Message message) {
		for(GComMessageListener l : listeners) {
			Debug.log("gcom.momNonOrdered", Debug.DEBUG, "Sent message to: " + l.toString());
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		Debug.log("gcom.momNonOrdered", Debug.DEBUG, "Queued message: " + m.toString());
		sendToListeners(m);
	}


	protected boolean checkBypass(Message m) {
		TYPE_MESSAGE type = m.getMessageType();
		if(type == TYPE_MESSAGE.JOINREQUEST) return true; 
		if(type == TYPE_MESSAGE.PARTREQUEST) return true; 
		if(type == TYPE_MESSAGE.REJECT) return true; 
		if(type == TYPE_MESSAGE.WELCOME) return true; 
		if(type == TYPE_MESSAGE.SEQUENCE) return true; 
		return false;
	}

}
