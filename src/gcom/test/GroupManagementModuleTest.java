package gcom.test;

import static org.junit.Assert.*;

import java.util.List;

import gcom.GroupDefinition;
import gcom.GroupManagementModule;

import org.junit.Before;
import org.junit.Test;

public class GroupManagementModuleTest {

	private GroupManagementModule gmm;
	
	@Before
	public void setUp() {
		this.gmm = new GroupManagementModule();
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
	public void testGetGroup() {
		fail("Not yet implemented");
	}

}
