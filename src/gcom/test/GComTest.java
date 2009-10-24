package gcom.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import gcom.GCom;
import gcom.GroupDefinition;
import gcom.interfaces.RemoteObject;

import rmi.BackdoorOpener;
import gcom.interfaces.Backdoor;
import java.rmi.server.UnicastRemoteObject;

import org.junit.Before;
import org.junit.Test;

public class GComTest {
	private GCom gcom;
	Registry registry;
	int port;

	@Before
	public void setUp() throws Exception {
		port = (int)Math.floor(Math.random()*400 + 10240);
		gcom = new GCom();
		try {
			registry = LocateRegistry.createRegistry(port);
			BackdoorOpener backdoor = new BackdoorOpener(port);
			registry.rebind(Backdoor.NAME, UnicastRemoteObject.exportObject(backdoor,0));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

	@Test
	public void testGCom() {
		assertNotNull(gcom);
	}

	@Test
	public void testAddMessageListener() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

	@Test
	public void testAddViewChangeListener() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

	@Test
	public void testConnectToRegistry() {
		try {
			gcom.connectToRegistry("localhost", 1);
		}
		catch(IOException e) {
			assertTrue(true);
		}
		
		try {
			gcom.connectToRegistry("localhost", port);
			assertTrue(true);
		}
		catch(IOException e) {
			fail("Could not connect to registry: " + e.getMessage());
		}
	}

	@Test
	public void testCreateGroup() {
		String groupName = "Nisse";
		GroupDefinition def = new gcom.GroupDefinition(groupName);
		try {
			gcom.connectToRegistry("localhost", port);
			gcom.createGroup(def, "Tomten");
			RemoteObject ro = (RemoteObject) registry.lookup(groupName);
			assertNotNull(ro);
		}
		catch (AlreadyBoundException e) {
			fail("Another with the same reference already exists");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			fail("Reference wasn't bound");
			e.printStackTrace();
		}
	}

	@Test
	public void testDisconnect() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

	@Test
	public void testGetLocalMember() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

	@Test
	public void testGetMembers() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

	@Test
	public void testJoinGroup() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

	@Test
	public void testListGroups() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

	@Test
	public void testRemoveGroup() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

	@Test
	public void testSendMessage() {
		// TODO: Not implemented test
		//fail("Not yet implemented");
	}

}
