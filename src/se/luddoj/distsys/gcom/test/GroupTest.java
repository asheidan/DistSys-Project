package se.luddoj.distsys.gcom.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.luddoj.distsys.gcom.*;


public class GroupTest {
	public Group g;
	
	@Before
	public void setUp() {
		g = new Group();
	}
	
	@Test
	public void testCreateObject() {
		assertNotNull(g);
	}
	
	@Test
	public void testAddMember() {
		Member member = new Member("123", "test");
		assertEquals(0, g.listMembers().size());
		g.addMember(member);
		assertEquals(1, g.listMembers().size());
		assertEquals(member, g.listMembers().get(0));
	}

	@Test
	public void testRemoveMember() {
		Member member = new Member("123", "test");
		assertEquals(0, g.listMembers().size());
		g.addMember(member);
		assertEquals(1, g.listMembers().size());
		g.removeMember(member);
		assertEquals(0, g.listMembers().size());
	}

	@Test
	public void testListMembers() {
		Member member1 = new Member("123", "test1");
		Member member2 = new Member("456", "test2");
		Member member3 = new Member("789", "test3");
		assertEquals(0, g.listMembers().size());
		g.addMember(member1);
		g.addMember(member2);
		g.addMember(member3);
		assertEquals(member1, g.listMembers().get(0));
		assertEquals(member2, g.listMembers().get(1));
		assertEquals(member3, g.listMembers().get(2));
	}

}
