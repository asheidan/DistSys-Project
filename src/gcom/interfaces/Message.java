package gcom.interfaces;

import gcom.HashVectorClock;

import java.io.Serializable;

public interface Message extends Serializable {
	public static enum TYPE_MESSAGE { APPLICATION, JOINREQUEST, PARTREQUEST, REJECT, WELCOME, GOTMEMBER, LOSTMEMBER};

	public Serializable getMessage();

	public HashVectorClock getClock();

	public String getGroupName();

	public Member getSource();
	
	public Member getReciever();

	public void setReciever(Member source);
	
	public TYPE_MESSAGE getMessageType();
}
