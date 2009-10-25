package rmi;

import gcom.Debug;
import gcom.interfaces.Backdoor;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;

public class RMIServer {
	protected Registry registry;
	private Backdoor backdoor;

	public static void main(String [] args) {
		try {
			new RMIServer(Integer.valueOf(args[0]));
			JOptionPane.showMessageDialog(null,"RMIRegistry is launched\nListening on port " + args[0],"RMI:" + args[0],JOptionPane.INFORMATION_MESSAGE);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			Debug.log(this,Debug.ERROR, "You must supply a portnumber");
		}
		catch(RemoteException e) {
			Debug.log(this,Debug.ERROR, "Got remote exception", e);
		}
		System.exit(0);
	}
	
	public RMIServer(int port) throws RemoteException {
		registry = LocateRegistry.createRegistry(port);
		backdoor = new BackdoorOpener(port);
		registry.rebind(Backdoor.NAME, UnicastRemoteObject.exportObject(backdoor,0));
	}
}
