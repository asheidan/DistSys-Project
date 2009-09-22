package se.luddoj.distsys.gcom.interfaces;

import java.io.Serializable;

/**
 * A GComInterface Group member interface. Used to identify individual members in a
 * GComInterface group.
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
