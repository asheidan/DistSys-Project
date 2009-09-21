package se.luddoj.distsys.gcom;

import java.util.List;

import se.luddoj.distsys.gcom.GCom.Member;

public interface GroupInterface {
	public void addMember(Member member);
	public void removeMember(Member member);
	public List<Member> listMembers();
}
