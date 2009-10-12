package gcom.interfaces;

public interface CommunicationModule {
	// TODO: COM should be expanded with private messages
	public void receive(Message m);
	
	public void send(Message m);

	public void addGComViewChangeListener(GComViewChangeListener listener);
}
