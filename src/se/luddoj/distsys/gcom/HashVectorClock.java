package se.luddoj.distsys.gcom;

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
	
	public HashVectorClock(Map<Object, Integer> m) {
		clocks = new Hashtable<Object, Integer>(m);
	}
	
	public int compareTo(HashVectorClock o) {
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

	@Override
	public int compareTo(VectorClock o) {
		logger.error("This method isn't implemented");
		// TODO Auto-generated method stub
		return 0;
	}

}
