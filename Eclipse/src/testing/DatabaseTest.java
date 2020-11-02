package testing;


import java.sql.SQLException;

import org.junit.Test;

import domain.mediator.DataPersistence;
import domain.mediator.DatabaseAdapter;
import domain.model.Item;
import domain.model.ItemList;
import domain.model.Order;
import domain.model.OrderList;

class DatabaseTest {

	@Test
	void loadItems() {
		DataPersistence data;
      try {
         data = new DatabaseAdapter();
         ItemList items = data.loadItems();
         System.out.println(items);
      }
      catch (ClassNotFoundException | SQLException e) {
         e.printStackTrace();
      }
	}

	// @Test
	void testAddItem() {
		DataPersistence data;
      try {
         data = new DatabaseAdapter();
         Item item = new Item("item name", "ingredients", "allergens", "Pizza", 120);
         data.addItem(item);
      }
      catch (ClassNotFoundException | SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
	}

	// @Test
	void testLoadCategories() {
		DataPersistence data;
      try {
         data = new DatabaseAdapter();
         String[] categories = data.getCategories();
         System.out.println(categories[0]);
      }
      catch (ClassNotFoundException | SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
	}

	// @Test
	void insertOrder() {
		DataPersistence data;
      try {
         data = new DatabaseAdapter();
         data.addOrder(new Order(1111, 11, "customer note", "cancel note", "pending"));
      }
      catch (ClassNotFoundException | SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
	}

	// @Test
	void getPendingOrdes() {
		DataPersistence data;
      try {
         data = new DatabaseAdapter();
         OrderList orders = data.loadPendingOrders();
         System.out.println(orders.get(0));
      }
      catch (ClassNotFoundException | SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
	}

}
