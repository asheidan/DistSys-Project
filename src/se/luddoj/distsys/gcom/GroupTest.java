package se.luddoj.distsys.gcom;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


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
		Member member = new Member();
		assertEquals(g.listMembers().size(), 0);
		g.addMember(member);
		assertEquals(g.listMembers().size(), 1);
	}

	@Test
	public void testRemoveMember() {
		fail("Not yet implemented");
	}

	@Test
	public void testListMembers() {
		fail("Not yet implemented");
	}

}
