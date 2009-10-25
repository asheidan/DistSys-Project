package gcom.interfaces;

import java.util.List;

public interface Group {
	public void addMember(Member member);
	public void removeMember(Member member);
	public boolean isMember(Member member);
	public List<Member> listMembers();
	
	public void close();
	public boolean isOpen();
	
	public GroupDefinition getDefinition();
	
	public void setLeader(Member leader);
	public Member getLeader();

	public void lost();
	public void addViewChangeListener(ViewChangeListener listener);
}
