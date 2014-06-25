import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class DataOrganizer {
	private static final int ITEM_ID_INDEX = 0;
	private static final int TIME_INDEX = 1;
	private static final int QUANTITY_INDEX = 2;
	public String summarize(File input, LinkedHashMap<String, LinkedHashMap<String, Integer>> hash) {
		String header = "";
		try ( FileReader reader = new FileReader(input);
			  BufferedReader in = new BufferedReader(reader)) {
			
			String record = in.readLine(); // IOException
			if (record == null) {
				System.err.format("%s is empty", input.getPath());
			}
			header = record;
			record = in.readLine();
			LinkedHashMap<String, Integer> itemRecord;
			while (record != null) {
				String[] tokens = record.split("\t");
				tokens[TIME_INDEX] = convertTime(tokens[TIME_INDEX]);
				
				if (hash.containsKey(tokens[ITEM_ID_INDEX])) {
					itemRecord = hash.get(tokens[ITEM_ID_INDEX]);
					if (itemRecord.containsKey(tokens[TIME_INDEX])) {
						itemRecord.put(tokens[TIME_INDEX], 
								itemRecord.get(tokens[TIME_INDEX]) 
								+ Integer.parseInt(tokens[QUANTITY_INDEX]));
					} else {
						itemRecord.put(tokens[TIME_INDEX], 
								Integer.parseInt(tokens[QUANTITY_INDEX]));
					}
				} else {
					itemRecord = new LinkedHashMap<String, Integer>();
					itemRecord.put(tokens[TIME_INDEX], 
							Integer.parseInt(tokens[QUANTITY_INDEX]));
					hash.put(tokens[ITEM_ID_INDEX], itemRecord);
				}
				record = in.readLine();
			}
			
		} catch (IOException e) {
			//System.err.print("IO exception due to : " + e.getMessage());
			e.printStackTrace();
		}
		return header;
	}
	public void writeTrainingDataToFile(File output, LinkedHashMap<String, LinkedHashMap<String, Integer>> hash) {
		assert(hash != null && hash.size() > 0);
		
		try ( FileWriter fw = new FileWriter(output);
			  BufferedWriter out = new BufferedWriter(fw)) {
			
			out.write("ID\tDate\tSold");
			out.newLine();
			StringBuilder entry;
			for (String id : hash.keySet()) {
				LinkedHashMap<String, Integer> record = hash.get(id);
				for (String time : record.keySet()) {
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
	private String convertTime(String original) {
		assert original != null && original.length() > 0 : original;
		String[] tokens = original.split("/");
		return tokens[2] + "-" + tokens[0];
	}

	public void output(File output, String[] items, String[] predictedValues) {
		assert(items != null && predictedValues != null 
				&& items.length == predictedValues.length);
		
		try ( FileWriter fw = new FileWriter(output);
			  BufferedWriter out = new BufferedWriter(fw)) {
			
			for (int i = 0; i < items.length; i++) {
				out.write(items[i] + "\t" + predictedValues[i]);
				out.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] average(LinkedHashMap<String, LinkedHashMap<String, Integer>> hash) {
		assert(hash != null && hash.size() > 0);
		String[] res = new String[hash.size()];
		int i = 0;
		for (String id : hash.keySet()) {
			int average = 0;
			int months = 0;
			for (int sold : hash.get(id).values()) {
				average += sold;
				months ++;
			}
			res[i] = String.valueOf((double) average / (double) months);
			i++;
		}
		return res;
	}
}
