package application;

public class Order {
	int order_ID;
	int cust_ID ;
	int employee_ID ;
String date;
	public Order(int order_ID, int cust_ID, int employee_ID, String date) {
		super();
		this.order_ID = order_ID;
		this.cust_ID = cust_ID;
		this.employee_ID = employee_ID;
		this.date = date;
	}

	public int getOrder_ID() {
		return order_ID;
	}
	public void setOrder_ID(int order_ID) {
		this.order_ID = order_ID;
	}
	public int getCust_ID() {
		return cust_ID;
	}
	public void setCust_ID(int cust_ID) {
		this.cust_ID = cust_ID;
	}
	public int getEmployee_ID() {
		return employee_ID;
	}
	public void setEmployee_ID(int employee_ID) {
		this.employee_ID = employee_ID;
	}
	

	
	
	
	
}
