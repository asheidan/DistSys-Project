package se.luddoj.distsys.gcom;

import se.luddoj.distsys.gcom.interfaces.*;

import java.util.List;
import java.util.Vector;

public class Group implements GroupInterface {

	private Vector<MemberInterface> members = new Vector<MemberInterface>();
	private GroupDefinitionInterface groupDefinition;
	
	public Group(GroupDefinitionInterface group_definition) {
		this.groupDefinition = group_definition;
	}
	
	@Override
	public void addMember(MemberInterface member) {
		members.add(member);
	}

	@Override
	public List<MemberInterface> listMembers() {
		return members;
	}

	@Override
	public void removeMember(MemberInterface member) {
		int index = members.indexOf(member);
		members.remove(index);
	}

}
