package gcom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import gcom.interfaces.CommunicationModule;
import gcom.interfaces.Message;

public class RemoteObject implements gcom.interfaces.RemoteObject,Runnable {
	private Logger logger = Logger.getLogger("gcom.RemoteObject");
	private transient CommunicationModule com;
	private transient BlockingQueue<Message> localQueue = new LinkedBlockingQueue<Message>();
	private transient boolean run = false;
	private transient Thread t;

	public RemoteObject(CommunicationModule com) {
		logger.setLevel(Level.ERROR);
		BasicConfigurator.configure();
		this.com = com;
		t = new Thread(this);
	}
	
	public void start() {
		logger.debug("Starting thread");
		run = true;
		t.start();
	}
	
	@Override
	public void send(Message m) {
		logger.debug("Queueing message");
		localQueue.add(m);
	}

	@Override
	public void run() {
		while(run){
			try {
				Message m = localQueue.poll(500,TimeUnit.MILLISECONDS);
				if(m != null) {
					com.receive(m);
					logger.debug("Received a Message");
				}
				else {
					logger.debug("Queue poll timed out");
				}
			}
			catch(InterruptedException e) {
				System.err.println("BlockingPoll interupted: " + e.getMessage());
			}
		}
	}
	
	public void stop() {
		logger.debug("Someone told thread to stop");
		run = false;
	}

}

