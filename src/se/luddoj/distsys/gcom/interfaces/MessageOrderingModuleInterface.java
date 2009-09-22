package se.luddoj.distsys.gcom.interfaces;

public interface MessageOrderingModuleInterface {
	public void queueMessage(MessageInterface m);

	public void addMessageListener(MessageListener listener);
}
