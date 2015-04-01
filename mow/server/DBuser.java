package mow.server;

import java.sql.*;
import java.util.*

class DBuser{
	
	Connection authenticate(String server){
		try{
			return DriverManager.getConnection(server,"test","bad password");
		}
		catch(Exception e){
			system.out.println("Server could not connect to database on server " + server +".");
			return null;
		}
	}
	
	
	// Store status. checking deliverability
	// TODO Find NEAREST STORE
	//Currently gets the first store in the table stores
	int findNearestStore(Connection con, double x, double y){
		Statement stmt = con.createStatement();
		
		ResultSet rs = stmt("SELECT id From stores");
		rs.next();
		return rs.getInt("id");
		
		/* More helpful when finding actual closest store
		ResultSet rs = stmt("SELECT id StoreLat StoreLng FROM stores WHERE StoreLat BETWEEN "+(y-.14492754)+" and " +(y+.14492754) WHERE StoreLng BETWEEN "+(x-.14492754)+" and " +(x+.14492754));
		int clostest = 1;
		int tempx = 0;
		int tempy = 0;
		
		//No Stores in range??
		
		while (rs.next()) {
			
			tempy = rs.getDouble("StoreLat");
			tempx = rs.getDouble("StoreLng");
			//Find closest here
		*/
	}
	
	public boolean checkStoreStatus(){
		
		return true; // or false
	}
	//
	
	// Place Order
	public int placeOrder(String name, String contact, int storeID, double x, double y, int weight){
		//create order. assign a drone id and a store id
		orderID = 0;
		//orderID = new Order();
		
		return orderID; //returns newly created order identity
	}
	//
	
	//Getting Order Status
	public int checkOrderETA(int OrderID){
		return 0; //return time in seconds
	}
	
	public double getOrderXPos(int OrderID){
		return 0;
	}
	
	public double getOrderYPos(int OrderID){
		return 0;
	}
	
	public boolean checkOrderIsGood(int OrderID){
		
		return true; //or false. true is good status
	}
	

}
//
















