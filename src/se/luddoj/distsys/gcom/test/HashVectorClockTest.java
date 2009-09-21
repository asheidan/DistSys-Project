package se.luddoj.distsys.gcom.test;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.luddoj.distsys.gcom.HashVectorClock;

public class HashVectorClockTest {
	HashVectorClock clock_1;
	HashVectorClock clock_2;
	
	@Before
	public void setUp() throws Exception {
		clock_1 = new HashVectorClock(null);
		clock_2 = new HashVectorClock(null);
	}

	@Test
	public void testCompareToHashVectorClock() {
		fail("Not yet implemented");
	}

}
