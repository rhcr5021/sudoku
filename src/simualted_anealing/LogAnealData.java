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
		logger = Logger.getLogger("MyLog");
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
	
	public void logInit(double a, double min_t, int temp_change, double k, double t)
	{
		logger.info("Anealing Parameters: " + "\na: " + a + "\nmin_t: " + min_t + "\ntemp_change: " + temp_change + "\nk: " + k + "\nt: " + t);
	}
	
	public void log_results(int[] data, int err, boolean valid, int correct, boolean sol, double dur)
	{
		logger.info("Anealing Results: " + "\nMinimum Number of Errors: " + data[0] + "\nNumber of Loop Iterations: " + data[1] + "\nErrors in Puzzle: " + err + "\nPuzzle is Valid: " + valid + "\nCorrect Indices: " + correct + "\nis Solution?: " + sol + "\nDuration: " + dur);
	}
}

