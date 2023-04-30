package application;

public class customer {
	int cust_id;
	String cname;
	String email;
	String address;
	public customer(int cust_id, String cname, String email, String address) {
		super();
		this.cust_id = cust_id;
		this.cname = cname;
		this.email = email;
		this.address = address;
	}
	public int getCust_id() {
		return cust_id;
	}
	public void setCust_id(int cust_id) {
		this.cust_id = cust_id;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
