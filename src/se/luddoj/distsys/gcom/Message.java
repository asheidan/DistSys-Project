package se.luddoj.distsys.gcom;

import java.io.Serializable;

public class Message implements MessageInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5507203760294899480L;
	private HashVectorClock clock;
	private String groupName;
	private Member source;
	private Serializable data;
	private TYPE_MESSAGE type;
	
	public Message(HashVectorClock clock, String group_name, Member source, Serializable message, TYPE_MESSAGE type) {
		this.clock = clock;
		this.groupName = group_name;
		this.source = source;
		this.data = message;
		this.type = type;
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

	public Member getMember() {
		return source;
	}

}
