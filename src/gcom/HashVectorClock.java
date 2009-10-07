package gcom;


import gcom.interfaces.*;

import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Implements the interface {@link VectorClock} with the use of a
 * {@link Hashtable} instead of a vector. This is to allow for
 * disappearing nodes.
 */
public class HashVectorClock implements VectorClock {
	private static Logger logger = Logger.getLogger("gcom.HashVectorClock");

	private Hashtable<Object, Integer> clocks;
	private Object key;

	public HashVectorClock(Object key) {
		clocks = new Hashtable<Object, Integer>();
		this.key = key;
		clocks.put(key, 0);
	}

	public HashVectorClock(Object key, Map<Object, Integer> m) {
		clocks = new Hashtable<Object, Integer>(m);
		this.key = key;
	}

	public void tick() {
		clocks.put(key, clocks.get(key)+1);
	}

	public void tickKey(Object k) {
		if(clocks.get(k) == null) {
			clocks.put(k, 1);
			return;
		}
		clocks.put(k, clocks.get(k)+1);
	}

	public void put(Object key, int value) {
		clocks.put(key, value);
	}

	public void merge(HashVectorClock o) {
		//logger.debug(String.format("Merging %s with %s", toString(), o.toString()));
		for(Object key : o.clocks.keySet()) {
			Integer my_value = clocks.get(key);
			Integer other_value = o.clocks.get(key);
			if(my_value == null) {
				clocks.put(key, other_value);
				continue;
			}
			if(my_value < other_value) {
				clocks.put(key, other_value);
			}
		}
	}

	public int compareTo(HashVectorClock o) {
		//logger.debug(String.format("Comparing %s with %s", toString(), o.toString()));
		int later = 0;
		int earlier = 0;
		for(Object key : clocks.keySet()){
			Integer otherClockValue = o.clocks.get(key);
			if(otherClockValue != null) { 
				switch(clocks.get(key).compareTo(otherClockValue)) {
					case -1: earlier = -1; break;
					case 1: later = 1; break;
				}
			}
		}
		return earlier + later;
	}

	public int excludedCompareTo(HashVectorClock o, Object excluded) {
		int later = 0;
		int earlier = 0;
		for(Object key : clocks.keySet()){
			Integer otherClockValue = o.clocks.get(key);
			if(otherClockValue != null && key != excluded) { 
				switch(clocks.get(key).compareTo(otherClockValue)) {
					case -1: earlier = -1; break;
					case 1: later = 1; break;
				}
			}
		}
		return earlier + later;
	}

	public int compareTo(VectorClock o) {
		throw(new RuntimeException("Method not implemented: compareTo(VectorClock)"));
	}

	public String toString() {
		return "VectorClock(" +this.key.toString() + ")"+ clocks.toString(); 
	}


	public Integer getValue(Object key) {
		return clocks.get(key);
	}

}
