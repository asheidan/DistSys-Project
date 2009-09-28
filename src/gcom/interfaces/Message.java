package gcom.interfaces;

import gcom.HashVectorClock;

import java.io.Serializable;

public interface Message extends Serializable {
	public static enum TYPE_MESSAGE { APPLICATION, JOIN };

	public Serializable getMessage();

	public HashVectorClock getClock();

	public String getGroupName();

	public Member getSource();
	
	public TYPE_MESSAGE getMessageType();
}
