package se.luddoj.distsys.gcom;

import se.luddoj.distsys.gcom.interfaces.*;

import java.util.List;
import java.util.Vector;

public class Group implements GroupInterface {

	private Vector<Member> members = new Vector<Member>();
	private GroupDefinitionInterface groupDefinition;
	
	public Group(GroupDefinitionInterface group_definition) {
		this.groupDefinition = group_definition;
	}
	
	@Override
	public void addMember(Member member) {
		members.add(member);
	}

	@Override
	public List<Member> listMembers() {
		return members;
	}

	@Override
	public void removeMember(Member member) {
		int index = members.indexOf(member);
		members.remove(index);
	}

}
