package se.luddoj.distsys.gcom;

import java.io.Serializable;
import java.util.Vector;
import se.luddoj.distsys.gcom.GCom.MessageListener;

public class momNonOrdered implements MessageOrderingModuleInterface {

	
	private Vector<MessageListener> listeners;
	
	public momNonOrdered() {
		listeners = new Vector<MessageListener>();
	}
	
	@Override
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	private void sendToListeners(Serializable message) {
		for(MessageListener l : listeners) {
			l.messageReceived(message);
		}
	}
	
	@Override
	public void queueMessage(MessageInterface m) {
		sendToListeners(m);
	}

}
