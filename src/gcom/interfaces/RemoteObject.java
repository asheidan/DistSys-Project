package gcom.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An interface describing the objects that are
 * passed around for communication
 */
public interface RemoteObject extends Remote,Serializable{
	public void send(Message m) throws RemoteException;
	public GroupDefinition getDefinition() throws RemoteException;
	public double getUnique() throws RemoteException;
}
