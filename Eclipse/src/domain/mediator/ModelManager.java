package domain.mediator;

import java.io.Serializable;
import java.sql.SQLException;

import domain.model.Item;
import domain.model.ItemList;
import domain.model.Order;
import domain.model.OrderList;

public class ModelManager implements ModelInterface, Serializable {

	/**
    * 
    */
   private static final long serialVersionUID = -304895379516192094L;

   private ItemList allItems;

	private OrderList pendingOrders;

	private DataPersistence dataPersistence;

	private String[] categories;

	public ModelManager() throws ClassNotFoundException, SQLException {
		dataPersistence = new DatabaseAdapter();
		allItems = dataPersistence.loadItems();
		pendingOrders = dataPersistence.loadPendingOrders();
		categories = dataPersistence.getCategories();
	}

	@Override
	public OrderList getActiveOrders() {

		return pendingOrders;
	}

	@Override
	public OrderList getOrderHistory() throws SQLException {

		return dataPersistence.loadOrdersHistory();
	}

	@Override
	public ItemList getAllItems() {

		return allItems;
	}

	@Override
	public ItemList getItemByCategory(String category) {
		ItemList itemsByCategory = new ItemList();
		for (int i = 0; i < allItems.size(); i++) {
			if (allItems.get(i).getCategory().equals(category)) {
				itemsByCategory.add(allItems.get(i));
			}
		}
		return itemsByCategory;
	}

	@Override
	public String[] getCategories() {
		return categories;

	}

	@Override
	public void addOrder(Order order) throws SQLException {
		pendingOrders.add(order);
		dataPersistence.addOrder(order);

	}

	@Override
	public void finishOrder(Order order) throws SQLException {
		pendingOrders.remove(order);
		dataPersistence.updateOrder(order);
	}

	@Override
	public void cancelOrder(Order order) throws SQLException {
		pendingOrders.remove(order);
		dataPersistence.updateOrder(order);
	}

	@Override
	public void addNewItem(Item item) throws SQLException {
		dataPersistence.addItem(item);
		allItems.add(item);
	}

	@Override
	public void removeItem(Item item) throws SQLException {
		dataPersistence.removeItem(item);
		allItems.remove(item);
	}

	@Override
	public void editItem(Item oldItem, Item newItem) throws SQLException {
		dataPersistence.editItem(newItem);
		oldItem.setName(newItem.getName());
		oldItem.setIngredients(newItem.getIngredients());
		oldItem.setAllergens(newItem.getAllergens());
		oldItem.setCategory(newItem.getCategory());
		oldItem.setPrice(newItem.getPrice());
	}

}