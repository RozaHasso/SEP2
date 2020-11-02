package kitchen;

public class KitchenSingleton {

   private static KitchenSingleton instance;

   private int tables, port;

   private KitchenSingleton() {

   }

   public static KitchenSingleton getInstance() {
      if (instance == null)
         instance = new KitchenSingleton();
      return instance;
   }

   public void prepareData(String args[]) {
         tables = Integer.parseInt(args[0]);
         port = Integer.parseInt(args[1]);
   }

   public int getTables() {
      return tables;
   }

   public int getPort() {
      return port;
   }

}
