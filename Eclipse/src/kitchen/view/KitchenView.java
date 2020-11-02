package kitchen.view;

import java.sql.SQLException;

import kitchen.KitchenController;

public interface KitchenView {
   void refreshMenu();
   void refreshOrderHistory();
   void refreshPendingOrders();
   void start(KitchenController controller) throws SQLException;
   void printError(String e);
}
