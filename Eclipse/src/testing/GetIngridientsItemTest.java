package testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.model.Item;

public class GetIngridientsItemTest
{


   @Test
   public void test()
   {
      Item item1 = new Item("COLA","some ingridients","some allegrens","BEVERAGES",50);
      assertEquals(item1.getIngredients(), "some ingridients");
   }

}
