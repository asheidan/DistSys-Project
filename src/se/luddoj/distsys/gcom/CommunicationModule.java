package se.luddoj.distsys.gcom;

public interface CommunicationModule {
	public void receive(Message m);
	
	public void send(String groupName, Message m);
}
