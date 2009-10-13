package gcom;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import gcom.interfaces.RemoteObject;

public class RMIModule implements gcom.interfaces.RMIModule {
	private Registry registry;
	
	public RMIModule(String serverAddress, int serverPort) throws RemoteException {
		registry = LocateRegistry.getRegistry(serverAddress, serverPort);
		registry.list();
	}
	
	@Override
	public void bind(String name, RemoteObject ro) throws AccessException, RemoteException, AlreadyBoundException {
		registry.bind(name, UnicastRemoteObject.exportObject(ro,0));
	}

	@Override
	public void unbind(String name) throws AccessException, RemoteException {
		try {
			registry.unbind(name);
		}
		catch(NotBoundException e){
			Debug.log(this, Debug.DEBUG, "Trying to unbind something that isn't there" + e.getMessage());
		}
	}

	@Override
	public String[] list() throws AccessException, RemoteException {
		return registry.list();
	}

	@Override
	public RemoteObject getReference(String name) throws AccessException, RemoteException, NotBoundException {
		return (RemoteObject)registry.lookup(name);
	}

}
