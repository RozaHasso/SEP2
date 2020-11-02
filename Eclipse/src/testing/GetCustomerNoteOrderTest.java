package testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.model.Order;

public class GetCustomerNoteOrderTest
{

   

   @Test
   public void test()
   {
      Order order1 = new Order(5);
      order1.setCancelNote("without ice");
      
      assertEquals(order1.getCancelNote(),"without ice");
   }

}
