package domain.model;

public class Item implements java.io.Serializable {

	/**
    * 
    */
   private static final long serialVersionUID = -5029835462285517116L;
   private int id;
	private String name;
	private String ingredients;
	private String allergens;
	private String category;
	private double price;

   public Item(int id, String name, String ingredients, String allergens, String category, double price) {
		this.id = id;
		this.name = name;
		this.ingredients = ingredients;
		this.allergens = allergens;
		this.category = category;
		this.price = price;
	}

	public Item(String name, String ingredients, String allergens, String category, double price) {
		this.name = name;
		this.ingredients = ingredients;
		this.allergens = allergens;
		this.category = category;
		this.price = price;
	}

	public void setAllergens(String allergens) {
		this.allergens = allergens;
	}

	public void setPrice(double price) {
      this.price = price;
   }
	
	public double getPrice() {
		return price;
	}

	public String getAllergens() {
		return allergens;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		return name + " (" + category + ")";
	}

}
