package se.luddoj.distsys.gcom;

import java.util.List;
import java.util.Vector;

import se.luddoj.distsys.gcom.GCom.Member;

public class Group implements GroupInterface {

	private Vector<Member> members = new Vector<Member>();
	
	@Override
	public void addMember(Member member) {
		members.add(member);
	}

	@Override
	public List<Member> listMembers() {
		return members;
	}

	@Override
	public void removeMember(Member member) {
		int index = members.indexOf(member);
		members.remove(index);
	}

}
