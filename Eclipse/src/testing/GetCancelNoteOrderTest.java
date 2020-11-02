package testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.model.Order;

public class GetCancelNoteOrderTest
{

   

   @Test
   public void test()
   {
      Order order1 = new Order(5);
      order1.setCancelNote("no water");
      
      assertEquals(order1.getCancelNote(),"no water");
   }

}
