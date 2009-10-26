package gcom.interfaces;

public interface CommunicationModule {

	public void receive(Message m);
	
	public void send(Message m);
	
	public void send(Member member, Message msg);

	public void addGComViewChangeListener(GComViewChangeListener listener);

	public void attachDebugger(DebugInterface debug);

}
