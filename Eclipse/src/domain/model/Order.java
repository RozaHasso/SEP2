package domain.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

	/**
    * 
    */
   private static final long serialVersionUID = -1260305735650128857L;

   private long timestamp;
	
   private int tableNo;
	private String customerNote;
	private ArrayList<Item> items;
	private String cancelNote;
	private String status;

	public Order(long timestamp, int tableNo, String customerNote, String cancelNote, String status) {
		this.timestamp = timestamp;
		this.tableNo = tableNo;
		this.customerNote = customerNote;
		this.cancelNote = cancelNote;
		items = new ArrayList<Item>();
		this.status = status;
	}

	public Order(int tableNo) {
		this.timestamp = 0;
		this.tableNo = tableNo;
		this.customerNote = "";
		items = new ArrayList<Item>();
		cancelNote = "";
		status = "pending";
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	public void removeItem(int index) {
		items.remove(index);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }

	public int getTableNo() {
		return tableNo;
	}

	public String getCustomerNote() {
		return customerNote;
	}

	public void setCustomerNote(String customerNote) {
		this.customerNote = customerNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public double getTotal() {
		double total = 0;
		if (items.size() != 0)
			for (Item item : items)
				total += item.getPrice();
		return total;
	}
	
	public void clear() {
	   items.clear();
	}

	public String toString() {
		return timestamp + " " + tableNo + " " + status;
	}

}
