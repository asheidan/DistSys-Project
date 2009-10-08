package gcom.interfaces;

public interface MessageOrderingModule {
	public void queueMessage(Message m);

	public void addMessageListener(GComMessageListener listener);
}
