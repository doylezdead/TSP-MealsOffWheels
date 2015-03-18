package mow.server;

import mow.common.Payload;


class Runner{
	/**
	 * checkSecurity returns true on a success
	 *
	 */
	public boolean checkSecurity(String input){
		//maybe read password out of a file somewhere?
		return true;
	}
	
	public Payload parseOps(Payload readData){
		
		Payload retData = new Payload(0,0,0,"fail");
		
		if(readData.value == -1){
			throw Exception("parseOps failed with payload value of " + readData.value);
		}
		
		int value = readData.value;
		int opcode = readData.opcode;
		int weight = readData.weight;
		double xcoord = readData.xcoord;
		double ycoord = readData.ycoord;
		String name = readData.name;
		int order_num = null; //this will be defined and returned in the return payload
		
		switch(opcode){
			case 0: // checking deliverability
					int storeID = findNearestStore(xcoord,ycoord);
					if (checkStoreStatus(storeID)){
						retData.value = storeID;
						retData.opcode = 0;
					}
			case 1:	// Placing an order
					retData.value = placeOrder(name, 15174047199, value, xcoord, ycoord, weight);
					
			case 2:	// Retrieving order status
					if (checkOrderIsGood(value)){
						retData.value = checkOrderETA(value);
						retData.xcoord = getOrderXPos(value);
						retData.ycoord = getOrderYPos(value);
					}
		}
		

		//RETURNING A ZERO VALUE TO THE CLIENT MEANS SOMETHING FAILED. MUST CHECK ON CLIENT
		return retData;
	}
	private


}

// Store status. checking deliverability
private int findNearestStore(double x, double y){
	return 0; //return nearest store's id
}

private boolean checkStoreStatus(){
	return true; // or false
}
//

// Place Order
private int placeOrder(String name, int phone, int storeID, double x, double y, int weight){
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
	return true: //or false. true is good status
}
//
















