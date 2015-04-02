package mow.server;

import java.sql.*;
import java.util.*

class DBuser{
	
	Connection authenticate(String server, String user, String password){
		try{
			return DriverManager.getConnection(server,user, password);
		}
		catch(Exception e){
			system.out.println("Server could not connect to database on server " + server +".");
			return null;
		}
	}
	
	
	// Store status. checking deliverability
	// TODO Find NEAREST STORE
	//Currently gets the first store in the table stores
	/**
	 * Returns the store id as an int of the closest store
	 * @param con The Connection to the database
	 * @param x The Lng of the delivery site
	 * @param y The Lat of the delivery site
	 * @throw SQLExecption
	 * @return The id of the cloest store
	 */
	int findNearestStore(Connection con, double x, double y) throws SQLExecption{
		Statement stmt = con.createStatement();
		
		ResultSet rs = stmt("SELECT id From stores");
		rs.next();
		int i = rs.getInt("id");
		stmt.close();
		return i;
		
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
		}
		stmt.close();
		return closest;
		*/
	}
	
	/** Return the true if the store is open and false if closed
	  * @param con The connection to the database
	  * @param storeID The id of the storeID
	  * @throw SQLExecption
	  * @return true if open else false
	  */
	public boolean checkStoreStatus(Connection con, int storeID) throws SQLException {
		//Create statmenet and result of doing select
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Open FROM stores WHERE StoreID == " + storeID);
		//Move cursor to first row
		rs.next();
		//Get value to return and close statement
		boolean r = rs.getBoolean("Open");
		stmt.close();
		return r; 
	}
	
	// Place Order
	/**
	 * Returns the order id
	 * @param con The Connection to the database
	 * @param name The user name
	 * @param contact The contact information for the order
	 * @param storeID The store the order is being placed at
	 * @param x The Lng location of the delivery
	 * @param y The Lat location of the delivery
	 * @param wieght The weight of the order in grams
	 * @throws SQLExecption
	 * @return THe orderID as an int
	 */
	public int placeOrder(Connection con, String name, String contact, int storeID, double x, double y, int weight) throws SLQExecption {
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id FROM users WHERE Name == " + name);
		int userID = rs.getInt("id");
		
		rs = stmt.executeQuery("SELECT * FROM orders");
		rs.moveToInsertRow();
		
		rs.updateString("StoreID", storeID);
		rs.updateInt("UserID", userID);
		rs.updateString("Contact",contact);
		rs.updateFloat("DeliveryLng", x);
		rs.updateBoolean("GoodOrder", null);
		rs.updateFloat("DeliveryLat", y);
		rs.updateInt("TimeReceived", null); //CHANGE THIS !!!!!!!!!!
		rs.updateTimeStamp("TimeDeliveried", null);
		
		rs.insertRow();
		
		int orderID = rs.getInt("id");
		
		stmt.close();
		return orderID; //returns newly created order identity
	}
	
	//Getting Order Status
	//NOT HERE 
	/**
	 * Returns the second til the time of delivery
	 * @param con The Connection to the database
	 * @param OrderID 
	 * @throws SQLExecption
	 * @return The time in to delivery as seconds as an int
	 */ 
	 /*
	public int checkOrderETA(Connection con, int OrderID) throws SQLExecption {
		
	 int i;
	 
	 return i;//return time in seconds
	}
	*/
	
	/**
	 * Return the Longitude of the delivery location
	 * @param con The Connection to the database
	 * @param OrderID The id of the order
	 * @throws SQLExecption 
	 * @return the Longitude of the delivery location
	 */
	public double getOrderXPos(Connection con, int OrderID) throws SQLExecption {

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DeliveryLng FROM orders WHERE id == " + OrderID);
		rs.next();
		double x = rs.getDouble("DeliveryLng");
		
		stmt.close();
		return x;
	}
	
		/**
	 * Return the Latitude of the delivery location
	 * @param con The Connection to the database
	 * @param OrderID The id of the order
	 * @throws SQLExecption 
	 * @return the Latitude of the delivery location
	 */
	public double getOrderYPos(Connection con, int OrderID) throws SQLExecption {
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DeliveryLat FROM orders WHERE id == " + OrderID);
		rs.next();
		double x = rs.getDouble("DeliveryLat");
		
		stmt.close();
		return x;
	}
	/**
	 * Returns true for an accpeted order else false
	 * @param con Connection to the database
	 * @param OrderID the id number of the order
	 * @throws SQLExecption
	 * @return True if order has been approved and accpeted
	 */
	public boolean checkOrderIsGood(int OrderID){
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Good FROM orders WHERE id == " + OrderID);
		rs.next();
		boolean x = rs.getBoolean("Good");
		
		stmt.close();
		return x; //or false. true is good status
	}
	

}
