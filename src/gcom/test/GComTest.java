package gcom.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import gcom.GCom;
import gcom.GroupDefinition;

import org.junit.Before;
import org.junit.Test;

public class GComTest {
	private GCom gcom;
	Registry registry;
	int port = (int)Math.floor(Math.random()*400 + 10240);

	@Before
	public void setUp() throws Exception {
		gcom = new GCom();
		try {
			registry = LocateRegistry.createRegistry(port);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	public void testGCom() {
		assertNotNull(gcom);
	}

	@Test
	public void testAddMessageListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddViewChangeListener() {
		fail("Not yet implemented");
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
		GroupDefinition def = new gcom.GroupDefinition("Nisse");
		try {
			gcom.createGroup(def, "Tomten");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Not yet implemented");
	}

	@Test
	public void testDisconnect() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLocalMember() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMembers() {
		fail("Not yet implemented");
	}

	@Test
	public void testJoinGroup() {
		fail("Not yet implemented");
	}

	@Test
	public void testListGroups() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveGroup() {
		fail("Not yet implemented");
	}

	@Test
	public void testSendMessage() {
		fail("Not yet implemented");
	}

}
