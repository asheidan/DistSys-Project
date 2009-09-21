package se.luddoj.distsys.gcom;

public interface CommunicationModule {
	public void receive(MessageInterface m);
	
	public void send(String groupName, MessageInterface m);
}
