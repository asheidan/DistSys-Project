package gcom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import gcom.interfaces.CommunicationModule;
import gcom.interfaces.GroupDefinition;
import gcom.interfaces.Message;

public class RemoteObject implements gcom.interfaces.RemoteObject,Runnable {
	//private Logger logger = Logger.getLogger("gcom.RemoteObject");
	private static final long serialVersionUID = 1740402897021175632L;
	private final double unique = Math.random();
	private GroupDefinition definition;
	private transient CommunicationModule com;
	private BlockingQueue<Message> localQueue = new LinkedBlockingQueue<Message>();
	private transient boolean run = false;
	private transient Thread t;

	public RemoteObject(CommunicationModule com, GroupDefinition definition) {
		//logger.setLevel(Level.ERROR);
		//BasicConfigurator.configure();
		this.definition = definition;
		this.com = com;
		t = new Thread(this);
		this.start();
	}
	
	public void start() {
		Debug.log("gcom.RemoteObject",Debug.DEBUG,"Starting thread in " + this);
		run = true;
		t.start();
	}
	
	@Override
	public void send(Message m) {
		Debug.log("gcom.RemoteObject",Debug.DEBUG,"Queueing message in " + this);
		localQueue.add(m);
	}

	@Override
	public void run() {
		while(run){
			try {
				Message m = localQueue.poll(500,TimeUnit.MILLISECONDS);
				if(m != null) {
					com.receive(m);
					Debug.log("gcom.RemoteObject",Debug.DEBUG,"Received a Message in " + this);
				}
				else {
					Debug.log("gcom.RemoteObject",Debug.TRACE,"Queue poll timed out");
				}
			}
			catch(InterruptedException e) {
				System.err.println("BlockingPoll interupted: " + e.getMessage());
			}
		}
	}
	
	public void stop() {
		Debug.log("gcom.RemoteObject",Debug.DEBUG,"Someone told thread to stop");
		run = false;
	}

	@Override
	public GroupDefinition getDefinition() {
		return definition;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: This doesn't work since we get a proxyobject from registry and not a proper remote
		if (obj == null) {
			Debug.log(this, Debug.DEBUG, "Compared to null");
			return false;
		}
		/*
		if (getClass() != obj.getClass()) {
			Debug.log(this, Debug.DEBUG, "Compared to other class: " + obj.getClass().getName());
			return false;
		}
		*/
		gcom.interfaces.RemoteObject other = (gcom.interfaces.RemoteObject) obj;
		try {
			if (this.unique != other.getUnique()) {
				Debug.log(this, Debug.DEBUG, "Compared to other unique");
				return false;
			}
		}
		catch(Exception e) {
			return false;
		}
		/*
		if (this.definition != other.definition && (this.definition == null || !this.definition.equals(other.definition))) {
			return false;
		}
		*/
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.unique) ^ (Double.doubleToLongBits(this.unique) >>> 32));
		hash = 19 * hash + (this.definition != null ? this.definition.hashCode() : 0);
		return hash;
	}

	@Override
	public double getUnique() {
		return unique;
	}
}

