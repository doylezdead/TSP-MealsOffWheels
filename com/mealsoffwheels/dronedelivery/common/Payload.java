package com.mealsoffwheels.dronedelivery.common;
import java.io.Serializable;

public class Payload implements Serializable{
	public int value; //return code for non-coordinate returning values. can also define send or return.
	public int opcode; //represent different operations or requests. 
	public int weight;
	public double xcoord; //gps locations
	public double ycoord; //gps locations
	public String name; //name of orderee. for database storage.
	public String contact;

	public Payload(int value, int opcode, double xcoord, double ycoord, String name, String contact){
		this.value = value;
		this.opcode = opcode;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.name = name;
		this.contact = contact;
	}

	

}
