/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gcom.interfaces;

import java.util.Vector;

/**
 *
 * @author emil
 */
public interface DebugInterface extends MessageOrderingModule {
	// COM
	public void attachDebugger(MessageOrderingModule mom);
}
