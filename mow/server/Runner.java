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
		
		Payload retData = null;
		
		if(readData.value != 0){
			throw Exception("parseOps failed with payload value of " + readData.value);
		}

		int opcode = readData.opcode;
		
		double xcoord = readData.xcoord;
		double ycoord = readData.ycoord;
		String name = readData.name;
		int order_num = null; //this will be defined and returned in the return payload
		



		return readData;
	}
	private


}
