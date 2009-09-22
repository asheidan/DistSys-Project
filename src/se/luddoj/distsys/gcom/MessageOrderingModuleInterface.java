package se.luddoj.distsys.gcom;

import se.luddoj.distsys.gcom.GCom.MessageListener;

public interface MessageOrderingModuleInterface {
	public void queueMessage(MessageInterface m);

	public void addMessageListener(MessageListener listener);
}
