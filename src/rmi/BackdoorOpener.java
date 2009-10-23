package rmi;

import gcom.Debug;

import gcom.interfaces.Backdoor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

class BackdoorOpener implements Backdoor {
	private Registry registry;
	
	public BackdoorOpener(int registryPort) throws RemoteException {
		Debug.log(this, Debug.DEBUG, "Backdoor opened to port: " + registryPort);
		registry = LocateRegistry.getRegistry(registryPort);
	}
	
	@Override
	public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {
		Debug.log(this, Debug.DEBUG, "Proxy binding: " + name);
		registry.bind(name,obj);
	}

	@Override
	public void rebind(String name, Remote obj) throws RemoteException, AccessException {
		Debug.log(this, Debug.DEBUG, "Proxy rebinding: " + name);
		registry.rebind(name,obj);
	}
	
	@Override
	public void unbind(String name) throws RemoteException, NotBoundException, AccessException {
		Debug.log(this, Debug.DEBUG, "Proxy unbinding: " + name);
		registry.unbind(name);
	}
}