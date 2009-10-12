package gcom;

import gcom.interfaces.Group;
import gcom.interfaces.GroupDefinition;
import gcom.interfaces.Member;

import gcom.interfaces.ViewChangeListener;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Level;

public class GroupManagementModule implements gcom.interfaces.GroupManagementModule {
	Hashtable<String, Group> groups;
	
	
	public GroupManagementModule() {
		groups = new Hashtable<String, Group>();
	}
	
	@Override
	public boolean isLeader(String groupName) {
		// TODO: nullpointerexception
		return groups.get(groupName).isLeader();
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
	public GroupDefinition getGroupDefinition(String groupName) {
		Group g = groups.get(groupName);
		if(g == null) { return null; }
		return g.getDefinition();
	}

	@Override
	public void addMember(String groupName, Member member) {
		Debug.log("gcom.GroupManagementModule", Level.DEBUG, "Adding member: " + member.toString());
		Group g = groups.get(groupName);
		g.addMember(member);
	}

	@Override
	public void removeMember(String groupName, Member member) {
		Debug.log(this, Level.DEBUG, "Removing member: " + member.toString());
		Group g = groups.get(groupName);
		g.removeMember(member);
	}

	public boolean memberIsInGroup(String groupName, Member member){
		// TODO: nullpointer exception
		Group g = groups.get(groupName);
		return g.isMember(member);
	}

	@Override
	public List<Member> listGroupMembers(String groupName) {
		Group g = groups.get(groupName);
		return g.listMembers();
	}

	@Override
	public void addViewChangeListener(String groupName, ViewChangeListener listener) {
		Group g = groups.get(groupName);
		if(g != null) {
			g.addViewChangeListener(listener);
		}
	}
}
