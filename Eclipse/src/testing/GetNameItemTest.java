package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.model.Item;
import domain.model.ItemList;

public class GetNameItemTest
{
   @Test
   public void test()
   {
     
     Item item1 = new Item("COLA","some ingridients","some allegrens","BEVERAGES",50);
     assertEquals(item1.getName(), "COLA");
     
   }
}
