package application;

public class meal_ing {
	
	int id_meal=0;
    String mealname;
    String mealType;
    double price;
    String ing;
	public meal_ing(int id_meal, String mealname, String mealType, double price, String ing) {
		super();
		this.id_meal = id_meal;
		this.mealname = mealname;
		this.mealType = mealType;
		this.price = price;
		this.ing = ing;
	}
	public int getId_meal() {
		return id_meal;
	}
	public void setId_meal(int id_meal) {
		this.id_meal = id_meal;
	}
	public String getMealname() {
		return mealname;
	}
	public void setMealname(String mealname) {
		this.mealname = mealname;
	}
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getIng() {
		return ing;
	}
	public void setIng(String ing) {
		this.ing = ing;
	}

	

	

}
