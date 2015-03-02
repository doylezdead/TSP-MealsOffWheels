package server;

class Payload{
	int value; //return code for non-coordinate returning values. can also define send or return.
	int opcode; //represent different operations or requests. 
	double xcoord; //gps locations
	double ycoord; //gps locations
	String name; //name of orderee. for database storage.
	int order_num;
	String password;

	void Payload(int opcode, int xcoord, int ycoord, String name){
		this.opcode = opcode;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.name = name;
	}

	

}
