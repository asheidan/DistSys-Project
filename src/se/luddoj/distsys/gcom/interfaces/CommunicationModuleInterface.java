package se.luddoj.distsys.gcom.interfaces;

public interface CommunicationModuleInterface {
	public void receive(MessageInterface m);
	
	public void send(MessageInterface m);
}
