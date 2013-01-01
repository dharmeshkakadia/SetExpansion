package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import concept.Runner;

public class LogUtil {
	public final static Logger log = Logger.getLogger(Runner.class .getName()); 

	static {
		log.setLevel(Level.FINER);
		FileHandler temp = null;
		try {
			temp = new FileHandler("SetExpansion.log");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		temp.setFormatter(new SimpleFormatter());
		log.addHandler(temp);
	}
	
	public void writeLog(){
		
	}
}
