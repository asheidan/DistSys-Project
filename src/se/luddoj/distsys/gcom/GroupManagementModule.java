package se.luddoj.distsys.gcom;

import java.util.List;

import se.luddoj.distsys.gcom.GCom.GroupDefinition;

public interface GroupManagementModule {
	public void addGroup(GroupDefinition group);

	public void removeGroup(String groupName);

	public List<GroupDefinition> listGroups();
}
