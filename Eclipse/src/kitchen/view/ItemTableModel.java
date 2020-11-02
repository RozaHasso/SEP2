package kitchen.view;

import javax.swing.table.AbstractTableModel;

import domain.model.ItemList;

public class ItemTableModel extends AbstractTableModel {

   /**
    * 
    */
   private static final long serialVersionUID = 959109034313960373L;
   private String[] columnNames = { "ID", "Name", "Ingredients", "Allergens", "Category", "Price" };
   private ItemList menu;

   public ItemTableModel(ItemList menu) {
      this.menu = menu;
   }

   @Override
   public int getColumnCount() {
      return columnNames.length;
   }

   @Override
   public int getRowCount() {
      if (menu == null)
         return 0;
      else
         return menu.size();
   }

   @Override
   public Object getValueAt(int row, int col) {
      Object cell = null;

      switch (col) {
         case 0:
            cell = menu.get(row).getId();
            break;
         case 1:
            cell = menu.get(row).getName();
            break;
         case 2:
            cell = menu.get(row).getIngredients();
            break;
         case 3:
            cell = menu.get(row).getAllergens();
            break;
         case 4:
            cell = menu.get(row).getCategory();
            break;
         case 5:
            cell = menu.get(row).getPrice();
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
