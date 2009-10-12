package gcom.interfaces;

import java.util.List;

public interface GroupManagementModule {

	public void addGroup(GroupDefinition group);
	public void removeGroup(String groupName);
	
	public List<GroupDefinition> listGroups();

	public boolean isLeader(String groupName);

	public GroupDefinition getGroupDefinition(String groupName);

	public void addMember(String groupName, Member member);
	public void removeMember(String groupName, Member member);

	public boolean memberIsInGroup(String groupName, Member member);

	public List<Member> listGroupMembers(String groupName);

	public void addViewChangeListener(String groupName, ViewChangeListener listener);
}
