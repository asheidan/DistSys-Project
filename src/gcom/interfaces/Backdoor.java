package gcom.interfaces;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Backdoor extends Remote {
	public static final String NAME = "backdoor";
	public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException;
	public void rebind(String name, Remote obj) throws RemoteException, AccessException;
	public void unbind(String name) throws RemoteException, NotBoundException, AccessException;
}