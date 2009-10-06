/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gcom;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author emil
 */
public class Debug {
	public static final Level ALL = Level.ALL;
	public static final Level TRACE = Level.TRACE;
	public static final Level DEBUG = Level.DEBUG;
	public static final Level INFO = Level.INFO;
	public static final Level WARN = Level.WARN;
	public static final Level ERROR = Level.ERROR;
	public static final Level FATAL = Level.FATAL;
	public static final Level OFF = Level.OFF;

	public static void log(String name, Level level, String message) {
		Logger.getLogger(name).log(level, message);
	}

	public static void log(String name, Level level, String message, Throwable t){
		Logger.getLogger(name).log(level, message, t);
	}
}
