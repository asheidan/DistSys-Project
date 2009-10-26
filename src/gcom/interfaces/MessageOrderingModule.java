package gcom.interfaces;

import gcom.HashVectorClock;

public interface MessageOrderingModule {

	public void addMessageListener(GComMessageListener listener);
	public void queueMessage(Message m);
	public HashVectorClock getClock();
	public void tick();

	//public void attachDebugger(DebugInterface debug);

}
