package gcom.test;

import static org.junit.Assert.*;

import gcom.RMIModule;
import gcom.interfaces.GroupDefinition;
import gcom.interfaces.Message;
import gcom.interfaces.RemoteObject;

import rmi.BackdoorOpener;
import gcom.interfaces.Backdoor;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.BackdoorOpener;
import gcom.interfaces.Backdoor;
import java.rmi.server.UnicastRemoteObject;

import org.junit.Before;
import org.junit.Test;

public class RMIModuleTest {
	int port;
	Registry registry;
	RMIModule rmi;
	
	@Before
	public void setUp() throws Exception {
		port = (int) (Math.floor(Math.random() * 400) + 10240);
		// -Djava.rmi.server.codebase=file://${workspace_loc}/"RMI%20Test/bin/"
		registry = LocateRegistry.createRegistry(port);
		BackdoorOpener backdoor = new BackdoorOpener(port);
		registry.rebind(Backdoor.NAME, UnicastRemoteObject.exportObject(backdoor,0));
		rmi = new RMIModule("localhost", port);
	}

	@Test
	public void testRMIModule() {
		assertNotNull(rmi);
	}

	@Test
	public void testBind() {
		String name = "Nisse";
		RemoteMockup ro = new RemoteMockup();
		try {
			rmi.bind(name, ro);
			RemoteObject payload = (RemoteObject) registry.lookup(name);
			assertNotNull(payload);
		}
		catch(AlreadyBoundException e) {
			fail("Already bound: " + e.getMessage());
		} catch (NotBoundException e) {
			fail("Not bound: " + e.getMessage());
		} catch (AccessException e) {
			fail("AccessException: " + e.getMessage());
		} catch (RemoteException e) {
			fail("RemoteException: " + e.getMessage());
		}
	}
	
	private class RemoteMockup implements gcom.interfaces.RemoteObject {
	    private static final long serialVersionUID = 0L;
		@Override
		public void send(Message m) {}

		@Override
		public GroupDefinition getDefinition() {
			return null;
		}
		public double getUnique() { return 0.0; }
	}
}
