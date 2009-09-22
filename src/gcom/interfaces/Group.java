package gcom.interfaces;

import java.util.List;

public interface Group {
	public void addMember(Member member);
	public void removeMember(Member member);
	public List<Member> listMembers();
}
