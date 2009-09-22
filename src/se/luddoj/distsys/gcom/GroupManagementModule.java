package se.luddoj.distsys.gcom;

import java.util.List;

import se.luddoj.distsys.gcom.GComInterface.GroupDefinitionInterface;

public interface GroupManagementModule {
	public void addGroup(GroupDefinitionInterface group);

	public void removeGroup(String groupName);

	public List<GroupDefinitionInterface> listGroups();
}
