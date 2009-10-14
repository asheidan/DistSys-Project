package gcom;

// Imports
import gcom.interfaces.CommunicationModule;
import gcom.interfaces.GComMessageListener;
import gcom.interfaces.GComViewChangeListener;
import gcom.interfaces.GroupManagementModule;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;
import gcom.interfaces.ViewChangeListener;
import gcom.interfaces.GroupDefinition;
import gcom.interfaces.Member;
import gcom.interfaces.MessageSender;
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
import java.util.Vector;
import org.apache.log4j.Level;


public class GCom implements gcom.interfaces.GCom,GComMessageListener,GComViewChangeListener,MessageSender {

	private GroupManagementModule gmm = new gcom.GroupManagementModule();
	private RMIModule rmi;
	private Hashtable<String, CommunicationModule> comModules = new Hashtable<String, CommunicationModule>();
	private Hashtable<String, MessageOrderingModule> moModules = new Hashtable<String, MessageOrderingModule>();
	private Hashtable<String, HashVectorClock> clocks = new Hashtable<String, HashVectorClock>();
	private Hashtable<String, Member> identities = new Hashtable<String, Member>();
	private Hashtable<String, Vector<GComMessageListener>> messageListeners = new Hashtable<String, Vector<GComMessageListener>>();
	private Hashtable<String, Vector<ViewChangeListener>> viewChangeListeners = new Hashtable<String, Vector<ViewChangeListener>>();
	private String processID;
	
	public GCom() {
		//processID = String.valueOf(String.valueOf(Math.random()).hashCode());
		processID = "0x" + String.format("%06x",(int)Math.floor(Math.random() * Math.pow(2, 24))).toUpperCase();
		Debug.log(this, Level.DEBUG, "gcom-object created");
	}
	
	@Override
	public void addMessageListener(String groupName, GComMessageListener listener) {
		Vector<GComMessageListener> listeners = messageListeners.get(groupName);
		if( listeners == null ) {
			listeners = new Vector<GComMessageListener>();
			messageListeners.put(groupName, listeners);
		}
		listeners.add(listener);
	}

	@Override
	public void addViewChangeListener(String groupName,
			ViewChangeListener listener) {
		gmm.addViewChangeListener(groupName, listener);
	}

	@Override
	public void connectToRegistry(String host, int port) throws IOException {
		rmi = new gcom.RMIModule(host,port);
	}

	private void setIdentityInMOM(MessageOrderingModule mom, GroupDefinition def) {
		if(def.getMessageOrderingType() == GCom.TYPE_MESSAGEORDERING.TOTAL) {
			((momTotal)mom).setIdentity(identities.get(def.getGroupName()));
		}
	}
	
	private MessageOrderingModule setupMOM(GroupDefinition definition) {
		MessageOrderingModule mom;
		switch (definition.getMessageOrderingType()) {
			case NONORDERED:
				mom = new gcom.momNonOrdered(processID);
				break;
			case FIFO:
				mom = new gcom.momFIFO(processID);
				break;
			case CAUSAL:
				mom = new gcom.momCausal(processID);
				break;
			case CAUSALTOTAL:
				Debug.log(this, Level.WARN, "Unimplemented ordering: CAUSALTOTAL");
				return null;
			case TOTAL:
				mom = new momTotal(processID);
				try {
					RemoteObject seq = rmi.getReference("sequencer");
					((momTotal)mom).setSequencer(seq);
				}
				catch(Exception e) {
					Debug.log(this, Debug.ERROR, "Could not get sequencer");
					return null;
				}
				break;
			default:
				Debug.log(this, Debug.ERROR, "Unknown message-ordering type: " + definition.getMessageOrderingType());
				return null;
		}
		mom.addMessageListener(this);
		return mom;
	}

	private CommunicationModule setupCom(GroupDefinition definition, MessageOrderingModule mom) {
		CommunicationModule com;
		switch(definition.getCommunicationType()) {
		case BASIC_UNRELIABLE_MULTICAST :
			com = new BasicCommunicationModule(mom, gmm, definition.getGroupName(), processID);
			break;
		case BASIC_RELIABLE_MULTICAST:
		default:
			Debug.log(this, Debug.ERROR, "Unknown communication type: " + definition.getCommunicationType());
			return null;
		}
		com.addGComViewChangeListener(this);
		return com;
	}

	@Override
	public void createGroup(GroupDefinition description,
			String localMemberName) throws IOException {
		String groupName = description.getGroupName();
		
		MessageOrderingModule mom = setupMOM(description);
		CommunicationModule com = setupCom(description,mom);

		try {
			// TODO: Should this throw an exception if we aren't connected to RMI?
			RemoteObject remote = new gcom.RemoteObject(com,description);
			rmi.bind(groupName, remote);
			Member me = new gcom.Member(processID,localMemberName, remote);
			gmm.addGroup(description);
			gmm.addMember(groupName, me);
			identities.put(groupName,me);
			
			setIdentityInMOM(mom,description);

			clocks.put(groupName, new HashVectorClock(processID));
			moModules.put(groupName, mom);
			comModules.put(groupName, com);
			// ReferenceKeeper isn't broken
			new ReferenceKeeper(rmi, groupName, remote);
		}
		catch (AlreadyBoundException e) {
			Debug.log(this, Debug.DEBUG, "Trying to bind object for new group while name already exists: " + groupName);
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
							Message.TYPE_MESSAGE.PARTREQUEST));
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

		MessageOrderingModule mom = setupMOM(definition);
		CommunicationModule com = setupCom(definition,mom);

		RemoteObject localRemote = new gcom.RemoteObject(com, definition);
		Member me = new gcom.Member(processID, localMemberName, localRemote);
		HashVectorClock clock = new HashVectorClock(processID);
		remoteRemote.send(new gcom.Message(clock, groupName, me, null, Message.TYPE_MESSAGE.JOINREQUEST));
		clocks.put(groupName, clock);
		comModules.put(groupName, com);
		moModules.put(groupName, mom);
		identities.put(groupName, me);
		
		setIdentityInMOM(mom,definition);
	
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
		throw new UnsupportedOperationException();
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
	@SuppressWarnings("unchecked")
	public void messageReceived(Message message) {
		Debug.log("gcom.GCom.messageReceived", Debug.DEBUG, "Got a " + message.getMessageType());
		String groupName = message.getGroupName();
		GroupDefinition definition = gmm.getGroupDefinition(groupName);
		if(definition == null) {
			Debug.log("gcom.GCom", Debug.DEBUG,
					"Got message from a group I'm not a member of." + message.toString());
			return;
		}
		switch(message.getMessageType()) {
			// TODO: should information about parting/joining be sent to "gui"?
			case JOINREQUEST:
				gotMember(groupName, message.getSource());
				List<Member> view = gmm.listGroupMembers(groupName);
				Message msg = new gcom.Message(clocks.get(groupName), groupName, identities.get(groupName), (Serializable)view, Message.TYPE_MESSAGE.WELCOME);
				try {
					// TODO: COM should be expanded with private messages
					Debug.log(this, Level.DEBUG,
						"Welcoming " + message.getSource().toString() +
						" to " + groupName + " via " + message.getSource().getRemoteObject());
					message.getSource().getRemoteObject().send(msg);
				} catch (RemoteException ex) {
					Debug.log(this,Debug.WARN, "Got remote exception", ex);
				}
				break;
			case WELCOME:
				for(Member m : (List<Member>)message.getMessage()) {
					gmm.addMember(groupName, m);
				}
				gmm.setLeader(groupName,message.getSource());
				break;
			case GOTMEMBER:
				gmm.addMember(groupName, (Member)message.getMessage());
				break;
			case PARTREQUEST:
				lostMember(groupName, message.getSource());
				// TODO: Will we send a reject now? Right now a node will remove itself but still be able to send messages
				break;
			case LOSTMEMBER:
				// TODO: Should we try to contact the lost member?
				gmm.removeMember(groupName, (Member)message.getMessage());
				break;
			case APPLICATION:
				sendToMessageListeners(groupName,message);
				break;
			case SEQUENCE:
				Debug.log(this, Debug.WARN, "Someone is sending sequence-requests to me: " + message.getSource());
				break;
			case ELECTION:
				election(message);
				break;
		}
	}

	private void sendToMessageListeners(String groupName, Message message) {
		for(GComMessageListener l : messageListeners.get(groupName)) {
			l.messageReceived(message);
		}
	}
	
	@Override
	public String getProcessID() {
		return processID;
	}

	private Hashtable<String,Vector<Member>> electionResults = new Hashtable<String,Vector<Member>>();
	private Hashtable<String,String> highestElectionValues = new Hashtable<String,String>();
	private Vector<Member> setupElection(String groupName) {
		Vector<Member> results;
		results = electionResults.get(groupName);
		if(results == null || results.size() == 0) {
			results = new Vector<Member>(gmm.listGroupMembers(groupName));
			results.removeElement(identities.get(groupName));
			electionResults.put(groupName,results);
			highestElectionValues.put(groupName,processID);
			gmm.setLeader(groupName,identities.get(groupName));
		}
		return results;
	}
	private void election(Message message) {
		String groupName = message.getGroupName();
		Vector<Member> results = setupElection(groupName);
		if(!results.contains(message.getSource())) {
			return;
		}
		switch(highestElectionValues.get(groupName).compareTo((String)message.getMessage())) {
			case 0:
				Debug.log(this, Debug.DEBUG, "Same leader chosen by: " + message.getSource());
				break;
			case -1:
				Debug.log(this, Debug.DEBUG, "Got electionmessage with lower value from: " + message.getSource());
				// Send my value, not highest
				comModules.get(groupName).send(
					message.getSource(),
					new gcom.Message(
						clocks.get(groupName),
						groupName,
						identities.get(groupName),
						processID,
						Message.TYPE_MESSAGE.ELECTION));
				break;
			case 1:
				Debug.log(this, Debug.DEBUG, "Got electionmessage with higher value from: " + message.getSource());
				// Send his value
				comModules.get(groupName).send(
					message.getSource(),
					new gcom.Message(
						clocks.get(groupName),
						groupName,
						identities.get(groupName),
						message.getMessage(),
						Message.TYPE_MESSAGE.ELECTION));
				highestElectionValues.put(groupName,(String)message.getMessage());
				gmm.setLeader(groupName,message.getSource());
				break;
		}
		results.removeElement(message.getSource());
		if(results.size() == 0) {
			Debug.log(this, Debug.DEBUG, "Election is over");
			if(highestElectionValues.get(groupName).equals(processID)) {
				Debug.log(this, Debug.DEBUG, "I'm elected as leader");
				// TODO: Should keepers be saved incase we want to kill them when we leave a group?
				new ReferenceKeeper(rmi, groupName, identities.get(groupName).getRemoteObject());
			}
			else {
				Debug.log(this, Debug.DEBUG, "Someone else is leader: " + gmm.getLeader(groupName));
			}
		}
		else {
			Debug.log(this, Debug.DEBUG, "Waiting for results from: " + results.toString());
		}
	}

	@Override
	public void lostMember(String groupName, Member member) {
		if(gmm.memberIsInGroup(groupName, member)) {
			gmm.removeMember(groupName, member);

			Message msg = new gcom.Message(clocks.get(groupName), groupName, identities.get(groupName), member, Message.TYPE_MESSAGE.LOSTMEMBER);
			comModules.get(groupName).send(msg);
			if(member.equals(gmm.getLeader(groupName))) {
				// TODO: Commence election
				Debug.log(this, Debug.WARN, "We lost our leader: " + member.toString());
				setupElection(groupName);
				if(electionResults.get(groupName).size() == 0) {
					Debug.log(this, Debug.DEBUG, "I'm leader in an empty group...");
					// TOOD: create referencekeeper
					new ReferenceKeeper(rmi, groupName, identities.get(groupName).getRemoteObject());
				}
				else {
					comModules.get(groupName).send(
						new gcom.Message(
							clocks.get(groupName),
							groupName,
							identities.get(groupName),
							highestElectionValues.get(groupName),
							Message.TYPE_MESSAGE.ELECTION
						)
					);
				}
			}
		}
		else {
			Debug.log(this, Debug.DEBUG, "Trying to remove member which isn't present: " + member.toString());
		}
	}

	@Override
	public void gotMember(String groupName, Member member) {
		if(!gmm.memberIsInGroup(groupName, member)) {
			Message msg = new gcom.Message(clocks.get(groupName), groupName, identities.get(groupName), member, Message.TYPE_MESSAGE.GOTMEMBER);
			comModules.get(groupName).send(msg);
			
			gmm.addMember(groupName, member);
		}
		else {
			Debug.log(this, Debug.DEBUG, "Trying to add member which is already present: " + member.toString());
		}
	}

}
