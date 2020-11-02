package table;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements TableInterface {

   /**
    * 
    */
   private static final long serialVersionUID = 1374135131880050989L;
   private TableController controller;

   public Client(TableController controller) throws RemoteException {
      this.controller = controller;
   }

   @Override
   public void finishOrder() {
      controller.finishOrder();
   }

   @Override
   public void cancelOrder(String cancelNote) {
      controller.cancelOrder(cancelNote);
   }

}
