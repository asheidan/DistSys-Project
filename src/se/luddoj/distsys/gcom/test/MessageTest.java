package se.luddoj.distsys.gcom.test;

import static org.junit.Assert.*;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import se.luddoj.distsys.gcom.*;
import se.luddoj.distsys.gcom.MessageInterface.TYPE_MESSAGE;


public class MessageTest {

	public Message m;
	public HashVectorClock clock;
	public Member source;
	
	@Before
	public void setUp() {
		clock = new HashVectorClock(0);
		source = new Member("123", "test");
		m = new Message(clock, "testGroup", source, "test", TYPE_MESSAGE.APPLICATION);
	}
	
	@Test
	public void testGetClock() {
		HashVectorClock c = m.getClock();
		assertEquals(clock, c);
	}
	
	@Test
	public void testGetMember() {
		Member member = m.getMember();
		assertEquals(source, member);
	}

	@Test
	public void testGetGroupName() {
		String n = m.getGroupName();
		assertEquals("testGroup", n);
	}

	@Test
	public void testGetMessage() {
		Serializable data = m.getMessage();
		assertEquals("test", data);
	}
	
	@Test
	public void testGetMessageType() {
		TYPE_MESSAGE type = m.getMessageType();
		assertEquals(TYPE_MESSAGE.APPLICATION, type);
	}

}
