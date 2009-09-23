package se.luddoj.distsys.gcom;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class GCom implements GComInterface {

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
	public void createGroup(GroupDefinitionInterface description,
			String localMemberName) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect(String groupName) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public MemberInterface getLocalMember(String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberInterface> getMembers(String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GroupDefinitionInterface joinGroup(String groupName,
			String localMemberName) throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GroupDefinitionInterface> listGroups() throws IOException {
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
