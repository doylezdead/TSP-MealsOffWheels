package mow.server;

import mow.common.Payload;
import mow.server.DBuser;
import java.sql.Connection;
import java.sql.ResultSet;

class Runner{
	
	public Payload parseOps(Payload readData){
		
		DBuser dbu = new DBuser();

		Payload retData = new Payload(0,0,0,"fail","none");
		
		if(readData.value < 0){
			throw Exception("parseOps failed with payload value of " + readData.value);
		}
		
		int value = readData.value;
		int opcode = readData.opcode;
		int weight = readData.weight;
		double xcoord = readData.xcoord;
		double ycoord = readData.ycoord;
		String name = readData.name;
		String contact = readData.contact;

		Connection auth = DBuser.authenticate("jdbc:mysql//doyle.pw:3306/mow","rcdoyle","doyle"); //this will need to be in place in order to authenticate to the database. 


		if (auth==null){
			throw Exception("mysql server connection failed :'( maybe it's not running?");
		}

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
						retData.value = dbu.checkOrderETA(auth, value);
						retData.xcoord = dbu.getOrderXPos(auth, value);
						retData.ycoord = dbu.getOrderYPos(auth, value);
					}
		}

		if(retData.value == 0){ System.out.println("WARNING. ZERO VALUE BEING RETURNED TO CLIENT. GOOD LUCK"); }
		auth.close();//close auth here

		//RETURNING A ZERO VALUE TO THE CLIENT MEANS SOMETHING FAILED. MUST CHECK ON CLIENT
		return retData;
	}

}
















