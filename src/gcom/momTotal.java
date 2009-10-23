package gcom;

import gcom.HashVectorClock;

import gcom.interfaces.*;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.RemoteObject;

import java.io.Serializable;

import java.util.Hashtable;
import java.util.Vector;

public class momTotal extends momNonOrdered {
	private Vector<Message> messages;
	private Integer lastDelivered;
	private RemoteObject sequencer;
	private Member identity;

	public momTotal(String id) {
		super(id);
		messages = new Vector<Message>();
	}
	
	public void setIdentity(Member me) {
		Debug.log(this, Debug.DEBUG, "Setting identity: " + me);
		this.identity = me;
	}

	public void setSequencer(RemoteObject sequencer) {
		this.sequencer = sequencer;
	}

	private void sendMessage(Message message) {
		Integer value = message.getClock().getValue("serialNo");
		this.lastDelivered++;
		sendToListeners(message);	
	}

	@Override
	public void queueMessage(Message m) {
		if(checkBypass(m)) { 
			sendToListeners(m);
			return;
		}
		Debug.log(this, Debug.DEBUG, String.format("Got message from %s with %s", m.getSource(), m.getClock()));
		Integer value = m.getClock().getValue("serialNo");
		if(value == null) {
			try {
				m.setReturnMember(identity);
				sequencer.send(m); // Request serialNo for message
			}
			catch(Exception e) {
				Debug.log(this, Debug.ERROR, "sequncer exception", e);
			}
			return; // Will be resent to us when stamped, so we discard
		}
		if(this.lastDelivered == null) {
			this.lastDelivered = value-1;
			sendMessage(m);
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
				sendMessage(m);
				remove.add(m);
			}
		}

		for(Message m : remove) {
			messages.remove(m);
		}
		if(remove.size() > 0) { checkMessages(); }
	}

}
