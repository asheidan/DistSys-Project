package gcom.interfaces;

import java.util.List;

/**
 * A GCom view change listener interface that deals with changes to the
 * group structure.
 */
public interface GComViewChangeListener {
	public void gotMember(String groupName, Member member);
	public void lostMember(String groupName, Member member);
}
