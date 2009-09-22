package se.luddoj.distsys.gcom.interfaces;

import java.io.Serializable;

/**
 * A GComInterface message listener interface that deals with messages delivered to a
 * group
 */
public interface MessageListener {
	/**
	 * Callback method when a message is delivered to the group.
	 * 
	 * @param source
	 *                the source Member who sent the message.
	 * @param message
	 *                the message object.
	 */
	public void messageReceived(Serializable message);
}

