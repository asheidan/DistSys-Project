package gcom.test;

import static org.junit.Assert.*;

import gcom.RMIModule;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Before;
import org.junit.Test;

public class RMIModuleTest {
	int port;
	Registry registry;
	RMIModule rmi;
	
	@Before
	public void setUp() throws Exception {
		port = (int) (Math.floor(Math.random() * 400) + 10240);
		registry = LocateRegistry.createRegistry(port);
		rmi = new RMIModule("localhost", port);
	}

	@Test
	public void testRMIModule() {
		assertNotNull(rmi);
	}

	@Test
	public void testBind() {
		fail("NOT IMPLEMENTED");
		//rmi.bind("Nisse", null);
	}

}
