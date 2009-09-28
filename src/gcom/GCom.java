package gcom;

import gcom.interfaces.CommunicationModule;
import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.Message;
import gcom.interfaces.MessageListener;
import gcom.interfaces.MessageOrderingModule;
import gcom.interfaces.RemoteObject;
import gcom.interfaces.ViewChangeListener;
import gcom.interfaces.GroupDefinition;
import gcom.interfaces.Member;
import gcom.interfaces.RMIModule;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class GCom implements gcom.interfaces.GCom {
	private Logger log = Logger.getLogger("gcom");

	private GroupManagementModule gmm = new gcom.GroupManagementModule();
	private RMIModule rmi;
	private Hashtable<String, CommunicationModule> comModules = new Hashtable<String, CommunicationModule>();
	
	public GCom() {
		BasicConfigurator.configure();
		log.setLevel(Level.ALL);
		log.debug("gcom-object created");
	}
	
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
		rmi = new gcom.RMIModule(host,port);
	}

	@Override
	public void createGroup(GroupDefinition description,
			String localMemberName) throws IOException {
		// TODO Auto-generated method stub
		String groupName = description.getGroupName();
		
		MessageOrderingModule mom;
		switch (description.getMessageOrderingType()) {
		case NONORDERED:
			mom = new gcom.momNonOrdered();
			break;

		default:
			log.error("Unknown message-ordering type: " + description.getMessageOrderingType());
			return;
		}
		
		CommunicationModule com;
		switch(description.getCommunicationType()) {
		case BASIC_UNRELIABLE_MULTICAST :
			com = new BasicCommunicationModule(mom, gmm, groupName);
			break;
			
		default:
			log.error("Unknown communication type: " + description.getCommunicationType());
			return;
		}
		
		try {
			rmi.bind(groupName, new gcom.RemoteObject(com));
			gmm.addGroup(description);
			comModules.put(groupName, com);
		}
		catch (AlreadyBoundException e) {
			log.debug("Trying to bind object for new group which already exists: " + groupName);
		}
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
		return gmm.listGroups();
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
