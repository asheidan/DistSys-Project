package gcom;


import gcom.interfaces.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

/**
 * Implements the interface {@link VectorClock} with the use of a
 * {@link Hashtable} instead of a vector. This is to allow for
 * disappearing nodes.
 */
public class HashVectorClock implements VectorClock, Serializable,Cloneable {
    private static final long serialVersionUID = -5166402897945275632L;

	private Hashtable<String, Integer> clocks;
	private String key;

	public HashVectorClock(String key) {
		clocks = new Hashtable<String, Integer>();
		this.key = key;
		clocks.put(key, 0);
	}

	public HashVectorClock(String key, Map<String, Integer> m) {
		clocks = new Hashtable<String, Integer>(m);
		this.key = key;
	}

	public void tick() {
		clocks.put(key, clocks.get(key)+1);
	}

	public void tickKey(String k) {
		if(clocks.get(k) == null) {
			clocks.put(k, 1);
			return;
		}
		clocks.put(k, clocks.get(k)+1);
	}

	public void put(String key, int value) {
		clocks.put(key, value);
	}

	public void merge(HashVectorClock o) {
		Debug.log(this, Debug.DEBUG, String.format("Merging %s with %s", toString(), o.toString()));
		for(String k : o.clocks.keySet()) {
			Integer my_value = clocks.get(k);
			Integer other_value = o.clocks.get(k);
			if(my_value == null) {
				clocks.put(k, other_value);
				continue;
			}
			if(my_value < other_value) {
				clocks.put(k, other_value);
			}
		}
	}

	public int compareTo(HashVectorClock o) {
		Debug.log(this, Debug.DEBUG, String.format("Comparing %s with %s", toString(), o.toString()));
		int later = 0;
		int earlier = 0;
		for(String k : clocks.keySet()){
			Integer otherClockValue = o.clocks.get(k);
			if(otherClockValue != null) { 
				switch(clocks.get(k).compareTo(otherClockValue)) {
					case -1: earlier = -1; break;
					case 1: later = 1; break;
				}
			}
		}
		Debug.log(this, Debug.DEBUG, "Result: " + earlier + " " + later);
		return earlier + later;
	}

	public int excludedCompareTo(HashVectorClock o, Object excluded) {
		Debug.log(this, Debug.DEBUG, String.format("Comparing e %s with %s", toString(), o.toString()));
		int later = 0;
		int earlier = 0;
		for(String k : clocks.keySet()){
			Integer otherClockValue = o.clocks.get(k);
			Debug.log(this, Debug.TRACE, "otherclockval: " + otherClockValue + " key: " + k);
			if(otherClockValue != null && !String.valueOf(k).equals(String.valueOf(excluded))) {
				Debug.log(this, Debug.TRACE, "clockval: " + clocks.get(k) + " cmp: " + clocks.get(k).compareTo(otherClockValue));
				switch(clocks.get(k).compareTo(otherClockValue)) {
					case -1: earlier = -1; break;
					case 1: later = 1; break;
				}
			}
		}
		Debug.log(this, Debug.DEBUG, "Result: " + earlier + " " + later);
		return earlier + later;
	}

	@Override
	public int compareTo(VectorClock o) {
		throw(new RuntimeException("Method not implemented: compareTo(VectorClock)"));
	}

	@Override
	public String toString() {
		return "VC(" +this.key.toString() + ")"+ clocks.toString();
	}

	public String prettyPrint() {
		String result = "";
		Vector<String> keys = new Vector<String>(clocks.keySet());
		Collections.sort(keys);
		for(String k : keys) {
			result = result.concat(String.format("%s:%s\n", k, clocks.get(k)));
		}
		return result;
	}

	@Override
	public int hashCode() {
		return clocks.hashCode();
	}

	public Integer getValue(String key) {
		return clocks.get(key);
	}

	@Override
	public HashVectorClock clone() {
		return new HashVectorClock(key,clocks);
	}
}
