package kitchen;

import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import domain.mediator.ModelInterface;
import domain.mediator.ModelManager;
import domain.model.Item;
import domain.model.ItemList;
import domain.model.Order;
import domain.model.OrderList;
import kitchen.view.KitchenGUI;
import kitchen.view.KitchenView;

public class KitchenController {

	private ModelInterface model;
	private KitchenView view;
	private Server server;

	public KitchenController(ModelInterface model, KitchenView view) throws RemoteException, MalformedURLException {
		this.model = model;
		this.view = view;
		server = new Server(this);
	}

	public void addOrder(Order order) {
		try {
			model.addOrder(order);
		} catch (SQLException e) {
			System.exit(1);
		}
	}

	public void addItem(Item item) {
		try {
			model.addNewItem(item);
		} catch (SQLException e) {
			System.exit(1);
		}
	}

	public void editItem(Item oldItem, Item newItem) {
		try {
			model.editItem(oldItem, newItem);
		} catch (SQLException e) {
			System.exit(1);
		}
	}

	public void removeItem(Item item) {
		try {
			model.removeItem(item);
		} catch (SQLException e) {
			System.exit(1);
		}
	}

	public void finishOrder(Order order) {
		try {
			model.finishOrder(order);
		} catch (SQLException e) {
			System.exit(1);
		}

		try {
			server.finishOrder(order);
		} catch (Exception e) {
			view.printError(e.getMessage());
		}

	}

	public void cancelOrder(Order order) {
		try {
			model.cancelOrder(order);
		} catch (Exception e) {
			view.printError(e.getMessage());
		}
		server.cancelOrder(order);
	}

	public String[] getCategories() {
		return model.getCategories();
	}

	public ItemList getAllItems() {
		return model.getAllItems();
	}

	public void refreshActiveOrders() {
		view.refreshPendingOrders();
	}

	public OrderList getActiveOrders() {
		return model.getActiveOrders();
	}

	public OrderList getOrderHistory() throws SQLException {
		return model.getOrderHistory();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				KitchenView view = new KitchenGUI();
				try {
					KitchenSingleton.getInstance().prepareData(args);

					ModelInterface model = new ModelManager();
					KitchenController controller = new KitchenController(model, view);
					view.start(controller);
				} catch (Exception e) {
					view.printError(e.toString());
					System.exit(1);
				}
			}
		});
	}

}
