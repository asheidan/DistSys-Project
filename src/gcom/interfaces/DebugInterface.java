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
public interface DebugInterface extends CommunicationModule {
	public void setQueue(Vector queue);

	public void attachCom(CommunicationModule com);
}
