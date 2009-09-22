package gcom.interfaces;

public interface CommunicationModule {
	public void receive(Message m);
	
	public void send(Message m);
}
