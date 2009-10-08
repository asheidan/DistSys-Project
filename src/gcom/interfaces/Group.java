package gcom.interfaces;

import java.util.List;

public interface Group {
	public void addMember(Member member);
	public void removeMember(Member member);
	public List<Member> listMembers();
	public GroupDefinition getDefinition();
	public void setLeader(boolean leader);
	public boolean isLeader();
	public void addViewChangeListener(ViewChangeListener listener);
}
