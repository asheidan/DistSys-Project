package se.luddoj.distsys.gcom.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.luddoj.distsys.gcom.Member;

public class MemberTest {

	private Member member;
	
	@Before
	public void setUp() {
		member = new Member("123", "test");
	}
	
	@Test
	public void testGetID() {
		String id = member.getID();
		assertEquals("123", id);
	}

	@Test
	public void testGetName() {
		String name = member.getName();
		assertEquals("test", name);
	}

	@Test
	public void testEquals() {
		Member member1 = new Member("123", "test");
		Member member2 = new Member("123", "test1");
		assertEquals(member, member1);
		assertEquals(member, member2);
	}
}
