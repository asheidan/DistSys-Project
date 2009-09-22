package se.luddoj.distsys.gcom;

import se.luddoj.distsys.gcom.GComInterface.MemberInterface;

public class Member implements se.luddoj.distsys.gcom.GComInterface.MemberInterface {

	private static final long serialVersionUID = 6940203241759957894L;
	private String id;
	private String name;
	
	public Member(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public boolean equals(Object m) {
		System.out.println("tja");
		if(MemberInterface.class.isInstance(m)) {
			return ((MemberInterface) m).getID().equals(this.id);
		}
		return false;
	}
	

}
