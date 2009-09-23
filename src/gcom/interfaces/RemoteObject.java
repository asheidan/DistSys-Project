package gcom.interfaces;

/**
 * An interface describing the objects that are
 * passed around for communication
 */
public interface RemoteObject {
	public void send(Message m);
}
