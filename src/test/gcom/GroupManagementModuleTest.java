package test.gcom;

import static org.junit.Assert.*;

import java.util.List;

import gcom.GroupDefinition;
import gcom.GroupManagementModule;
import gcom.Member;

import org.junit.Before;
import org.junit.Test;

public class GroupManagementModuleTest {

	private GroupManagementModule gmm;
	
	@Before
	public void setUp() {
		this.gmm = new gcom.GroupManagementModule();
	}

	@Test
	public void testAddListGroup() {
		GroupDefinition testGroup1 = new GroupDefinition("testGroup1");
		this.gmm.addGroup(testGroup1);

		List<gcom.interfaces.GroupDefinition> groups = this.gmm.listGroups();
		assertEquals(testGroup1, groups.get(0));
	}

	@Test
	public void testRemoveGroup() {
		GroupDefinition testGroup1 = new GroupDefinition("testGroup1");
		this.gmm.addGroup(testGroup1);
		this.gmm.removeGroup("testGroup1");

		List<gcom.interfaces.GroupDefinition> groups = this.gmm.listGroups();
		assertEquals(0, groups.size());
	}

	@Test
	public void testGetGroupDefinition() {
		GroupDefinition testGroup1 = new GroupDefinition("testGroup1");
		this.gmm.addGroup(testGroup1);
		GroupDefinition testGroup2 = new GroupDefinition("testGroup2");
		this.gmm.addGroup(testGroup2);

		gcom.interfaces.GroupDefinition returned_gd = this.gmm.getGroupDefinition("testGroup1");
		assertEquals(testGroup1, returned_gd);

		this.gmm.removeGroup("testGroup1");
		returned_gd = this.gmm.getGroupDefinition("testGroup1");
		assertNull(returned_gd);
	}

	@Test
	public void testAddGetGroupMember() {
		GroupDefinition testGroup1 = new GroupDefinition("testGroup1");
		this.gmm.addGroup(testGroup1);
		
		Member testMember = new Member("123", "test");
		this.gmm.addMember("testGroup1", testMember);

		List<gcom.interfaces.Member> returnedMembers = this.gmm.listGroupMembers("testGroup1");
		assertTrue("Returned 1 member", returnedMembers.size() == 1);
		assertEquals("Returned correct member", testMember, returnedMembers.get(0));
	}

}
