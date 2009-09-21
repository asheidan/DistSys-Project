package se.luddoj.distsys.gcom;

import java.io.Serializable;

public class Message implements MessageInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5507203760294899480L;
	private HashVectorClock clock;
	private String groupName;
	private Serializable data;
	private TYPE_MESSAGE type;
	
	public Message(HashVectorClock c, String n, Serializable d, TYPE_MESSAGE t) {
		clock = c;
		groupName = n;
		data = d;
		type = t;
	}
	
	@Override
	public HashVectorClock getClock() {
		return clock;
	}

	@Override
	public String getGroupName() {
		return groupName;
	}

	@Override
	public Serializable getMessage() {
		return data;
	}

	@Override
	public TYPE_MESSAGE getMessageType() {
		return type;
	}

}
