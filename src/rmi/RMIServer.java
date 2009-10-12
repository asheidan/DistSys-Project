package rmi;

import gcom.Debug;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import javax.swing.JOptionPane;

public class RMIServer {
	protected Registry registry;
	public static void main(String [] args) {
		
		try {
			new RMIServer(Integer.valueOf(args[0]));
			JOptionPane.showMessageDialog(null,"RMIRegistry is launched\nListening on port " + args[0],"RMI:" + args[0],JOptionPane.INFORMATION_MESSAGE);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			Debug.log("RMIServer",Debug.ERROR, "You must supply a portnumber");
		}
		System.exit(0);
	}
	
	public RMIServer(int port) {
		try {
			registry = LocateRegistry.createRegistry(port);
		}
		catch(RemoteException e) {
			Debug.log("RMIServer",Debug.ERROR, "Got remote exception", e);
		}
	}
}
