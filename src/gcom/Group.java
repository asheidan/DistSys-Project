package gcom;

import gcom.interfaces.Member;

import java.util.List;
import java.util.Vector;

public class Group implements gcom.interfaces.Group {

	private Vector<Member> members = new Vector<Member>();
	private GroupDefinition groupDefinition;

	public Group(GroupDefinition group_definition) {
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

	public GroupDefinition getGroupDefinition() {
		return groupDefinition;
	}

}
