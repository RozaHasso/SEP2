package testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
   GetAllergensItemTest.class, 
   GetCategoryItemTest.class,
   GetIngridientsItemTest.class, 
   GetNameItemTest.class,
   GetPriceItemTest.class })
public class AllGettersItemTest
{

}
