package gcom;

public class Member implements gcom.interfaces.Member {

	private static final long serialVersionUID = 6940203241759957894L;
	private String id;
	private String name;
	
	public Member(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public boolean equals(Object m) {
		if(Member.class.isInstance(m)) {
			return ((Member) m).getID().equals(this.id);
		}
		return false;
	}

}
