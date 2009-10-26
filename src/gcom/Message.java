package gcom;

import java.io.Serializable;
import gcom.interfaces.Member;

public class Message implements gcom.interfaces.Message,Comparable<Message> {

	private static final long serialVersionUID = 5507203760294899480L;
	private HashVectorClock clock;
	private String groupName;
	private Member source;
	private Serializable data;
	private TYPE_MESSAGE type;
	private Member returnMember;
	
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
	
	@Override
	public Member getSource() {
		return source;
	}
	
	@Override
	public Member getReturnMember() {
		return returnMember;
	}
	
	@Override
	public void setReturnMember(Member returnMember) {
		this.returnMember = returnMember;
	}

	@Override
	public boolean bypass() {
		if(type == TYPE_MESSAGE.JOINREQUEST) return true; 
		if(type == TYPE_MESSAGE.PARTREQUEST) return true; 
		if(type == TYPE_MESSAGE.REJECT) return true; 
		if(type == TYPE_MESSAGE.WELCOME) return true; 
		return false;
	}
	@Override
	public String toString() {
		return String.format("Message %s:%s:%s",this.type,this.clock,this.data);
	}

	@Override
	public int hashCode() {
		return this.clock.hashCode();
	}

	@Override
	public int compareTo(Message m) {
		return this.clock.compareTo(m.clock);
	}
}
