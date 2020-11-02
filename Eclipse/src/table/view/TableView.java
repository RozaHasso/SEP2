package table.view;

import table.TableController;

public interface TableView {
   void loadMenu();
   void finishOrder(String cancelNote);
   void updateTotal();
   void start(TableController controller);
   void printError(String e);
}
