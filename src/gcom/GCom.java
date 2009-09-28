package gcom;

import gcom.interfaces.CommunicationModule;
import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.MessageListener;
import gcom.interfaces.ViewChangeListener;
import gcom.interfaces.GroupDefinition;
import gcom.interfaces.Member;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class GCom implements gcom.interfaces.GCom {

	private GroupManagementModule gmm = new gcom.GroupManagementModule();
	
	@Override
	public void addMessageListener(String groupName, MessageListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addViewChangeListener(String groupName,
			ViewChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectToRegistry(String host, int port) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createGroup(GroupDefinition description,
			String localMemberName) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void disconnect(String groupName) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Member getLocalMember(String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Member> getMembers(String groupName) {
		return gmm.listGroupMembers(groupName);
	}

	@Override
	public GroupDefinition joinGroup(String groupName,
			String localMemberName) throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GroupDefinition> listGroups() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeGroup(String groupName) throws IOException,
			IllegalStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String groupName, Serializable message)
			throws IOException {
		// TODO Auto-generated method stub

	}

}
