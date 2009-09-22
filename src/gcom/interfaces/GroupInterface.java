package gcom.interfaces;

import java.util.List;

public interface GroupInterface {
	public void addMember(Member member);
	public void removeMember(Member member);
	public List<Member> listMembers();
}
