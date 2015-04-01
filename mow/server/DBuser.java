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
	int findNearestStore(double x, double y){
		
	}
	
	private boolean checkStoreStatus(){
		return true; // or false
	}
	//
	
	// Place Order
	private int placeOrder(String name, String contact, int storeID, double x, double y, int weight){
		//create order. assign a drone id and a store id
		orderID = 0;
		//orderID = new Order();
		
		return orderID; //returns newly created order identity
	}
	//
	
	//Getting Order Status
	private int checkOrderETA(int OrderID){
		return 0; //return time in seconds
	}
	
	private double getOrderXPos(int OrderID){
		return 0;
	}
	
	private double getOrderYPos(int OrderID){
		return 0;
	}
	
	private boolean checkOrderIsGood(int OrderID){
		return true; //or false. true is good status
	}
	

}
//
















