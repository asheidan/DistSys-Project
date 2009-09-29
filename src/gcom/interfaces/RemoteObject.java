package gcom.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An interface describing the objects that are
 * passed around for communication
 */
public interface RemoteObject extends Remote{
	public void send(Message m) throws RemoteException;
}
