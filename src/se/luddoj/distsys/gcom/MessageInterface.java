package se.luddoj.distsys.gcom;

import java.io.Serializable;

public interface MessageInterface extends Serializable {
	public static enum TYPE_MESSAGE { APPLICATION };

	// 

	public Serializable getMessage();

	public HashVectorClock getClock();

	public String getGroupName();
	
	public TYPE_MESSAGE getMessageType();
}
