import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;


public class JavaRInterface {
	//private void forecastAll(HashMap<String, HashMap<String, Integer>> hash) {
	private static final String TRAINING_DATA_PATH = "C:/workspace/RjavaTest/trainingData.txt";
	private static final String INPUT_DATA_PATH = "C:/workspace/RjavaTest/input.txt";
	private static final String OUTPUT_DATA_PATH = "C:/workspace/RjavaTest/output.txt";
	private static final String R_DATA_PATH = "C:/workspace/RjavaTest/rData.txt";
	private static final String R_SCRIPT_PATH = "C:/workspace/RjavaTest/src/Forecast.R";
	
	public static void main(String[] args) {
		//assert hash != null && hash.size() > 0 : hash;
		//Collection<HashMap<String, Integer>> records = hash.values();
		DataOrganizer t = new DataOrganizer();
		File inputData = new File(INPUT_DATA_PATH);
		LinkedHashMap<String, LinkedHashMap<String, Integer>> hash 
			= new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		t.summarize(inputData, hash);
		
		File trainingData = new File(TRAINING_DATA_PATH);
		t.writeTrainingDataToFile(trainingData, hash);
		
		String[] rargs = {"--vanilla"};
		Rengine re = new Rengine(rargs, false, null);
		if (!re.waitForR()) {
			System.err.println("Cannot load R");
			return;
		}
		
		//re.assign("trainingDataPath", trainingData.getAbsolutePath().replaceAll("\\", "/"));
		re.assign("trainingDataPath", TRAINING_DATA_PATH);
		
		//File rData = new File(R_DATA_PATH);
		//re.eval(String.format("rDataPath <- %s", rData.getAbsolutePath()));
		//re.assign("rDataPath", rData.getAbsolutePath());
		re.assign("rDataPath", R_DATA_PATH);
		re.eval("source('" + R_SCRIPT_PATH + "');");
		re.end();

		File outputData = new File(OUTPUT_DATA_PATH);
		
	}
}
