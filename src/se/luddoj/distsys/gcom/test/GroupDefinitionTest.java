package se.luddoj.distsys.gcom.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import se.luddoj.distsys.gcom.GroupDefinition;
import se.luddoj.distsys.gcom.GComInterface.TYPE_COMMUNICATION;
import se.luddoj.distsys.gcom.GComInterface.TYPE_GROUP;
import se.luddoj.distsys.gcom.GComInterface.TYPE_MESSAGEORDERING;

public class GroupDefinitionTest {

	public GroupDefinition gdef_simple;
	public GroupDefinition gdef_custom;
	
	@Before
	public void setUp() {
		gdef_simple = new GroupDefinition("testGroup1");
		gdef_custom = new GroupDefinition("testGroup2",
					TYPE_COMMUNICATION.BASIC_RELIABLE_MULTICAST,
					TYPE_GROUP.STATIC,
					TYPE_MESSAGEORDERING.FIFO);
	}
	
	@Test
	public void testGetGroupName() {
		assertEquals("testGroup1", gdef_simple.getGroupName());
		assertEquals("testGroup2", gdef_custom.getGroupName());
	}
	
	@Test
	public void testGetCommunicationType() {
		assertEquals(TYPE_COMMUNICATION.BASIC_UNRELIABLE_MULTICAST, 
								gdef_simple.getCommunicationType());
		assertEquals(TYPE_COMMUNICATION.BASIC_RELIABLE_MULTICAST, 
				gdef_custom.getCommunicationType());
	}

	@Test
	public void testGetGroupType() {
		assertEquals(TYPE_GROUP.DYNAMIC, gdef_simple.getGroupType());
		assertEquals(TYPE_GROUP.STATIC, gdef_custom.getGroupType());
	}

	@Test
	public void testGetMessageOrderingType() {
		assertEquals(TYPE_MESSAGEORDERING.NONORDERED, gdef_simple.getMessageOrderingType());
		assertEquals(TYPE_MESSAGEORDERING.FIFO, gdef_custom.getMessageOrderingType());
	}

}
