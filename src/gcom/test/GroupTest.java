package gcom.test;

import static org.junit.Assert.*;
import gcom.*;

import org.junit.Before;
import org.junit.Test;

public class GroupTest {
	public Group group;
	public GroupDefinition gdef;

	@Before
	public void setUp() {
		gdef = new GroupDefinition("testGroup");
		group = new Group(gdef);
	}

	@Test
	public void testCreateObject() {
		assertNotNull(group);
	}

	@Test
	public void testAddMember() {
		Member member = new Member("123", "test");
		assertEquals(0, group.listMembers().size());
		group.addMember(member);
		assertEquals(1, group.listMembers().size());
		assertEquals(member, group.listMembers().get(0));
	}

	@Test
	public void testRemoveMember() {
		Member member = new Member("123", "test");
		assertEquals(0, group.listMembers().size());
		group.addMember(member);
		assertEquals(1, group.listMembers().size());
		group.removeMember(member);
		assertEquals(0, group.listMembers().size());
	}

	@Test
	public void testListMembers() {
		Member member1 = new Member("123", "test1");
		Member member2 = new Member("456", "test2");
		Member member3 = new Member("789", "test3");
		assertEquals(0, group.listMembers().size());
		group.addMember(member1);
		group.addMember(member2);
		group.addMember(member3);
		assertEquals(member1, group.listMembers().get(0));
		assertEquals(member2, group.listMembers().get(1));
		assertEquals(member3, group.listMembers().get(2));
	}

	@Test
	public void testGetGroupDefinition() {
		GroupDefinition g = (GroupDefinition) group.getGroupDefinition();
		assertEquals(gdef, g);
	}

}
