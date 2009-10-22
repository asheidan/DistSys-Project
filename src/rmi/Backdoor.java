package rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

interface Backdoor extends Remote {
	public void proxyBind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException;
	public void proxyRebind(String name, Remote obj) throws RemoteException, AccessException;
	public void proxyUnbind(String name) throws RemoteException, NotBoundException, AccessException;
}