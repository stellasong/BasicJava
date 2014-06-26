import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * MinimumDeliveryCostChart class is for selecting the 
 * @author Yucan
 *
 */
public class MinimumDeliveryCostChart {
	private static HashMap<String, HashMap<Integer, CostEntry>> chart;
	private static final MinimumDeliveryCostChart INSTANCE = new MinimumDeliveryCostChart();
	private static final int FROM_INDEX = 0;
	private static final int TO_INDEX = 1;
	private MinimumDeliveryCostChart() {
		chart = new HashMap<String, HashMap<Integer, CostEntry>>();
	};
	
	public static MinimumDeliveryCostChart getInstance() {
		return INSTANCE;
	}
	public HashMap<String, HashMap<Integer, CostEntry>> getChart() {
		return chart;
	}
	private boolean addDestination(String destination) {
		assert(destination != null);
		if (chart.containsKey(destination)) {
			return false;
		} else {
			HashMap<Integer, CostEntry> costs = new HashMap<Integer, CostEntry>();
			chart.put(destination, costs);
			return true;
		}
	}
	private void setCostEntry(String dest, int timeLimit, String fromLocation, int cost) {
		assert(chart.containsKey(dest));
		
		HashMap<Integer, CostEntry> costs = chart.get(dest);
		assert(costs != null);
		
		CostEntry entry = new CostEntry(fromLocation, cost);
		costs.put(timeLimit, entry);
	}
	public void importFromFile(File costChart) {
		if (costChart == null) {
			throw new IllegalArgumentException();
		}
		try (FileReader fr = new FileReader(costChart);
			 BufferedReader input = new BufferedReader(fr)) {
			
			String line;
			while ((line = input.readLine()) != null) {
				String[] cells = line.split(" ");
				String from = cells[FROM_INDEX];
				String dest = cells[TO_INDEX];
				if (!chart.containsKey(dest)) {
					addDestination(dest);
				}
				HashMap<Integer, CostEntry> costs = chart.get(dest);
				int timeLimit = 1;
				for (int i = TO_INDEX + 1; i < cells.length; i++) {
					int cost = Integer.parseInt(cells[i]);
					if (!costs.containsKey(timeLimit) || costs.get(timeLimit).cost > cost) {
						setCostEntry(dest, timeLimit, from, cost);
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
	public static class CostEntry {
		String from;
		int cost;
		CostEntry(String from, int cost) {
			this.from = from;
			this.cost = cost;
		}
	}
}
