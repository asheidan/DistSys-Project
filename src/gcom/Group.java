package gcom;

import gcom.interfaces.Member;
import gcom.interfaces.GroupDefinition;

import gcom.interfaces.ViewChangeListener;
import java.util.List;
import java.util.Vector;

public class Group implements gcom.interfaces.Group {

	private Vector<ViewChangeListener> viewChangeListeners = new Vector<ViewChangeListener>();
	private Vector<Member> members = new Vector<Member>();
	private GroupDefinition groupDefinition;
	private Member leader;

	public Group(GroupDefinition group_definition) {
		this.groupDefinition = group_definition;
	}

	@Override
	public synchronized void addMember(Member member) {
		if(!isMember(member)) {
			Debug.log("gcom.Group", Debug.DEBUG, groupDefinition.getGroupName() + ": adding member: " + member);
			members.add(member);
			for(ViewChangeListener l : viewChangeListeners) {
				l.gotMember(member);
			}
		} else {
			Debug.log("gcom.Group", Debug.DEBUG, "Addmember: Member " + member + " is already part of group.");
		}
	}

	@Override
	public synchronized List<Member> listMembers() {
		return members;
	}

	@Override
	public boolean isMember(Member member) {
		return members.contains(member);
	}

	@Override
	public synchronized void removeMember(Member member) {
		int index = members.indexOf(member);
		members.remove(index);
		for(ViewChangeListener l : viewChangeListeners) {
			l.lostMember(member);
		}
	}

	@Override
	public GroupDefinition getDefinition() {
		return groupDefinition;
	}


	@Override
	public void setLeader(Member leader) {
		this.leader = leader;
	}
	
	@Override
	public Member getLeader() {
		return leader;
	}

	@Override
	public void addViewChangeListener(ViewChangeListener listener) {
		Debug.log("gcom.Group", Debug.DEBUG, groupDefinition.getGroupName() + ": adding listener: " + listener);
		for(Member m : members) {
			listener.gotMember(m);
		}
		viewChangeListeners.add(listener);
	}

}
