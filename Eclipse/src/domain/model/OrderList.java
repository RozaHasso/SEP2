package domain.model;

import java.util.ArrayList;

public class OrderList implements java.io.Serializable {

	/**
    * 
    */
   private static final long serialVersionUID = 8765797200564113431L;
   private ArrayList<Order> orders;
	
	public OrderList()
	{
	   orders = new ArrayList<Order>();
	}

	public void add(Order order) {
	   orders.add(order);
	}

	public void remove(Order order) {
	   orders.remove(order);
	}

	public Order get(int index) {
		return orders.get(index);
	}

	public int size() {
		return orders.size();
	}

	
  


}
