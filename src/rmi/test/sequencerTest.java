package rmi.test;

import gcom.interfaces.DebugInterface;
import static org.junit.Assert.*;

import rmi.*;
import gcom.Member;
import gcom.interfaces.CommunicationModule;
import gcom.Message;
import gcom.interfaces.MessageListener;
import gcom.interfaces.Message.TYPE_MESSAGE;
import gcom.interfaces.GComMessageListener;
import gcom.interfaces.GComViewChangeListener;
import gcom.RemoteObject;
import gcom.HashVectorClock;
import gcom.GroupDefinition;
import gcom.interfaces.GCom.TYPE_MESSAGEORDERING;
import gcom.interfaces.GCom.TYPE_COMMUNICATION;
import gcom.interfaces.GCom.TYPE_GROUP;

import java.io.Serializable;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;
import java.rmi.registry.Registry;
import java.rmi.Remote;

public class sequencerTest {

	private SequencerCommunicationModule seq;
	private CommunicationMockup com;
	private Member me;
	
	@Before
	public void setUp() {
		mockRegistry registry = new mockRegistry();
		seq = new SequencerCommunicationModule(registry);
		com = new CommunicationMockup();
		RemoteObject r = new RemoteObject(com, null);
		me = new Member("0", "test0", r);
	}
	
	@Test
	public void testMessagesOutOfOrder() {
		Member source1 = new Member("1", "test1");
		Member source2 = new Member("2", "test2");
		Member source3 = new Member("3", "test3");
		
		Member[] s = {source1, source2, source3};
		Message m0 = getMessageArray(source2, s, new int[] {0,1,0}, "0");
		Message m1 = getMessageArray(source1, s, new int[] {1,1,0}, "1");
		Message m2 = getMessageArray(source3, s, new int[] {0,0,1}, "2");
		Message m3 = getMessageArray(source2, s, new int[] {1,2,1}, "3");
		Message m4 = getMessageArray(source3, s, new int[] {1,2,2}, "4");
		Message m5 = getMessageArray(source1, s, new int[] {2,2,2}, "5");

		seq.receive(packMessage(m0));
		seq.receive(packMessage(m1));
		seq.receive(packMessage(m2));
		seq.receive(packMessage(m5));
		seq.receive(packMessage(m3));
		seq.receive(packMessage(m4));

		assertEquals("All messages recieved", 6, com.recieved.size());
		assertEquals(1, com.recieved.get(0).getClock().getValue("serialNo").intValue());
		assertEquals(2, com.recieved.get(1).getClock().getValue("serialNo").intValue());
		assertEquals(3, com.recieved.get(2).getClock().getValue("serialNo").intValue());
		assertEquals(4, com.recieved.get(3).getClock().getValue("serialNo").intValue());
		assertEquals(5, com.recieved.get(4).getClock().getValue("serialNo").intValue());
		assertEquals(6, com.recieved.get(5).getClock().getValue("serialNo").intValue());

		assertEquals(m0.getMessage(), com.recieved.get(0).getMessage());
		assertEquals(m1.getMessage(), com.recieved.get(1).getMessage());
		assertEquals(m2.getMessage(), com.recieved.get(2).getMessage());
		assertEquals(m3.getMessage(), com.recieved.get(3).getMessage());
		assertEquals(m4.getMessage(), com.recieved.get(4).getMessage());
		assertEquals(m5.getMessage(), com.recieved.get(5).getMessage());

	}

	private Message getMessageArray(Member source, Member[] sources, int[] ticks, String dataN) {
		HashVectorClock clock = new HashVectorClock(source.getID());
		for(int i=0; i<sources.length; i++) {
			for(int j=0; j<ticks[i]; j++) {
				clock.tickKey(sources[i].getID());
			}
		}
		Message m = new Message(clock, "group1", source, "test data"+dataN, TYPE_MESSAGE.APPLICATION);
		return m;
	}

	private Message packMessage(Message m) {
		m.setReturnMember(me);
		return m;
	}

	private class mockRegistry implements Registry {
		public void bind(String name, Remote obj) {}

		public String[] list() { return null; }

		public Remote lookup(String name) {
			GroupDefinition g = new GroupDefinition("testGroup", 
				TYPE_COMMUNICATION.BASIC_UNRELIABLE_MULTICAST, 
				TYPE_GROUP.DYNAMIC, 
				TYPE_MESSAGEORDERING.CAUSALTOTAL);

			RemoteObject r = new RemoteObject(null, g);
			return (Remote)r;	
		}
		
		public void rebind(String name, Remote obj) {}
		
		public void unbind(String name) {}

	}
	
	public class CommunicationMockup implements CommunicationModule {
		public Vector<gcom.interfaces.Message> recieved;

		public CommunicationMockup() {
			recieved = new Vector<gcom.interfaces.Message>();
		}

		@Override
		public void receive(gcom.interfaces.Message m) {
			recieved.add(m);
		}

		public void addGComViewChangeListener(GComViewChangeListener l) {}

		@Override
		public void send(gcom.interfaces.Message m) {
			throw new java.lang.RuntimeException("You're using a mockup!");
		}

		@Override
		public void send(gcom.interfaces.Member member, gcom.interfaces.Message msg) {
			throw new java.lang.RuntimeException("You're using a mockup!");
		}

		@Override
		public void attachDebugger(DebugInterface debug) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}
}
