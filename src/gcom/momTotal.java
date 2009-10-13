package gcom;

import gcom.HashVectorClock;

import gcom.interfaces.*;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.RemoteObject;

import java.io.Serializable;

import java.util.Hashtable;
import java.util.Vector;

public class momTotal implements MessageOrderingModule {
	private Vector<GComMessageListener> listeners;
	private Vector<Message> messages;
	private Integer lastDelivered;
	private HashVectorClock clock;
	private RemoteObject sequencer;
	private Member identity;

	public momTotal(String id) {
		listeners = new Vector<GComMessageListener>();
		messages = new Vector<Message>();
		this.clock = new HashVectorClock(id);
	}
	
	public void setIdentity(Member me) {
		this.identity = me;
	}

	@Override
	public HashVectorClock getClock() {
		return this.clock;	
	}
	
	public void setSequencer(RemoteObject sequencer) {
		this.sequencer = sequencer;
	}

	@Override
	public void tick() {
		// Do nothing
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
		Debug.log(this, Debug.DEBUG, String.format("Got message from %s with %s", m.getSource(), m.getClock()));
		Integer value = m.getClock().getValue("serialNo");
		if(value == null) {
			try {
				// FIXME: This is where the problem is. The message is never altered with correct source so the message is returned to the original sender instead of the one requesting serialNo.
				sequencer.send(new gcom.Message(null, null, identity, m, Message.TYPE_MESSAGE.SEQUENCE)); // Request serialNo for message
			}
			catch(Exception e) {
				Debug.log(this, Debug.ERROR, "sequncer exception", e);
			}
			return; // Will be resent to us when stamped, so we discard
		}
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
