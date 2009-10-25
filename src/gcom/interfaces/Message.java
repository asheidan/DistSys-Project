package gcom.interfaces;

import gcom.HashVectorClock;

import java.io.Serializable;


public interface Message extends Serializable {
	public static enum TYPE_MESSAGE {
		APPLICATION, JOINREQUEST, PARTREQUEST,
		REJECT, WELCOME, GOTMEMBER, LOSTMEMBER,
		ELECTION, CLOSE
	};

	public Serializable getMessage();

	public HashVectorClock getClock();

	public String getGroupName();

	public Member getSource();
	
	public Member getReturnMember();
	
	public void setReturnMember(Member returnMember);
	
	public TYPE_MESSAGE getMessageType();

	public boolean bypass();
}
