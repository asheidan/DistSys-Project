package gcom;

import gcom.interfaces.Message;
import gcom.interfaces.*;
import gcom.interfaces.Message.*; 

import java.util.Vector;

public class momNonOrdered implements MessageOrderingModule {
	protected Vector<GComMessageListener> listeners;
	protected Vector<Message> messages = new Vector<Message>();
	protected HashVectorClock clock;
	protected String myID;

	public momNonOrdered(String id) {
		myID = id;
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
			Debug.log(this, Debug.TRACE, "Sent message to: " + l.toString());
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(Message m) {
		Debug.log(this, Debug.TRACE, "Queued message: " + m.toString());
		sendToListeners(m);
	}

	@Override
	public void attachDebug(DebugInterface debug) {
		debug.setQueue(messages);
	}

}
