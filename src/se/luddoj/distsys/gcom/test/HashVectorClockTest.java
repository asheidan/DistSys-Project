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
	
	HashVectorClock clock_0,clock_1,clock_2,clock_3,clock_4;
	
	@Before
	public void setUp() throws Exception {
		BasicConfigurator.configure();

		clock_0 = new HashVectorClock(0);
		
		Hashtable<Object, Integer> table_1 = new Hashtable<Object, Integer>();
		Integer k = 0;
		for(Integer v : Arrays.asList(1,2,3,4,5,6,7,8,9)) {
			k++;
			table_1.put(k,v);
		}
		clock_1 = new HashVectorClock(1,table_1);

		Hashtable<Object, Integer> table_2 = new Hashtable<Object, Integer>();
		k = 0;
		for(Integer v : Arrays.asList(1,2,3,4,5,7,7,8,9)) {
			k++;
			table_2.put(k,v);
		}
		clock_2 = new HashVectorClock(2,table_2);
		
		Hashtable<Object, Integer> table_3 = new Hashtable<Object, Integer>();
		k = 0;
		for(Integer v : Arrays.asList(0,2,3,4,5,7,7,9,9)) {
			k++;
			table_3.put(k,v);
		}
		clock_3 = new HashVectorClock(3,table_3);
		
		Hashtable<Object, Integer> table_4 = new Hashtable<Object, Integer>();
		k = 4;
		for(Integer v : Arrays.asList(5,6,7,8,9,0,1,2,3,4,5,6,7,8)) {
			k++;
			table_4.put(k,v);
		}
		clock_4 = new HashVectorClock(4,table_4);
	}

	@Test
	public void testCompareToHashVectorClock() {
		assertEquals(
				"Clock should be concurrent with itself",
				0, clock_1.compareTo(clock_1));
		
		assertEquals("Early clock should be before late clock", -1, clock_1.compareTo(clock_2));
		assertEquals("Late clock should be after early clock", 1, clock_2.compareTo(clock_1));
		
		assertEquals("Concurrent clock should be concurrent", 0, clock_3.compareTo(clock_1));
		
		assertEquals("Clock with different keys should be concurrent",0,clock_4.compareTo(clock_1));
		assertEquals("Clock with different keys should be concurrent",0,clock_1.compareTo(clock_4));
	}

	@Test
	public void testMerge() {
		clock_1.merge(clock_3);
		assertEquals(-1, clock_2.compareTo(clock_1));
	}
	
	public void testTick() {
		clock_0.tick();
		assertEquals("VectorClock(0){0=1}",clock_0.toString());
	}
	
	@Test
	public void testToString() {
		assertEquals("VectorClock(0){0=0}",clock_0.toString());
		assertEquals("VectorClock(1){9=9, 8=8, 7=7, 6=6, 5=5, 4=4, 3=3, 2=2, 1=1}", clock_1.toString());
	}
}
