package gcom;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import gcom.interfaces.RemoteObject;
import gcom.interfaces.Backdoor;

public class RMIModule implements gcom.interfaces.RMIModule {
	private Registry registry;
	private Backdoor backdoor;
	private String serverAddress;
	private int serverPort;
	
	public RMIModule(String serverAddress, int serverPort) throws RemoteException {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		registry = LocateRegistry.getRegistry(serverAddress, serverPort);
		connect();
		//registry.list();
	}
	
	private void connect() throws RemoteException {
		if(backdoor == null) {
			try {
				backdoor = (Backdoor)registry.lookup(Backdoor.NAME);
			}
			catch(NotBoundException e) {
				Debug.log(this,Debug.ERROR, "No backdoor present in registry");
			}
		}
		else {
			try {
				backdoor.ping();
			}
			catch(ConnectException e) {
				Debug.log(this, Debug.WARN, "Backdoor is dead trying to replace it");
				backdoor = null;
				connect();
			}
		}
	}
	
	@Override
	public void bind(String name, RemoteObject ro) throws AccessException, RemoteException, AlreadyBoundException {
		connect();
		try {
			backdoor.bind(name, UnicastRemoteObject.exportObject(ro,0));
		}
		catch(ExportException e) {
			Debug.log(this, Debug.DEBUG, "Object already exported");
			backdoor.bind(name,ro);
		}
	}
	
	@Override
	public void rebind(String name, RemoteObject ro) throws AccessException, RemoteException {
		connect();
		try {
			backdoor.rebind(name, UnicastRemoteObject.exportObject(ro,0));
		}
		catch(ExportException e) {
			Debug.log(this, Debug.DEBUG, "Object already exported");
			backdoor.rebind(name,ro);
		}
	}

	@Override
	public void unbind(String name) throws AccessException, RemoteException {
		connect();
		try {
			backdoor.unbind(name);
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
