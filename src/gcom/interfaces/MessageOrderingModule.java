package gcom.interfaces;

import gcom.HashVectorClock;

public interface MessageOrderingModule {

	public void queueMessage(Message m);
	public void addMessageListener(MessageListener listener);
	public HashVectorClock getClock();
	public void tick();

}
