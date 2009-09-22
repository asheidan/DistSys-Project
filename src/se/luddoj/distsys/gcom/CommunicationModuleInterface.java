package se.luddoj.distsys.gcom;

public interface CommunicationModuleInterface {
	public void receive(MessageInterface m);
	
	public void send(MessageInterface m);
}
