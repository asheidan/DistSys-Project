package gcom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import gcom.interfaces.CommunicationModule;
import gcom.interfaces.Message;

public class RemoteObject implements gcom.interfaces.RemoteObject,Runnable {
	private CommunicationModule com;
	private transient BlockingQueue<Message> localQueue = new LinkedBlockingQueue<Message>();

	public RemoteObject(CommunicationModule com) {
		this.com = com;
		new Thread(this);
	}
	
	@Override
	public void send(Message m) {
		localQueue.add(m);
	}

	@Override
	public void run() {
		for(;;){
			try {
				com.receive(localQueue.take());
			}
			catch(InterruptedException e) {
				System.err.println("BlockingTake interupted: " + e.getMessage());
			}
		}
	}

}
