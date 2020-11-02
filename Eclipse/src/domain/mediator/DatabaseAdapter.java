package domain.mediator;

import java.sql.SQLException;
import java.util.ArrayList;

import domain.model.Item;
import domain.model.ItemList;
import domain.model.Order;
import domain.model.OrderList;
import utility.persistence.MyDatabase;

public class DatabaseAdapter implements DataPersistence {
   private MyDatabase mydatabase;

   private static final String DRIVER = "org.postgresql.Driver";
   private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
   private static final String USER = "postgres";
   private static final String PASSWORD = "root";

   public DatabaseAdapter() throws ClassNotFoundException {
         this.mydatabase = new MyDatabase(DRIVER, URL, USER, PASSWORD);
   }

   @Override
   public ItemList loadItems() throws SQLException {
      String sql = "SELECT CATEGORY.CATEGORY_NAME, ITEMS.ITEM_ID, ITEMS.ITEM_NAME, ITEMS.INGREDIENTS, ITEMS.ALLERGENS, ITEMS.PRICE, ITEMS.CATEGORY_ID "
            + "FROM sep2.ITEMS, sep2.CATEGORY WHERE sep2.ITEMS.CATEGORY_ID = sep2.CATEGORY.CATEGORY_ID";
      ItemList allItems = null;
         ArrayList<Object[]> results = mydatabase.query(sql);
         allItems = new ItemList();
         for (int i = 0; i < results.size(); i++) {
            Item item = new Item(Integer.parseInt(results.get(i)[1].toString()), results.get(i)[2].toString(), results.get(i)[3].toString(), results.get(i)[4].toString(), results.get(i)[0].toString(),
                  Double.parseDouble(results.get(i)[5].toString()));
            allItems.add(item);

         }
      return allItems;
   }

   @Override
   public OrderList loadPendingOrders() throws SQLException {
      String sql1 = "SELECT ORDERS.TIME_STAMP, ORDERS.TABLE_NO, ORDERS.CUSTOMER_NOTE, ORDERS.CANCEL_NOTE, ORDERS.STATUS from sep2.orders where status='pending'";
      OrderList orderList = new OrderList();
         ArrayList<Object[]> results1 = mydatabase.query(sql1);
         for (int i = 0; i < results1.size(); i++) {
            Order order = new Order((long) results1.get(i)[0], (int) results1.get(i)[1], results1.get(i)[2].toString(), results1.get(i)[3].toString(), results1.get(i)[4].toString());
            ArrayList<Object[]> items = mydatabase.query(
                  "select item_name,ingredients,allergens,category_name,price from sep2.items,sep2.orders_items,sep2.category where category.category_id=items.category_id and items.item_id=orders_items.item_id and orders_items.time_stamp=? and orders_items.table_no=?",
                  order.getTimestamp(), order.getTableNo());
            for (int j = 0; j < items.size(); j++) {
               order.addItem(new Item(items.get(j)[0].toString(), items.get(j)[1].toString(), items.get(j)[2].toString(), items.get(j)[3].toString(), Double.parseDouble(items.get(j)[4].toString())));
            }
            orderList.add(order);
         }
      return orderList;
   }

   @Override
   public OrderList loadOrdersHistory() throws SQLException {
      String sql1 = "SELECT ORDERS.TIME_STAMP, ORDERS.TABLE_NO, ORDERS.CUSTOMER_NOTE, ORDERS.CANCEL_NOTE, ORDERS.STATUS from sep2.orders where status='cancelled' or status='finished'";
      OrderList orderList = new OrderList();
         ArrayList<Object[]> results1 = mydatabase.query(sql1);
         for (int i = 0; i < results1.size(); i++) {
            Order order = new Order((long) results1.get(i)[0], (int) results1.get(i)[1], results1.get(i)[2].toString(), results1.get(i)[3].toString(), results1.get(i)[4].toString());
            ArrayList<Object[]> items = mydatabase.query(
                  "select item_name,ingredients,allergens,category_name,price from sep2.items,sep2.orders_items,sep2.category where category.category_id=items.category_id and items.item_id=orders_items.item_id and orders_items.time_stamp=? and orders_items.table_no=?",
                  order.getTimestamp(), order.getTableNo());
            for (int j = 0; j < items.size(); j++) {
               order.addItem(new Item(items.get(j)[0].toString(), items.get(j)[1].toString(), items.get(j)[2].toString(), items.get(j)[3].toString(), Double.parseDouble(items.get(j)[4].toString())));
            }
            orderList.add(order);
         }
      return orderList;
   }

   @Override
   public void addOrder(Order order) throws SQLException {
         mydatabase.update("insert into sep2.orders(time_stamp,table_no,customer_note,cancel_note,status) values (?,?,?,?,?)", order.getTimestamp(), order.getTableNo(),
               order.getCustomerNote() == null ? "" : order.getCustomerNote(), order.getCancelNote() == null ? "" : order.getCancelNote(), order.getStatus());
         ArrayList<Item> items = order.getItems();
         
         for (int i = 0; i < items.size(); i++) {
            ArrayList<Object[]> idExisting = mydatabase.query("select item_name from sep2.items where item_id=?", items.get(i).getId());
            if (idExisting.size() > 0)
               mydatabase.update("insert into sep2.orders_items (time_stamp,table_no,item_id) values(?,?,?)", order.getTimestamp(), order.getTableNo(), items.get(i).getId());
         }
   }

   @Override
   public void updateOrder(Order order) throws SQLException {
         mydatabase.update("update sep2.orders set status=?,cancel_note=? where time_stamp=? and table_no=?", order.getStatus(), order.getCancelNote() != null ? order.getCancelNote() : "",
               order.getTimestamp(), order.getTableNo());
   }

   @Override
   public void addItem(Item item) throws SQLException {
         ArrayList<Object[]> results = mydatabase.query(
               "insert into sep2.items (item_name,ingredients,allergens,price,category_id) values (?,?,?,?,(select category_id from sep2.category where category_name=?) ) RETURNING item_id",
               item.getName(), item.getIngredients(), item.getAllergens(), item.getPrice(), item.getCategory());
         item.setId(Integer.parseInt(results.get(0)[0].toString()));
   }

   @Override
   public String[] getCategories() throws SQLException {
      String sql = "SELECT CATEGORY.CATEGORY_NAME FROM sep2.CATEGORY";
      ArrayList<String> allCategories = null;
         ArrayList<Object[]> results = mydatabase.query(sql);
         allCategories = new ArrayList<String>();
         for (int i = 0; i < results.size(); i++) {
            allCategories.add(results.get(i)[0].toString());
         }
      return allCategories.toArray(new String[allCategories.size()]);
   }

   @Override
   public void editItem(Item item) throws SQLException {
         mydatabase.update("update sep2.items set item_name=?,ingredients=?,allergens=?,price=?,category_id=(select category_id from sep2.category where category_name=?) where item_id=?",
               item.getName(), item.getIngredients(), item.getAllergens(), item.getPrice(), item.getCategory(), item.getId());
   }

   @Override
   public void removeItem(Item item) throws SQLException {
         mydatabase.update("delete from sep2.orders_items where item_id=?", item.getId());
         mydatabase.update("delete from sep2.items where item_id=?", item.getId());
   }
}