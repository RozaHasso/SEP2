package domain.mediator;

import java.sql.SQLException;

import domain.model.Item;
import domain.model.ItemList;
import domain.model.Order;
import domain.model.OrderList;

public interface DataPersistence {
	public ItemList loadItems() throws SQLException;

	public OrderList loadPendingOrders() throws SQLException;

	public OrderList loadOrdersHistory() throws SQLException;

	public void addOrder(Order order) throws SQLException;

	public void updateOrder(Order order) throws SQLException;

	public void addItem(Item item) throws SQLException;

	public void editItem(Item item) throws SQLException;

	public void removeItem(Item item) throws SQLException;

	public String[] getCategories() throws SQLException;
}
