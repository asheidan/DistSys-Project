package se.luddoj.distsys.gcom;

import java.io.Serializable;

public interface Message extends Serializable {
	public static enum TYPE_MESSAGE { APPLICATION };

	// public Message(VectorClock j, String groupName, Serializable data, TYPE_MESSAGE messageType);

	public Serializable getMessage();

	public VectorClock getClock();

	public String getGroupName();
	
	public TYPE_MESSAGE getMessageType();
}
