package bgu.spl.mics.application.passiveObjects;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary implements Serializable {
	private static class SingeltonHolder {
		private static Diary theDiary = new Diary();
	}

	private List<Report> reports;
	private AtomicInteger total;

	private Diary(){
		reports=new LinkedList<Report>();
		total=new AtomicInteger(0);
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return SingeltonHolder.theDiary;
	}

	public List<Report> getReports(){
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		while(!total.compareAndSet(total.intValue(),total.intValue()+1));
		if(reportToAdd!=null)
			reports.add(reportToAdd);

	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().create();
		String DiaryString =gson.toJson(Diary.getInstance());
		try(FileWriter writer=new FileWriter(filename)){
			writer.write(DiaryString);
		}
		catch (Exception e){e.printStackTrace();}
	}


	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return total.intValue();
	}
}