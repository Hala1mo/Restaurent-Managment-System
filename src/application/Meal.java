package application;

public class Meal {
	
	int ID;
	String m_Name ;
	String m_Type;
	double price;
	
	
	public Meal(int iD, String m_Name, String m_Type, double price) {
		super();
		ID = iD;
		this.m_Name = m_Name;
		this.m_Type = m_Type;
		this.price = price;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getM_Name() {
		return m_Name;
	}
	public void setM_Name(String m_Name) {
		this.m_Name = m_Name;
	}
	public String getM_Type() {
		return m_Type;
	}
	public void setM_Type(String m_Type) {
		this.m_Type = m_Type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	

}
