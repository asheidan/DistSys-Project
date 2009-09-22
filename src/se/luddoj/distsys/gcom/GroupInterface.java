package se.luddoj.distsys.gcom;

import java.util.List;

import se.luddoj.distsys.gcom.GCom.MemberInterface;

public interface GroupInterface {
	public void addMember(MemberInterface member);
	public void removeMember(MemberInterface member);
	public List<MemberInterface> listMembers();
}
