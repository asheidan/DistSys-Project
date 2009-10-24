package rmi;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import gcom.Debug;
import javax.swing.JOptionPane;
import gcom.RemoteObject;
import gcom.GroupDefinition;
import gcom.HashVectorClock;
import gcom.interfaces.Member;
import gcom.interfaces.Message;
import gcom.interfaces.GComViewChangeListener;
import gcom.interfaces.Message.TYPE_MESSAGE;

import java.util.Hashtable;

public class Sequencer extends RMIServer {

	public static void main(String [] args) {
		try {
			new Sequencer(Integer.valueOf(args[0]));
			JOptionPane.showMessageDialog(null,"RMIRegistry is launched\nListening on port " + args[0],"RMI:" + args[0],JOptionPane.INFORMATION_MESSAGE);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			Debug.log("RMIServer",Debug.ERROR, "You must supply a portnumber");
		}
		catch(RemoteException e) {
			Debug.log("RMIServer",Debug.ERROR, "Got remote exception", e);
		}
		System.exit(0);
	}

	public Sequencer(int port) throws RemoteException {
		super(port);
		SequencerCommunicationModule com = new SequencerCommunicationModule(this.registry);
		GroupDefinition group = new gcom.GroupDefinition("sequencer");
		RemoteObject ro = new RemoteObject(com, group);
		try {
			registry.bind("sequencer", UnicastRemoteObject.exportObject(ro, 0));
		} catch(Exception e) {
			Debug.log("Sequencer",Debug.ERROR, "Got exception", e);
		}
	}

}
