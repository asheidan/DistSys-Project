package gcom;

import gcom.interfaces.CommunicationModule;
import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.Message;
import gcom.interfaces.MessageListener;
import gcom.interfaces.MessageOrderingModule;
import gcom.interfaces.ViewChangeListener;
import gcom.interfaces.GroupDefinition;
import gcom.interfaces.Member;
import gcom.interfaces.RMIModule;
import gcom.interfaces.RemoteObject;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class GCom implements gcom.interfaces.GCom,gcom.interfaces.GComMessageListener {
	private Logger log = Logger.getLogger("gcom");

	private GroupManagementModule gmm = new gcom.GroupManagementModule();
	private RMIModule rmi;
	private Hashtable<String, CommunicationModule> comModules = new Hashtable<String, CommunicationModule>();
	private Hashtable<String, MessageOrderingModule> moModules = new Hashtable<String, MessageOrderingModule>();
	private Hashtable<String, HashVectorClock> clocks = new Hashtable<String, HashVectorClock>();
	private Hashtable<String, Member> identities = new Hashtable<String, Member>();
	private String processID;
	
	public GCom() {
		processID = String.valueOf(String.valueOf(Math.random()).hashCode());
		BasicConfigurator.configure();
		log.setLevel(Level.ALL);
		log.debug("gcom-object created");
	}
	
	@Override
	public void addMessageListener(String groupName, MessageListener listener) {
		MessageOrderingModule mom = moModules.get(groupName);
		if( mom != null ) {
			mom.addMessageListener(listener);
		}
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
			// TODO: Should this throw an exception if we aren't connected to RMI?
			RemoteObject remote = new gcom.RemoteObject(com,description);
			rmi.bind(groupName, remote);
			Member me = new gcom.Member(processID,localMemberName, remote);
			gmm.addGroup(description);
			gmm.addMember(groupName, me);
			identities.put(groupName,me);
			
			moModules.put(groupName, mom);
			comModules.put(groupName, com);
		}
		catch (AlreadyBoundException e) {
			log.debug("Trying to bind object for new group while name already exists: " + groupName);
		}
	}

	@Override
	public void disconnect(String groupName) throws IOException {
		CommunicationModule com = comModules.get(groupName);
		if( com != null ) {
			com.send(
					new gcom.Message(
							clocks.get(groupName),
							groupName,
							identities.get(groupName),
							"",
							Message.TYPE_MESSAGE.PART));
			gmm.removeGroup(groupName);
			comModules.remove(groupName);
			moModules.remove(groupName);
			clocks.remove(groupName);
			identities.remove(groupName);
		}
	}

	@Override
	public Member getLocalMember(String groupName) {
		return identities.get(groupName);
	}

	@Override
	public List<Member> getMembers(String groupName) {
		return gmm.listGroupMembers(groupName);
	}

	@Override
	public GroupDefinition joinGroup(String groupName,
			String localMemberName) throws IOException, IllegalStateException,NotBoundException {
		RemoteObject remoteRemote = rmi.getReference(groupName);
		GroupDefinition definition = remoteRemote.getDefinition();

		MessageOrderingModule mom;
		switch (definition.getMessageOrderingType()) {
		case NONORDERED:
			mom = new gcom.momNonOrdered();
			break;

		default:
			log.error("Unknown message-ordering type: " + definition.getMessageOrderingType());
			return null;
		}


		CommunicationModule com;
		switch(definition.getCommunicationType()) {
		case BASIC_UNRELIABLE_MULTICAST :
			com = new BasicCommunicationModule(mom, gmm, groupName);
			break;

		default:
			log.error("Unknown communication type: " + definition.getCommunicationType());
			return null;
		}
		RemoteObject localRemote = new gcom.RemoteObject(com, definition);
		Member me = new gcom.Member(processID, localMemberName, localRemote);
		remoteRemote.send(new gcom.Message(null, groupName, me, null, Message.TYPE_MESSAGE.JOIN));
		comModules.put(groupName, com);
		moModules.put(groupName, mom);
		gmm.addGroup(definition);
		return definition;
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
		CommunicationModule com = comModules.get(groupName);
		if(com != null) {
			com.send(
					new gcom.Message(clocks.get(groupName), groupName,
					identities.get(groupName), message, Message.TYPE_MESSAGE.APPLICATION));
		}
	}

	@Override
	public String[] listReferences() throws AccessException, RemoteException {
		return rmi.list();
	}

	@Override
	public void messageReceived(Message message) {
		// TODO !!!
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
