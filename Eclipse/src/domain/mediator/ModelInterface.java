package domain.mediator;

import java.sql.SQLException;

import domain.model.Item;
import domain.model.ItemList;
import domain.model.Order;
import domain.model.OrderList;

public interface ModelInterface {
	public OrderList getActiveOrders();

	public OrderList getOrderHistory() throws SQLException;

	public ItemList getAllItems();

	public ItemList getItemByCategory(String category);

	public String[] getCategories();

	public void addOrder(Order order) throws SQLException;

	public void finishOrder(Order order) throws SQLException;

	public void cancelOrder(Order order) throws SQLException;

	public void addNewItem(Item item) throws SQLException;

	public void editItem(Item oldItem, Item newItem) throws SQLException;

	public void removeItem(Item item) throws SQLException;

}
