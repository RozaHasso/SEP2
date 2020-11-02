package table;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TableInterface extends Remote {

	/**
	 * Calls finishOrder() from the TableController, that calls the necessary
	 * methods from view to display a JOptionPane to informer the customer that his
	 * order is finished, and also clears the order items. Is called remotely from
	 * Server when the order is finished.
	 * 
	 * @throws RemoteException
	 */
	void finishOrder() throws RemoteException;

	/**
	 * Calls cancelsOrder from TableController, that calls the necessary methods
	 * from view to display a JOptionPane to informer the customer that his order
	 * was canceled, and also clears the order items. Is called remotely from Server
	 * when the order is canceled.
	 * 
	 * @param cancelNote
	 *            Cancel note to be displayed to the customer.
	 * @throws RemoteException
	 */

	void cancelOrder(String cancelNote) throws RemoteException;

}
