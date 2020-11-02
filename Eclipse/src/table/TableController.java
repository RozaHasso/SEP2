package table;

import java.awt.EventQueue;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import domain.model.Item;
import domain.model.ItemList;
import domain.model.Order;
import kitchen.KitchenRemote;
import table.view.TableGUI;
import table.view.TableView;

public class TableController extends UnicastRemoteObject implements TableInterface, Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 3568025702269200249L;

	private KitchenRemote server;
	private TableView view;
	private Order order;
	private TableInterface client;

	public TableController(KitchenRemote server, TableView view) throws RemoteException {
		order = new Order(TableSingleton.getInstance().getTableNo());
		this.server = server;
		this.view = view;
		this.client = client;
	}

	public void setClient(TableInterface client) {
		this.client = client;
	}

	public void finishOrder() {
		view.finishOrder(null);
		order.clear();
	}

	public void cancelOrder(String cancelNote) {
		view.finishOrder(cancelNote);
		order.clear();
	}

	public void sendOrder(String customerNote) {
		order.setCustomerNote(customerNote);
		order.setTimestamp(System.currentTimeMillis() / 1000L);
		try {
			server.addOrder(order, this);
		} catch (RemoteException e) {
			System.exit(1);
		}
	}

	public void addItem(Item item) {
		order.addItem(item);
	}

	public void removeItem(int item) {
		order.removeItem(item);
	}

	public ItemList refreshMenu() {
		ItemList menu;
		try {
			menu = server.getMenu();
		} catch (RemoteException e) {
			menu = null;
			System.exit(1);
		}
		return menu;
	}

	public String[] refreshCategories() {
		String[] categories;
		try {
			categories = server.getCategories();
		} catch (RemoteException e) {
			categories = null;
			System.exit(1);
		}
		return categories;
	}

	public static void main(String[] args) {
		TableSingleton.getInstance().prepareData(args);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				TableView view = new TableGUI();
				try {

					KitchenRemote server = (KitchenRemote) Naming.lookup("rmi://" + TableSingleton.getInstance().getIP()
							+ ":" + TableSingleton.getInstance().getPort() + "/KitchenServer");
					TableController controller = new TableController(server, view);
					TableInterface client = new Client(controller);
					controller.setClient(client);
					view.start(controller);
					controller.refreshMenu();
					controller.refreshCategories();
				} catch (Exception e) {
					view.printError(e.toString());
					System.exit(1);
				}
			}
		});
	}
}
