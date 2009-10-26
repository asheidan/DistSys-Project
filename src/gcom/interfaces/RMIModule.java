package gcom.interfaces;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface RMIModule {
	public void bind(String name, RemoteObject ro)
		throws AccessException, RemoteException, AlreadyBoundException;
	
	public void rebind(String name, RemoteObject ro) throws AccessException, RemoteException;
	
	public void unbind(String name) throws AccessException, RemoteException;

	public String[] list() throws AccessException, RemoteException;

	public RemoteObject getReference(String name)
			throws AccessException, RemoteException, NotBoundException;
}
