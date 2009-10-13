package gcom.test;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import gcom.HashVectorClock;
import gcom.RemoteObject;
import gcom.GroupDefinition;
import gcom.interfaces.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoteObjectTest {
	RemoteObject ro;
	CommunicationMockup com;
	GroupDefinition def;

	
	@Before
	public void setUp() throws Exception {
		com = new CommunicationMockup();
		def = new GroupDefinition("test"); 
		ro = new RemoteObject(com,def);
	}

	@Test
	public void testRemoteObject() {
		assertNotNull(ro);
	}

	@Test
	public void testSend() throws InterruptedException {
		Message m = new MessageMockup();
		ro.send(m);
		Thread.sleep(1000);
		assertEquals(m, com.lastMessage);
	}
	
	@Test
	public void testGetDefinition() {
		assertEquals(def, ro.getDefinition());
	}
	
	@Test
	public void testBindObjectToRegistry() {
		int port = (int) (Math.floor(Math.random() * 400) + 10240);
		// -Djava.rmi.server.codebase=file://${workspace_loc}/"RMI%20Test/bin/"
		try {
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("test", UnicastRemoteObject.exportObject(ro, 0));
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@After
	public void tearDown() {
		ro.stop();
	}

	@SuppressWarnings("serial")
	private class MessageMockup implements Message {

		@Override
		public HashVectorClock getClock() {
			return null;
		}

		@Override
		public String getGroupName() {
			return null;
		}

		@Override
		public Serializable getMessage() {
			return null;
		}

		@Override
		public TYPE_MESSAGE getMessageType() {
			return null;
		}

		@Override
		public Member getSource() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Member getReciever() {
			return null;
		}

		@Override
		public void setReciever(Member source) {
		}

	}
	
}
