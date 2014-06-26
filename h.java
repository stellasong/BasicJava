import java.util.HashMap;

/**
 * 
 */

/**
 * @author Yucan
 *
 */
public class WareHouse {
	private String location;
	private HashMap<String, Integer> inventory;
	
	public WareHouse(String location) {
		this.location = location;
		inventory = new HashMap<String, Integer>();
	}
		
	public WareHouse(String location, HashMap<String, Integer> inventory) {
		this.location = location;
		this.inventory = inventory;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (location == null) ? 0 : location.hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WareHouse))
			return false;
		WareHouse other = (WareHouse) obj;
		if (location == null) {
			return other.location == null;
		} 
		return location.equals(other.location);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("WareHouse " + location);
		res.append("\n");
		for (String id : inventory.keySet()) {
			res.append(id + ": " + inventory.get(id) + "\n");
		}
		return res.toString();
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the inventory
	 */
	public HashMap<String, Integer> getInventory() {
		return inventory;
	}
	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(HashMap<String, Integer> inventory) {
		this.inventory = inventory;
	}
}
