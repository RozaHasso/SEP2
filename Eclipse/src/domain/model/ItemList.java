package domain.model;

import java.util.ArrayList;

public class ItemList implements java.io.Serializable {

	/**
    * 
    */
   private static final long serialVersionUID = -6056561842815330876L;
   private ArrayList<Item> items;
	
	public ItemList() 
	{
	   items = new ArrayList<Item>();
	}

	public void add(Item item) {
	   items.add(item);
	}

	public void remove(Item item) {
	   items.remove(item);
	}

	public Item[] getItemsByCategory(String category) {
		ArrayList<Item> itemsByCategory = new ArrayList<Item>();
		for(int i = 0; i < items.size(); i++)
		{
		   if (items.get(i).getCategory().equals(category))
		   {
		      itemsByCategory.add(items.get(i));
		   }
		}
	   return itemsByCategory.toArray(new Item[items.size()]);
	}

	
	public Item get(int index) {
		return items.get(index);
	}

	public int size() {
		return items.size();
	}

}
