package gcom;

import gcom.interfaces.Group;
import gcom.interfaces.GroupDefinition;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class GroupManagementModule implements gcom.interfaces.GroupManagementModule {
	Hashtable<String, Group> groups;
	
	
	public GroupManagementModule() {
		groups = new Hashtable<String, Group>();
	}
	
	@Override
	public void addGroup(GroupDefinition group) {
		//groups.put(group.getGroupName(),new gcom.Group(group,group.getGroupType()));
		groups.put(group.getGroupName(),new gcom.Group(group));
	}

	@Override
	public List<GroupDefinition> listGroups() {
		// TODO Shouldn't this return List<Group> and should it be sorted?
		Vector<GroupDefinition> result = new Vector<GroupDefinition>(groups.size());
		for(Group g : groups.values()) {
			result.add(g.getDefinition());
		}
		return result;
	}

	@Override
	public void removeGroup(String groupName) {
		// TODO This should only be done if we are the group leader? Or is that check performed somewhere else?
		groups.remove(groupName);
	}

	@Override
	public Group getGroup(String groupName) {
		return groups.get(groupName);
	}

}
