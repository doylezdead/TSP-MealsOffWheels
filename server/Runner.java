package server;

class Runner{
	public boolean checkSecurity(String input){
		return true;
	}
	
	public Payload parseOps(Payload readData){
		readData.value = 100;
		return readData;
	}


}
