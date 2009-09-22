package se.luddoj.distsys.gcom;

public interface CommunicationModule {
	public void receive(MessageInterface m);
	
	public void send(MessageInterface m);
}
