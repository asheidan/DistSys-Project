package se.luddoj.distsys.gcom.test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Hashtable;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import se.luddoj.distsys.gcom.HashVectorClock;

public class HashVectorClockTest {
	static Logger logger = Logger.getLogger("gcom.HashVectorClock.test"); 
	
	HashVectorClock clock_empty;
	HashVectorClock clock_1;
	HashVectorClock clock_2;
	HashVectorClock clock_3;
	
	@Before
	public void setUp() throws Exception {
		BasicConfigurator.configure();
		clock_empty = new HashVectorClock();

		Hashtable<Object, Integer> table_1 = new Hashtable<Object, Integer>();
		Integer k = 0;
		for(Integer v : Arrays.asList(1,2,3,4,5,6,7,8,9)) {
			k++;
			table_1.put(k,v);
		}
		clock_1 = new HashVectorClock(table_1);

		Hashtable<Object, Integer> table_2 = new Hashtable<Object, Integer>();
		k = 0;
		for(Integer v : Arrays.asList(1,2,3,4,5,7,7,8,9)) {
			k++;
			table_2.put(k,v);
		}
		clock_2 = new HashVectorClock(table_2);
		
		Hashtable<Object, Integer> table_3 = new Hashtable<Object, Integer>();
		k = 0;
		for(Integer v : Arrays.asList(0,2,3,4,5,7,7,9,9)) {
			k++;
			table_3.put(k,v);
		}
		clock_3 = new HashVectorClock(table_3);
	}

	@Test
	public void testCompareToHashVectorClock() {
		assertEquals(
				"Clock should be concurrent with itself",
				0, clock_1.compareTo(clock_1));
		assertEquals("Clock should be concurrent with empty clock", 0, clock_1.compareTo(clock_empty));
		assertEquals("Empty clock should be concurrent with any clock", 0, clock_empty.compareTo(clock_1));
		assertEquals("Empty clock should be concurrent with any clock", 0, clock_empty.compareTo(clock_2));
		
		assertEquals("Early clock should be before late clock", -1, clock_1.compareTo(clock_2));
		assertEquals("Late clock should be after early clock", 1, clock_2.compareTo(clock_1));
		
		assertEquals("Concurrent clock should be concurrent", 0, clock_3.compareTo(clock_1));
	}

	@Test
	public void testMerge() {
		clock_1.merge(clock_3);
		assertEquals(-1, clock_2.compareTo(clock_1));
	}
}
