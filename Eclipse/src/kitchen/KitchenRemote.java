package kitchen;

import java.rmi.Remote;
import java.rmi.RemoteException;

import domain.model.ItemList;
import domain.model.Order;
import table.TableInterface;

public interface KitchenRemote extends Remote {

	/**
	 * Calls the addOrder method from the controller that will add the order to the
	 * database and to pending orders and saves table in an array of tables so we
	 * can call finishOrder or cancelOrder later on that table.
	 * 
	 * @param order
	 *            the order to be added in the database and to pending orders
	 * @param table
	 *            the table that called the method
	 * @throws RemoteException
	 */
	void addOrder(Order order, TableInterface table) throws RemoteException;

	/**
	 * The method returns a list with all categories in the database.
	 * 
	 * @return a string array with categories.
	 * @throws RemoteException
	 */

	String[] getCategories() throws RemoteException;

	/**
	 * Returns an ItemList with all the items in the database.
	 * 
	 * @return ItemList containing all the items in the database.
	 * @throws RemoteException
	 */

	ItemList getMenu() throws RemoteException;
}
