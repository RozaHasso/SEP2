package kitchen;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import domain.model.ItemList;
import domain.model.Order;
import table.TableInterface;

public class Server extends UnicastRemoteObject implements KitchenRemote {

	/**
	* 
	*/
	private static final long serialVersionUID = 3963298567345112883L;

	private KitchenController controller;

	private TableInterface[] tableInterfaces;

	public Server(KitchenController controller) throws RemoteException, MalformedURLException {
		tableInterfaces = new TableInterface[KitchenSingleton.getInstance().getTables()];
		this.controller = controller;
		LocateRegistry.createRegistry(KitchenSingleton.getInstance().getPort());
		Naming.rebind("KitchenServer", (KitchenRemote) this);
	}

	@Override
	public void addOrder(Order order, TableInterface table) {
		controller.addOrder(order);
		controller.refreshActiveOrders();
		if (tableInterfaces[order.getTableNo()] == null)
			tableInterfaces[order.getTableNo()] = table;
	}

	@Override
	public String[] getCategories() {
		return controller.getCategories();
	}

	@Override
	public ItemList getMenu() throws RemoteException {
		return controller.getAllItems();
	}

	public void finishOrder(Order order) throws Exception {
		TableInterface table = tableInterfaces[order.getTableNo()];
		if (table == null) {
			throw new Exception("Table is disconnected.");
		}
		try {
			table.finishOrder();
		} catch (RemoteException e) {
			throw new Exception("Exception: Table of this order is not reachable");
		}
	}

	public void cancelOrder(Order order) throws Exception {
		TableInterface table = tableInterfaces[order.getTableNo()];
		if (table == null) {
			throw new Exception("Table is disconnected.");
		}
		try {
			table.cancelOrder(order.getCancelNote());
		} catch (RemoteException e) {
			throw new Exception("Exception: Table of this order is not reachable");
		}
	}

}
