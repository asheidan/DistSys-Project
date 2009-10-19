package gcom;

import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;

import java.util.Hashtable;
import java.util.PriorityQueue;

class ReliableCommunicationModule extends BasicCommunicationModule {
	
	// TODO: Broken since messagehashing is broken (or something else)
	
	private static final int QUEUE_LENGTH = 100;
	
	private Hashtable<Message, Boolean> receivedMessages = new Hashtable<Message, Boolean>(QUEUE_LENGTH);

	// Perhaps we can use with a simple fifo instead?
	private PriorityQueue<Message> lastMessages = new PriorityQueue<Message>(QUEUE_LENGTH);
	
	public ReliableCommunicationModule(MessageOrderingModule mom, GroupManagementModule gmm, String groupName, String processID) {
		super(mom,gmm,groupName,processID);
	}
	
	@Override
	public void receive(Message m) {
		
		// TODO: local delivery
		
		if(!receivedMessages.containsKey(m)) {
			// New message!!!
			Debug.log(this, Debug.DEBUG, "Got new Message: " + m.getClock().toString());
			// Clean out history
			if(lastMessages.size() == QUEUE_LENGTH) {
				Debug.log(this, Debug.DEBUG, "Shortening queue.");
				Message oldest = lastMessages.poll();
				receivedMessages.remove(oldest);
			}
			
			super.send(m);
			
			lastMessages.add(m);
			receivedMessages.put(m,true);
			
			super.receive(m);
		}
		else {
			Debug.log(this, Debug.DEBUG, "Got old Message: " + m.getClock().toString());
		}
	}
	
}