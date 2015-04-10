package com.mealsoffwheels.dronedelivery.server;

import java.sql.*;
import java.util.*;

class DBuser{
	
	Connection authenticate(String server, String user, String password){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance(); //register the jdbc driver
			return DriverManager.getConnection(server,user,password); //plaintextpasswordlol
		}
		catch(SQLException e){
			System.out.println("An issue occurred connecting to database on server " + server + ". Printing stack trace");
			e.printStackTrace();
			return null;
		}
		catch(Exception e){
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
		
		int i = -1;

		if( rs.next() ){
			i = rs.getInt(1);
		}
		
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
			
			tempy = rs.getDouble(2);
			tempx = rs.getDouble(3);
			
			if (distance == -1) {
				closest = rs.getInt(1);
				distance = Math.sqrt( Math.pow(tempx - x, 2) + Math.pow(tempy - y, 2) );
				continue;
			}
			tempd = Math.sqrt( Math.pow(tempx - x, 2) + Math.pow(tempy - y, 2) );
			
			if (tempd < distance) {
				distance = tempd;
				closeset = rs.getInt(1);
			}
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
		ResultSet rs = stmt.executeQuery("SELECT Open FROM stores WHERE ID=" + storeID);
		//Move cursor to first row
		//Get value to return and close statement
		boolean r = false;	
		
		if( rs.next() ){
			r = rs.getBoolean(1);
		}
		
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
		
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		ResultSet rs = stmt.executeQuery("SELECT ID FROM users WHERE UserName='" + name + "'");

		int userID;
		if (rs.next()){
			userID = rs.getInt(1);
		}
		else { 
			//creating new entry 
			rs = stmt.executeQuery("SELECT * FROM users");
			rs.moveToInsertRow();
			rs.updateDouble(2, x);
			rs.updateDouble(3, y);
			rs.updateString(4, name);
			rs.updateString(5, contact);
			rs.updateBoolean(7, true);
			rs.insertRow();
			rs.last();
			userID = rs.getInt(1);
		}

		rs = stmt.executeQuery("SELECT * FROM orders");
		
		rs.moveToInsertRow();
		rs.updateInt(2, userID);
		rs.updateInt(3, storeID);
		if
		rs.updateDouble(4, y);
		rs.updateDouble(5, x);
		rs.updateBoolean(6,true);

		rs.insertRow();
		rs.last();
		
		//TODO Get the actual id back instead of zero
		int orderID = rs.getInt(1);

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
		
		double x = -1;
		
		if( rs.next() ){
			x = rs.getDouble(1);
		}
		
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
		ResultSet rs = stmt.executeQuery("SELECT DeliveryLat FROM orders WHERE ID=" + OrderID);
		
		double y = -1;
		if( rs.next() ){
			y = rs.getDouble(1);
		}
		
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
		
		boolean x = false;

		if( rs.next() ){
			x = rs.getBoolean(1);
		}
		
		
		stmt.close();
		return x; //or false. true is good status
	}
	

}
