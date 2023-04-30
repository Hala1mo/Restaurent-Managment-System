package application;

import java.util.Date;

public class Employee {

	int ID;
	String Address;
	Date birth; 
	String gender;
	String ename;
	String designation;
	String phone;
	int salary;
	
	public Employee(int iD, String ename, Date birth, String gender, String address, String designation,String phone, int salary) {
		super();
		ID = iD;
		Address = address;
		this.birth = birth;
		this.gender = gender;
		this.ename = ename;
		this.designation = designation;
		this.phone=phone;
		this.salary = salary;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDatee() {
		String x="";
		x=birth.getDay()+""+birth.getMonth()+""+birth.getYear();
		return x;
	}
	
	
	
}
