package com.mealsoffwheels.dronedelivery;

public class Payload {
    int value; //return code for non-coordinate returning values. can also define send or return.
    int opcode; //represent different operations or requests.
    double xcoord; //gps locations
    double ycoord; //gps locations
    String name; //name of orderee. for database storage.
    int order_num;
    String password;

    Payload(int opcode, double xcoord, double ycoord, String name){
        this.opcode = opcode;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.name = name;
    }
}