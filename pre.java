import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class pre {
	private HashMap<String, HashMap<Integer, CostEntry>> minimumCostChart = 
			new HashMap<String, HashMap<Integer, CostEntry>>();
	
	private HashMap<String, HashMap<String, Integer>> inventory = 
			new HashMap<String, HashMap<String, Integer>>();
	
	private HashMap<String, HashMap<Integer, Integer>> saleSummary = 
			new HashMap<String, HashMap<Integer, Integer>>();
	
	private static final int FROM_INDEX = 0;
	private static final int TO_INDEX = 1;
	private static final int ITEM_ID_INDEX = 0;
	private static final int DATE_INDEX = 3;
	private static final int TIME_LIMIT_INDEX = 1;
	private static final int QUANTITY_INDEX = 2;
	private static final int MONTHS_IN_HISTORY = 4;
	private static final double GROWTH_FACTOR = 1.25;

	private static final String OUTPUT_FILE_PATH = "";
	
	public void selectMinimumCost(File costChart) throws FileNotFoundException {
		if (costChart == null) {
			throw new IllegalArgumentException("Cost chart file was not designated.");
		}
		try (FileReader fr = new FileReader(costChart);
			 BufferedReader input = new BufferedReader(fr)) {
			
			String line;
			while ((line = input.readLine()) != null) {
				String[] cells = line.split(" ");
				String from = cells[FROM_INDEX];
				String dest = cells[TO_INDEX];
				if (!minimumCostChart.containsKey(dest)) {
					HashMap<Integer, CostEntry> costs = new HashMap<Integer, CostEntry>();
					minimumCostChart.put(dest, costs);
				}
				HashMap<Integer, CostEntry> costs = minimumCostChart.get(dest);
				int timeLimit = 1;
				for (int i = TO_INDEX + 1; i < cells.length; i++) {
					int cost = Integer.parseInt(cells[i]);
					if (!costs.containsKey(timeLimit) || costs.get(timeLimit).cost > cost) {
						CostEntry entry = new CostEntry(from, cost);
						costs.put(timeLimit, entry);
					}
					timeLimit++;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sumUpFromHistory(File history) {
		if (history == null) {
			throw new IllegalArgumentException("History file was not designated.");
		}
		try (FileReader fr = new FileReader(history); 
			 BufferedReader input = new BufferedReader(fr)) {
			
			String line;
			while ((line = input.readLine()) != null) {
				String[] tokens = line.split("\t");
				String dest = tokens[TO_INDEX];//make sure this is correct
				int quantity = Integer.parseInt(tokens[QUANTITY_INDEX]);
				int timeLimit = Integer.parseInt(tokens[TIME_LIMIT_INDEX]);
				int month = getMonth(tokens[DATE_INDEX]);
				String itemId = tokens[ITEM_ID_INDEX];
				
				String wareHouse = minimumCostChart.get(dest).get(timeLimit).from;
				
				if (inventory.containsKey(wareHouse)) {
					HashMap<String, Integer> itemEntry = inventory.get(wareHouse);
					if (itemEntry.containsKey(itemId)) {
						itemEntry.put(itemId, itemEntry.get(itemId) + quantity);
					} else {
						itemEntry.put(itemId, quantity);
					}
				} else {
					HashMap<String, Integer> itemEntry = new HashMap<String, Integer>();
					itemEntry.put(itemId, quantity);
					inventory.put(wareHouse, itemEntry);
				}
				
				if (saleSummary.containsKey(itemId)) {
					HashMap<Integer, Integer> monthRecord = saleSummary.get(itemId);
					if (monthRecord.containsKey(month)) {
						monthRecord.put(month, monthRecord.get(month) + quantity);
					} else {
						monthRecord.put(month,quantity);
					}
				} else {
					HashMap<Integer, Integer> monthRecord = new HashMap<Integer, Integer>();
					monthRecord.put(month, quantity);
					saleSummary.put(itemId, monthRecord);
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int getMonth(String date) {
		assert(date != null && date.length() > 0);
		String[] tokens = date.split("/");
		int res = Integer.parseInt(tokens[0]);
		assert res > 0 && res <= 12;
		return res;
	}
	public void forecastByAveraging() {
		for (HashMap<String, Integer> wareHouse : inventory.values()) {
			for (String item : wareHouse.keySet()) {
				int futureStock = (int) (wareHouse.get(item) / MONTHS_IN_HISTORY * GROWTH_FACTOR);
				wareHouse.put(item, futureStock);
			}
		}
	}
	public void forecastByTimeSeries() {
		ForeCastWithR test = new ForeCastWithR(saleSummary);
		String rDataPath = test.forecast();
		if (rDataPath == null || rDataPath.length() == 0) {
			System.err.print("Failed forecasting with Time Series analysis");
			return;
		}
		try (FileReader fr = new FileReader(rDataPath);
			 BufferedReader rData = new BufferedReader(fr)) {
			
			String line;
			while ((line = rData.readLine()) != null) {
				String[] tokens = line.split("/t");
				String item = tokens[0];
				int predicted = Integer.parseInt(tokens[1]);
				int total = 0;
				for (int quantityPerMonth : saleSummary.get(total).values()) {
					total += quantityPerMonth;
				}
				for (String wareHouse : inventory.keySet()) {
					int sum = inventory.get(wareHouse).get(item);
					double percent = sum / total;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String outputResult() {
		
		return OUTPUT_FILE_PATH;
	}
	
	private static class CostEntry {
		String from;
		int cost;
		CostEntry(String from, int cost) {
			this.from = from;
			this.cost = cost;
		}
	}
}
