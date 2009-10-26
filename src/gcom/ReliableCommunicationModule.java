package gcom;

import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;

import java.util.Hashtable;
import java.util.PriorityQueue;

class ReliableCommunicationModule extends BasicCommunicationModule {
	
	// TODO: Broken since messagehashing is broken (or something else)
	
	private static final int QUEUE_LENGTH = 100;
	
	private Hashtable<Integer, Message> receivedMessages = new Hashtable<Integer, Message>(QUEUE_LENGTH);

	// Perhaps we can use a simple fifo instead?
	private PriorityQueue<Message> lastMessages = new PriorityQueue<Message>(QUEUE_LENGTH);
	
	public ReliableCommunicationModule(MessageOrderingModule mom, GroupManagementModule gmm, String groupName, String processID) {
		super(mom,gmm,groupName,processID);
	}
	
	@Override
	public void receive(Message m) {
		Debug.log(this, Debug.DEBUG, "Got Message: " + m.hashCode());
		if(!receivedMessages.containsKey(m.hashCode())) {
			// New message!!!
			rememberMessage(m);
			
			if( !m.bypass() ) super.send(m);
			
			super.receive(m);
		}
		else {
			Debug.log(this, Debug.DEBUG, "Got old Message: " + m.getClock().toString());
		}
	}
	
	@Override
	public void send(Message m) {
		Debug.log(this, Debug.DEBUG, "Sent Message: " + m.hashCode());
		rememberMessage(m);
		super.send(m);
		super.receive(m);
	}
	
	private void rememberMessage(Message m) {
		// Clean out history
		if(lastMessages.size() >= QUEUE_LENGTH) {
			Debug.log(this, Debug.DEBUG, "Shortening queue.");
			Message oldest = lastMessages.poll();
			receivedMessages.remove(Integer.valueOf(oldest.hashCode()));
		}
		lastMessages.add(m);
		receivedMessages.put(Integer.valueOf(m.hashCode()),m);
		
	}
	
}
