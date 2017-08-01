package ch.blutch.battlephoto.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JUtil {

	private static Logger rootLogger;
	
	public static Logger getLogger() {
		if (rootLogger == null) {
			setUp();
		}
		
		return rootLogger;
	}
	
	private static void setUp() {
		rootLogger = LogManager.getRootLogger();
	}
	
}
