package application;

public class Ingredients {
	int ID;
	int supp_id;
	String ing_name;
	
	
	public Ingredients(int iD, int supp_id, String ing_name) {
		super();
		ID = iD;
		this.supp_id = supp_id;
		this.ing_name = ing_name;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getSupp_id() {
		return supp_id;
	}
	public void setSupp_id(int supp_id) {
		this.supp_id = supp_id;
	}
	public String getIng_name() {
		return ing_name;
	}
	public void setIng_name(String ing_name) {
		this.ing_name = ing_name;
	}
	
}