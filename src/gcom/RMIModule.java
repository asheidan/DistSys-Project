package gcom;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import gcom.interfaces.RemoteObject;

public class RMIModule implements gcom.interfaces.RMIModule {
	private Logger logger = Logger.getLogger("gcom.RMIModule");
	private Registry registry;
	
	public RMIModule(String serverAddress, int serverPort) throws RemoteException {
		registry = LocateRegistry.getRegistry(serverAddress, serverPort);	
	}
	
	@Override
	public void bind(String name, RemoteObject ro) throws AccessException, RemoteException, AlreadyBoundException {
		registry.bind(name, UnicastRemoteObject.exportObject(ro));
	}

	@Override
	public void unbind(String name) throws AccessException, RemoteException {
		try {
			registry.unbind(name);
		}
		catch(NotBoundException e){
			logger.debug("Trying to unbind something that isn't there" + e.getMessage());
		}
	}

}
