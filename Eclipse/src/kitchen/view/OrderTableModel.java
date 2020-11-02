package kitchen.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;
import domain.model.OrderList;

public class OrderTableModel extends AbstractTableModel {

   /**
    * 
    */
   private static final long serialVersionUID = -4115990791379225877L;
   private String[] columnNames = { "Date", "Time", "Table number", "Number of items", "Total",
         "Status" };
   private OrderList orderList;

   public OrderTableModel(OrderList orderList) {
      this.orderList = orderList;
   }

   @Override
   public int getColumnCount() {
      return columnNames.length;
   }

   @Override
   public int getRowCount() {
      if (orderList == null)
         return 0;
      else
         return orderList.size();
   }

   @Override
   public Object getValueAt(int row, int col) {
      Object cell = null;
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
      Date dateTime = new Date(orderList.get(row).getTimestamp() * 1000L);

      switch (col) {
         case 0:
            cell = dateFormat.format(dateTime);
            break;
         case 1:
            cell = timeFormat.format(dateTime);
            break;
         case 2:
            cell = orderList.get(row).getTableNo();
            break;
         case 3:
            cell = orderList.get(row).getItems().size();
            break;
         case 4:
            cell = orderList.get(row).getTotal();
            break;
         case 5:
            cell = orderList.get(row).getStatus();
            break;
         default:
            cell = null;
      }
      return cell;
   }

   @Override
   public String getColumnName(int col) {
      return columnNames[col];
   }

}
