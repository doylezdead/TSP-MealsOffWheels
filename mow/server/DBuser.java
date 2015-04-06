package mow.server;

import java.sql.*;
import java.util.*;

class DBuser{
	
	Connection authenticate(String server, String user, String password){
		try{
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return DriverManager.getConnection(server,user,password); //plaintextpasswordlol
		}
		catch(SQLException e){
			System.out.println("Server could not connect to database on server " + server +". Printing stack trace");
			e.printStackTrace();
			return null;
		}
		catch(Exception e){
			System.out.println("jdbc driver could not be loaded. printing stack trace..");
			e.printStackTrace();
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
	 * @throw SQLException
	 * @return The id of the cloest store
	 */
	int findNearestStore(Connection con, double x, double y) throws SQLException{
		Statement stmt = con.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT ID From stores");
		rs.next();
		int i = rs.getInt("ID");
		stmt.close();
		return i;
		
		/* More helpful when findin
		
		//Find open stores within 100 square miles
		ResultSet rs = stmt("SELECT ID StoreLat StoreLng FROM stores WHERE StoreLat BETWEEN "+(y-.14492754)+" and " +(y+.14492754) WHERE StoreLng BETWEEN "+(x-.14492754)+" and " +(x+.14492754) + "WHERE Open == 1");
		int closest = 1;
		int tempx = 0;
		int tempy = 0;
		double distance = -1;
		double tempd = -1;
		//No Stores in range??
		
		//Still stores to check
		while (rs.next()) {
			
			tempy = rs.getDouble("StoreLat");
			tempx = rs.getDouble("StoreLng");
			
			if (distance == -1) {
				closest = rs.getInt("ID");
				distance = Math.sqrt( Math.pow(tempx - x, 2) + Math.pow(tempy - y, 2) );
				continue;
			}
			tempd = Math.sqrt( Math.pow(tempx - x, 2) + Math.pow(tempy - y, 2) );
			
			if (tempd < distance)
				distance = tempd;
		}
		stmt.close();
		return closest;
		*/
	}
	
	/** Return the true if the store is open and false if closed
	  * @param con The connection to the database
	  * @param storeID The id of the storeID
	  * @throw SQLException
	  * @return true if open else false
	  */
	public boolean checkStoreStatus(Connection con, int storeID) throws SQLException {
		//Create statmenet and result of doing select
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Open FROM stores WHERE StoreID=" + storeID);
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
	 * @throws SQLException
	 * @return THe orderID as an int
	 */
	public int placeOrder(Connection con, String name, String contact, int storeID, double x, double y, int weight) throws SQLException {
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT ID FROM users WHERE UserName='" + name + "'");
		int userID = rs.getInt("ID");
		
		rs = stmt.executeQuery("SELECT * FROM orders");
		rs.moveToInsertRow();
		
		rs.updateInt("StoreID", storeID);
		rs.updateInt("UserID", userID);
		rs.updateString("Contact",contact);
		rs.updateDouble("DeliveryLng", x);
		rs.updateDouble("DeliveryLat", y);
		
		//Test to see if row is made after this
		rs.insertRow();
		rs.next();
		
		int orderID = rs.getInt("ID");
		
		stmt.close();
		return orderID; //returns newly created order identity
	}
	
	//Getting Order Status
	//NOT HERE 
	/**
	 * Returns the second til the time of delivery
	 * @param con The Connection to the database
	 * @param OrderID 
	 * @throws SQLException
	 * @return The time in to delivery as seconds as an int
	 */ 
	 /*
	public int checkOrderETA(Connection con, int OrderID) throws SQLException {
		
	 int i;
	 
	 return i;//return time in seconds
	}
	*/
	
	/**
	 * Return the Longitude of the delivery location
	 * @param con The Connection to the database
	 * @param OrderID The id of the order
	 * @throws SQLException 
	 * @return the Longitude of the delivery location
	 */
	public double getOrderXPos(Connection con, int OrderID) throws SQLException {

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DeliveryLng FROM orders WHERE id=" + OrderID);
		rs.next();
		double x = rs.getDouble("DeliveryLng");
		
		stmt.close();
		return x;
	}
	
		/**
	 * Return the Latitude of the delivery location
	 * @param con The Connection to the database
	 * @param OrderID The id of the order
	 * @throws SQLException 
	 * @return the Latitude of the delivery location
	 */
	public double getOrderYPos(Connection con, int OrderID) throws SQLException {
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DeliveryLat FROM orders WHERE i=" + OrderID);
		rs.next();
		double y = rs.getDouble("DeliveryLat");
		
		stmt.close();
		return y;
	}
	/**
	 * Returns true for an accpeted order else false
	 * @param con Connection to the database
	 * @param OrderID the id number of the order
	 * @throws SQLException
	 * @return True if order has been approved and accpeted
	 */
	public boolean checkOrderIsGood(Connection con, int OrderID) throws SQLException{
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Approved FROM orders WHERE id=" + OrderID);
		rs.next();
		boolean x = rs.getBoolean("Approved");
		
		stmt.close();
		return x; //or false. true is good status
	}
	

}
