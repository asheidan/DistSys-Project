package gcom.interfaces;

import java.util.List;

/**
 * A GCom view change listener interface that deals with changes to the
 * group structure.
 */
public interface GComViewChangeListener {
	public void viewChanged(String groupName, List<Member> list);
}
