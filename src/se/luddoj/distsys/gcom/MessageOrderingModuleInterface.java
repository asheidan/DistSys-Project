package se.luddoj.distsys.gcom;

import se.luddoj.distsys.gcom.GComInterface.MessageListener;

public interface MessageOrderingModuleInterface {
	public void queueMessage(MessageInterface m);

	public void addMessageListener(MessageListener listener);
}
