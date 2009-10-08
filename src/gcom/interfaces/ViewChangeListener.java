package gcom.interfaces;

/**
 * A GCom view change listener interface that deals with changes to the
 * group structure.
 */
public interface ViewChangeListener {
	public void gotMember(Member member);
	public void lostMember(Member member);
}
