package application;

public class Supp_Ing {
int supp_ID;
String address;
String phone;
String supp_Name;
String ing_Name;
public Supp_Ing(int supp_ID, String address, String phone, String supp_Name, String ing_Name) {
	super();
	this.supp_ID = supp_ID;
	
	this.address = address;
	this.phone = phone;
	this.supp_Name = supp_Name;
	this.ing_Name = ing_Name;
}
public int getSupp_ID() {
	return supp_ID;
}
public void setSupp_ID(int supp_ID) {
	this.supp_ID = supp_ID;
}

public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getSupp_Name() {
	return supp_Name;
}
public void setSupp_Name(String supp_Name) {
	this.supp_Name = supp_Name;
}
public String getIng_Name() {
	return ing_Name;
}
public void setIng_Name(String ing_Name) {
	this.ing_Name = ing_Name;
}
	
	
	
	
	
	
}
