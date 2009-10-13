package gcom;

import gcom.interfaces.RemoteObject;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Member implements gcom.interfaces.Member {

	private static final long serialVersionUID = 6940203241759957894L;
	private String id;
	private String name;
	private RemoteObject remote;
	
	public Member(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Member(String id, String name, RemoteObject remote) {
		this.id = id;
		this.name = name;
		try {
			this.remote = (RemoteObject) UnicastRemoteObject.exportObject(remote,0);
		} catch (RemoteException ex) {
			Debug.log(this, Debug.WARN, "Trying to export already exported RemoteObject");
			this.remote = remote;
		}
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object m) {
		if(Member.class.isInstance(m)) {
			return ((Member) m).getID().equals(this.id);
		}
		return false;
	}

	@Override
	public String toString() {
		return id + ":" + name;
	}

	@Override
	public RemoteObject getRemoteObject() {
		return remote;
	}
}
