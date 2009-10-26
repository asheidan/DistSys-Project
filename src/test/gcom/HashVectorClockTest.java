package test.gcom;
import static org.junit.Assert.*;

import gcom.HashVectorClock;

import java.util.Arrays;
import java.util.Hashtable;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class HashVectorClockTest {
	static Logger logger = Logger.getLogger("gcom.HashVectorClock.test"); 
	
	HashVectorClock clock_0,clock_1,clock_2,clock_3,clock_4;
	
	@Before
	public void setUp() throws Exception {
		BasicConfigurator.configure();

		clock_0 = new HashVectorClock("0");
		
		Hashtable<String, Integer> table_1 = new Hashtable<String, Integer>();
		Integer k = 0;
		for(Integer v : Arrays.asList(1,2,3,4,5,6,7,8,9)) {
			k++;
			table_1.put(String.valueOf(k),v);
		}
		clock_1 = new HashVectorClock("1",table_1);

		Hashtable<String, Integer> table_2 = new Hashtable<String, Integer>();
		k = 0;
		for(Integer v : Arrays.asList(1,2,3,4,5,7,7,8,9)) {
			k++;
			table_2.put(String.valueOf(k),v);
		}
		clock_2 = new HashVectorClock("2",table_2);
		
		Hashtable<String, Integer> table_3 = new Hashtable<String, Integer>();
		k = 0;
		for(Integer v : Arrays.asList(0,2,3,4,5,7,7,9,9)) {
			k++;
			table_3.put(String.valueOf(k),v);
		}
		clock_3 = new HashVectorClock("3",table_3);
		
		Hashtable<String, Integer> table_4 = new Hashtable<String, Integer>();
		k = 4;
		for(Integer v : Arrays.asList(5,6,7,8,9,0,1,2,3,4,5,6,7,8)) {
			k++;
			table_4.put(String.valueOf(k),v);
		}
		clock_4 = new HashVectorClock("4",table_4);
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
		assertEquals("VC(0){0=0}",clock_0.toString());
	}

	 @Test
	 public void testDifferentOrderOfKeys() {
		HashVectorClock clock1 = new HashVectorClock("0");
		HashVectorClock clock2 = new HashVectorClock("0");
		clock1.put("1", 5);
		clock1.put("2", 7);
		clock2.put("2", 7);
		clock2.put("1", 5);
		assertEquals(0, clock1.compareTo(clock2));
		assertEquals(0, clock2.compareTo(clock1));
	}

}
