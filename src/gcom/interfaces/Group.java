package gcom.interfaces;

import java.util.List;

public interface Group {
	// TODO I think this might need public bool leader
	public void addMember(Member member);
	public void removeMember(Member member);
	public List<Member> listMembers();
	public GroupDefinition getDefinition();
}
