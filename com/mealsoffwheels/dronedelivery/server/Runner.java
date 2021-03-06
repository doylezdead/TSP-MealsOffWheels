package com.mealsoffwheels.dronedelivery.server;

import com.mealsoffwheels.dronedelivery.common.Payload;
import com.mealsoffwheels.dronedelivery.server.DBuser;
import java.sql.SQLException;
import java.sql.Connection;


class Runner{
	
	public Payload parseOps(Payload readData) throws Exception, SQLException {
		
		DBuser dbu = new DBuser();

		Payload retData = new Payload(0,0,0,0,"fail","none");
		
		if( readData.value < 0 || readData.opcode < 0 || readData.opcode > 2 ) {
			throw new Exception("parseOps failed with payload value of " + readData.value + " and opcode of " + readData.opcode);
		}
		
		int value = readData.value;
		int opcode = readData.opcode;
		int weight = readData.weight;
		double xcoord = readData.xcoord;
		double ycoord = readData.ycoord;
		String name = readData.name;
		String contact = readData.contact;
		
		Connection auth = dbu.authenticate("jdbc:mysql://192.168.2.101:3306/test","rcdoyle","doyle"); //this will need to be in place in order to authenticate to the database. 

		if (auth==null){
			throw new SQLException("dbuser failed to return a valid connection :'( maybe it's not running or dbuser is broken?");
		}
		
		try{
			switch(opcode){
				case 0: // checking deliverability
						int storeID = dbu.findNearestStore(auth, xcoord,ycoord);
						if (dbu.checkStoreStatus(auth, storeID)){
							retData.value = storeID;
							retData.opcode = 0;
						}
	
				case 1:	// Placing an order
						retData.value = dbu.placeOrder(auth, name, contact, value, xcoord, ycoord, weight);
						
				case 2:	// Retrieving order status
						if (dbu.checkOrderIsGood(auth, value)){
							retData.value = 1;
							retData.xcoord = dbu.getOrderXPos(auth, value);
							retData.ycoord = dbu.getOrderYPos(auth, value);
						}
			}
		}
		catch(Exception e){
			System.out.println("a DBuser call failed! printing stack trace...");
			e.printStackTrace();
		}

		if(retData.value == 0){ System.out.println("WARNING. ZERO VALUE BEING RETURNED TO CLIENT. GOOD LUCK"); }
		auth.close();//close auth here

		//RETURNING A ZERO VALUE TO THE CLIENT MEANS SOMETHING FAILED. MUST CHECK ON CLIENT
		//
		System.out.println("The return payload is on it's way!");
		return retData;
	}

}
















