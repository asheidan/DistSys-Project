package se.luddoj.distsys.gcom.interfaces;

import java.util.List;

public interface GroupManagementModule {
	public void addGroup(GroupDefinitionInterface group);

	public void removeGroup(String groupName);

	public List<GroupDefinitionInterface> listGroups();
}
