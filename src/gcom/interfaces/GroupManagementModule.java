package gcom.interfaces;

import java.util.List;

public interface GroupManagementModule {
	public void addGroup(GroupDefinition group);

	public void removeGroup(String groupName);

	public List<GroupDefinition> listGroups();
}
