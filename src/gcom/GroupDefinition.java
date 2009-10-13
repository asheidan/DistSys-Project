package gcom;

import gcom.interfaces.GCom.TYPE_COMMUNICATION;
import gcom.interfaces.GCom.TYPE_GROUP;
import gcom.interfaces.GCom.TYPE_MESSAGEORDERING;

public class GroupDefinition implements gcom.interfaces.GroupDefinition {

	private static final long serialVersionUID = -5166402897021175632L;
	private String groupName;
	private TYPE_COMMUNICATION communicationType;
	private TYPE_GROUP groupType;
	private TYPE_MESSAGEORDERING messageOrderingType;

	public GroupDefinition(String group_name) {
		this.groupName = group_name;
		this.communicationType = TYPE_COMMUNICATION.BASIC_UNRELIABLE_MULTICAST;
		this.groupType = TYPE_GROUP.DYNAMIC;
		this.messageOrderingType = TYPE_MESSAGEORDERING.NONORDERED;
	}

	public GroupDefinition(String group_name,
			TYPE_COMMUNICATION communication_type, TYPE_GROUP group_type,
			TYPE_MESSAGEORDERING message_ordering_type) {
		this.groupName = group_name;
		this.communicationType = communication_type;
		this.groupType = group_type;
		this.messageOrderingType = message_ordering_type;
	}

	@Override
	public String getGroupName() {
		return groupName;
	}

	@Override
	public TYPE_COMMUNICATION getCommunicationType() {
		return communicationType;
	}

	@Override
	public TYPE_GROUP getGroupType() {
		return groupType;
	}

	@Override
	public TYPE_MESSAGEORDERING getMessageOrderingType() {
		return messageOrderingType;
	}

	public String toString() {
		return String.format("Group{%s,%s,%s,%s}", groupName, messageOrderingType, groupType, communicationType);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final GroupDefinition other = (GroupDefinition) obj;
		if ((this.groupName == null) ? (other.groupName != null) : !this.groupName.equals(other.groupName)) {
			return false;
		}
		if (this.communicationType != other.communicationType) {
			return false;
		}
		if (this.groupType != other.groupType) {
			return false;
		}
		if (this.messageOrderingType != other.messageOrderingType) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 37 * hash + (this.groupName != null ? this.groupName.hashCode() : 0);
		hash = 37 * hash + this.communicationType.hashCode();
		hash = 37 * hash + this.groupType.hashCode();
		hash = 37 * hash + this.messageOrderingType.hashCode();
		return hash;
	}

}