/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gcom.interfaces;

import gcom.HashVectorClock;
import gcom.ReliableCommunicationModule;
import gcom.interfaces.Message;
import java.util.Vector;

/**
 *
 * @author emil
 */
public interface DebugInterface extends MessageOrderingModule {
	// COM
	public void attachDebugger(MessageOrderingModule mom);
	// RCOM
	public void attachDebugger(ReliableCommunicationModule rcom);
	public void receive(Message m);

	// MOM
	public void attachDebugger(HashVectorClock clock, Vector<Message>messages);
}
