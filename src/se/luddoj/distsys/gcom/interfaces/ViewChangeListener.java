package se.luddoj.distsys.gcom.interfaces;

/**
 * A GComInterface view change listener interface that deals with changes to the
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
