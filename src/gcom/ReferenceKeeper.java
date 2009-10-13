package gcom;

import java.rmi.AlreadyBoundException;

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
	public void run() {
		// TODO: We should see if mainthread is alive before rebinding reference
		while(run) {
			try {
				RemoteObject r = rmi.getReference(name);
				if( r != reference ) {
					Debug.log(this, Debug.DEBUG, "Something else than our reference was in registry");
					rmi.bind(name, reference);
				}
			}
			catch(AlreadyBoundException e) {
				Debug.log(this, Debug.DEBUG, "Another reference already bound rebinding");
				try {
					rmi.rebind(name,reference);
				}
				catch(Exception ex) {
					Debug.log(this, Debug.DEBUG, "Got exception eventhough registry is online", ex);
				}
			}
			catch(Exception e) {
				Debug.log(this, Debug.DEBUG, "Caught Exception", e);
			}

			try {
				t.wait(300 * 1000);
			}
			catch (InterruptedException ex) {
				Debug.log(this,Debug.WARN, "Wait was interrupted, stopping thread.");
				stop();
			}
		}
	}
	
	public void stop() {
		Debug.log(this, Debug.DEBUG, "ReferenceKeeper for " + name + " stopped");
		run = false;
	}
}