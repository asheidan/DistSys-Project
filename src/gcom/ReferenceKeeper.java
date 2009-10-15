package gcom;

import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;

import gcom.interfaces.RemoteObject;
import gcom.interfaces.RMIModule;

public class ReferenceKeeper implements Runnable {
	private RMIModule rmi;
	private String name;
	private RemoteObject reference;
	private boolean run = false;
	private Thread t;

	public ReferenceKeeper(RMIModule rmi, String name, RemoteObject reference) {
		this.rmi = rmi;
		this.name = name;
		this.reference = reference;
		
		t = new Thread(this);
		this.start();
	}
	
	public void start() {
		Debug.log(this, Debug.DEBUG, "ReferenceKeeper for " + name + " started");
		run = true;
		t.start();
	}
	
	@Override
	public synchronized void run() {
		// TODO: We should see if mainthread is alive before rebinding reference
		// TODO: This is rediculous
		while(run) {
			try {
				RemoteObject r = rmi.getReference(name);
				try {
					if( reference.getUnique() != r.getUnique() ) {
						Debug.log(this, Debug.DEBUG, "Another reference already bound");
						rmi.rebind(name, reference);
					}
					else {
						Debug.log(this, Debug.DEBUG, "Our reference is in registry");
					}
				}
				catch(ConnectException e) {
					Debug.log(this, Debug.DEBUG, "Another reference is dead, rebinding");
					try {
						rmi.rebind(name, reference);
					}
					catch(Exception ex) {
						Debug.log(this, Debug.DEBUG, "Got exception when trying to rebind", ex);
					}
				}
			}
			catch(NotBoundException e) {
				Debug.log(this, Debug.DEBUG, "Nothing bound, binding our reference");
				try {
					rmi.bind(name,reference);
				}
				catch(Exception ex) {
					Debug.log(this, Debug.DEBUG, "Got exception eventhough registry is online", ex);
				}
			}
			catch(ConnectException e) {
				Debug.log(this, Debug.DEBUG, "Registry not online, waiting for it to come online.",e);
			}
			catch(Exception e) {
				Debug.log(this, Debug.DEBUG, "Caught Exception", e);
			}


			try {
				this.wait(60 * 1000);
			}
			catch (InterruptedException ex) {
				Debug.log(this,Debug.WARN, "Wait was interrupted, stopping thread.");
				this.stop();
			}
			catch(IllegalMonitorStateException ex) {
				Debug.log(this, Debug.ERROR, "We got illegalstate", ex);
				this.stop(); // ??
			}
		}
	}
	
	public void stop() {
		Debug.log(this, Debug.DEBUG, "ReferenceKeeper for " + name + " stopped");
		run = false;
	}
}