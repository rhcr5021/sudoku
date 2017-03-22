package simualted_anealing;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogAnealData {
	Logger logger;
	FileHandler fh; 
	SimpleFormatter formatter;
	public LogAnealData()
	{
		Logger logger = Logger.getLogger("MyLog");
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		try {
			//fh = new FileHandler("aneal_log_" + format.format(Calendar.getInstance().getTime()) + ".log");
			fh = new FileHandler("aneal_log.log",true);
			logger.addHandler(fh);
	        formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	public void logInit()
	{
		logger.info("Anealing Metadata: ");
	}
}
