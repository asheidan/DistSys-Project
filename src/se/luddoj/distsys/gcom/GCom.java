package se.luddoj.distsys.gcom;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * GCom is an interface for the GCom middleware to be developed in the
 * assignment parts of the course Distributed Systems (5DV020). The GCom
 * middleware is a middleware that facilitates group communication between
 * interconnected, distributed clients.
 * 
 * @version 1.1 Clarified group name/id/description
 * 
 * @author P-O �stberg
 * @author Lars Larsson
 * @author Daniel Henriksson
 * 
 */
public interface GCom {

	/**
	 * A group type identifier. Consult the assignment specification for details
	 * on the difference between static and dynamic groups.
	 */
	public static enum TYPE_GROUP {
		STATIC, DYNAMIC
	};

	// ---------------------------------------------------------
	/**
	 * A communication mode identifier. Consult the course book and the
	 * assignment specification for details on communication mode algorithms.
	 */
	public static enum TYPE_COMMUNICATION {
		BASIC_UNRELIABLE_MULTICAST, BASIC_RELIABLE_MULTICAST, TREEBASED_RELIABLE_MULTICAST
	};

	// ---------------------------------------------------------
	/**
	 * A message ordering mode identifier. Consult the course book and the
	 * assignment specification for details on message ordering algorithms.
	 */
	public static enum TYPE_MESSAGEORDERING {
		NONORDERED, FIFO, CAUSAL, TOTAL, CAUSALTOTAL
	};

	/**
	 * Sends a message to the group. The actual message can be any serializable
	 * object.
	 * 
	 * @param groupName
	 *                the identifier of the group to send to.
	 * @param message
	 *                the object to send.
	 * 
	 * @throws IllegalStateException
	 *                 upon attempts to send to a disabled group
	 * @throws IOException
	 *                 upon message transmission failure
	 */
	public void sendMessage(String groupName, Serializable message)
	throws IOException;

	/**
	 * Adds a ViewChangeListener to this object.
	 * 
	 * @param groupName
	 *                the identifier of the group to add a listener to.
	 * @param listener
	 *                the new listener
	 */
	public void addViewChangeListener(String groupName,
			ViewChangeListener listener);

	/**
	 * Adds a MessageListener to this object.
	 * 
	 * @param groupName
	 *                the identifier of the group to add a listener to.
	 * @param listener
	 *                the new listener
	 */
	public void addMessageListener(String groupName, MessageListener listener);

	/**
	 * Connects to a group.
	 * 
	 * @param groupName
	 *                the name of the group to connect to
	 * @param localMemberName
	 *                the name of local member.
	 * @throws IOException
	 *                 upon group connect failure
	 * @throws IllegalStateException
	 *                 if trying to join static group where this local member
	 *                 name is not a member.
	 * @return the group definition of the joined group.
	 */
	public GroupDefinition joinGroup(String groupName, String localMemberName)
	throws IOException, IllegalStateException;

	/**
	 * Creates a group using the provided parameters.
	 * 
	 * @param localMemberName
	 *                the name of local member.
	 * @throws IllegalArgumentException
	 *                 for invalid parameter configurations
	 * @throws IOException
	 *                 upon group creation failure
	 * 
	 */
	public void createGroup(GroupDefinition description,
			String localMemberName) throws IOException;

	/**
	 * Removes a group from the RMI registry. This should only be allowed for
	 * the creator / owner of the group in the registry. Note that this does not
	 * mean that all existing members are kicked out, merely that no new members
	 * can find the group.
	 * 
	 * @param groupName
	 *                the name of the group to de-register
	 * @throws IOException
	 *                 upon connect failure to the registry.
	 * @throws IllegalStateException
	 *                 if trying to de-register a group without the required
	 *                 permissions.
	 */
	public void removeGroup(String groupName) throws IOException,
	IllegalStateException;

	/**
	 * Sets the adress and port of the RMI registry to be used by this GCom
	 * instance.
	 * 
	 * @param hostname
	 *                the hostname of the computer hosting the registry
	 * @param port
	 *                the port of the registry.
	 * @throws IOException
	 *                 if the registry could not be contacted.
	 */
	public void connectToRegistry(String host, int port) throws IOException;

	/**
	 * Disconnects from the group.
	 * 
	 * @param groupName
	 *                the name of the group to leave.
	 * @throws IOException
	 *                 upon disconnect failure
	 */
	public void disconnect(String groupName) throws IOException;

	/**
	 * Get a list of groups currently accessible through this GCom instance.
	 * 
	 * @return a list of group definitions.
	 * @throws IOException
	 *                 if the connection to the RMI registry previously
	 *                 specified fails.
	 */
	public List<GroupDefinition> listGroups() throws IOException;

	/**
	 * Return the local member for this GCom instance, in the specified group.
	 * 
	 * @param groupName
	 *                the affected group
	 */
	public MemberInterface getLocalMember(String groupName);

	/**
	 * Retrieves the group members.
	 * 
	 * @param groupName
	 *                the affected group
	 * @return an array containing group members
	 */
	public List<MemberInterface> getMembers(String groupName);

	/**
	 * A group definition specifies the type of group, the communication type,
	 * the type of message order in use the group description and (for static
	 * groups only) also the expected names of the members in the completed
	 * group.
	 * 
	 */
	public interface GroupDefinition extends Serializable {

		/**
		 * Returns the group type (Static / Dynamic) of this group
		 * 
		 * @return The group type
		 */
		public TYPE_GROUP getGroupType();

		/**
		 * Returns the communication type used in this group
		 * 
		 * @return The communication type
		 */
		public TYPE_COMMUNICATION getCommunicationType();

		/**
		 * Returns the message ordering type used in this group
		 * 
		 * @return The message ordering type
		 */
		public TYPE_MESSAGEORDERING getMessageOrderingType();

		/**
		 * @return The human-readable name or description of this group
		 */
		public String getGroupName();
	}

	/**
	 * A GCom Group member interface. Used to identify individual members in a
	 * GCom group.
	 */
	public interface MemberInterface extends Serializable {
		// ---------------------------------------------------------
		/**
		 * @return a unique group member identifier.
		 */
		public String getID();

		/**
		 * @return the human readable name of this Member
		 */
		public String getName();
	}


	/**
	 * A GCom view change listener interface that deals with changes to the
	 * group structure.
	 */
	public interface ViewChangeListener {
		/**
		 * Callback method when a new member joins the group.
		 * 
		 * @param member
		 *                the new member who has joined
		 */
		public void newMember(MemberInterface member);

		/**
		 * Callback method when a member leaves the group.
		 * 
		 * @param member
		 *                the new member who has left
		 */
		public void lostMember(MemberInterface member);
	}

	/**
	 * A GCom message listener interface that deals with messages delivered to a
	 * group
	 */
	public interface MessageListener {
		/**
		 * Callback method when a message is delivered to the group.
		 * 
		 * @param source
		 *                the source Member who sent the message.
		 * @param message
		 *                the message object.
		 */
		public void messageReceived(Serializable message);
	}


}