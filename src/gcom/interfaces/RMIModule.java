package gcom.interfaces;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public interface RMIModule {
	public void bind(String name, RemoteObject ro)
		throws AccessException, RemoteException, AlreadyBoundException;
	
	public void unbind(String name) throws AccessException, RemoteException;
}
