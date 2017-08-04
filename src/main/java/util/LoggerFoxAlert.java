package util;

import org.apache.log4j.Logger;

public class LoggerFoxAlert {
	
	private static LoggerFoxAlert instance;
	/* Get actual class name to be printed on */
	private static Logger log = Logger.getLogger(LoggerFoxAlert.class.getName());
	
	private LoggerFoxAlert() {
		
	}
	
	public static LoggerFoxAlert getLoggerInstance(){
		if (instance == null) {
			instance = new LoggerFoxAlert();
		}
		
		return instance;
	}
	
	public void logError(String error) {
		log.error(error);
	}
	
	public void logInfo(String info) {
		log.info(info);
	}

}
