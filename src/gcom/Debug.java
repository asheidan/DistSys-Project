/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gcom;

import org.apache.log4j.BasicConfigurator;
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

	private static Logger root = null;
	//private static LevelMatchFilter filter = new LevelMatchFilter();

	private static void init() {
		if(root == null) {
			root = Logger.getRootLogger();
			BasicConfigurator.configure();
		}
	}

	private static String objName(Object obj) {
		return obj.getClass().getName();
	}

	public static void setLevel(Level level) {
		init();
		root.setLevel(level);
	}

	public static void log(String name, Level level, String message) {
		init();
		Logger.getLogger(name).log(level, message);
	}

	public static void log(Object obj, Level level, String message) {
		log(objName(obj), level, message);
	}

	public static void log(String name, Level level, String message, Throwable t){
		init();
		Logger.getLogger(name).log(level, message, t);
	}

	public static void log(Object obj, Level level, String message, Throwable t){
		log(objName(obj),level, message, t);
	}
}
