package se.luddoj.distsys.gcom.interfaces;

import java.util.List;

public interface GroupInterface {
	public void addMember(MemberInterface member);
	public void removeMember(MemberInterface member);
	public List<MemberInterface> listMembers();
}
