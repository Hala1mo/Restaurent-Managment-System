package application;

import java.util.Date;

public class OrderCustomer {

	
	int ID;
	String ename;
	int order_ID;
	Date date; 
	int cut_ID;
	int meal_ID;
	int Quantity;
	double price;


	public OrderCustomer(int iD, String ename, int order_ID, Date date,int cut_ID,int meal_ID, int quantity, double price) {
		super();
		ID = iD;
		this.ename = ename;
		this.order_ID = order_ID;
		this.cut_ID = cut_ID;
		this.meal_ID = meal_ID;
		Quantity = quantity;
		this.price = price;
		this.date=date;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public int getOrder_ID() {
		return order_ID;
	}
	public void setOrder_ID(int order_ID) {
		this.order_ID = order_ID;
	}
	public int getCut_ID() {
		return cut_ID;
	}
	public void setCut_ID(int cut_ID) {
		this.cut_ID = cut_ID;
	}
	public int getMeal_ID() {
		return meal_ID;
	}
	public void setMeal_ID(int meal_ID) {
		this.meal_ID = meal_ID;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Date getDate() {
		return date;
	}
	public void setBirth(Date date) {
		this.date=date;
	}
	public String getDatee() {
		String x="";
		x=date.getDay()+""+date.getMonth()+""+date.getYear();
		return x;
	}
	

}
