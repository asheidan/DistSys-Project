package se.luddoj.distsys.gcom;

import se.luddoj.distsys.gcom.GCom.MemberInterface;

public class Member implements se.luddoj.distsys.gcom.GCom.MemberInterface {

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
	
	public boolean equals(MemberInterface m) {
		return m.getID().equals(this.id);
	}
	

}
