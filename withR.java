import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.rosuda.JRI.Rengine;


public class ForeCastWithR {
	private HashMap<String, HashMap<Integer, Integer>> saleSummary;
	private static final String TRAINING_DATA_PATH = "C:/workspace/RjavaTest/trainingData.txt";
	private static final String R_DATA_PATH = "C:/workspace/RjavaTest/rData.txt";
	private static final String R_SCRIPT_PATH = "C:/workspace/RjavaTest/src/Forecast.R";
	
	public ForeCastWithR(HashMap<String, HashMap<Integer, Integer>> saleSummary) {
		this.saleSummary = saleSummary;
	}
	private void writeTrainingDataToFile() {
		try ( FileWriter fw = new FileWriter(TRAINING_DATA_PATH);
			  BufferedWriter out = new BufferedWriter(fw)) {
			
			out.write("ID\tMonth\tSold");
			out.newLine();
			StringBuilder entry;
			for (String id : saleSummary.keySet()) {
				HashMap<Integer, Integer> record = saleSummary.get(id);
				for (Integer time : record.keySet()) {
					entry = new StringBuilder();
					entry.append(id + "\t" + time + "\t" + record.get(time));
					out.write(entry.toString());
					out.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String forecast() {
		writeTrainingDataToFile();
		
		String[] rargs = {"--vanilla"};
		Rengine re = new Rengine(rargs, false, null);
		if (!re.waitForR()) {
			System.err.println("Cannot load R");
			return null;
		}
		
		re.assign("trainingDataPath", TRAINING_DATA_PATH);
		re.assign("rDataPath", R_DATA_PATH);
		re.eval("source('" + R_SCRIPT_PATH + "');");
		re.end();
		return R_DATA_PATH;
	}
}
