package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class test {
	static ArrayList<Integer> m = new ArrayList<>();
	
	public static void main(String[] args) {
		System.out.println("hereeeee");
		try {
			 DataBaseConnection dp = new DataBaseConnection();
			Connection con = dp.getConnection().connectDB();
			String sql = "Select * from customer";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("hhhhhhhhhhhhhh");
			while(rs.next()) {				
				System.out.println("Id : "+rs.getString(2));
			}
			con.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(m.toString());
	}

}
