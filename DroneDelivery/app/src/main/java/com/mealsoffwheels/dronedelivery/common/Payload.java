package com.mealsoffwheels.dronedelivery.common;

import java.io.Serializable;

/**
 * Defines the object to send to the server.
 */
public class Payload implements Serializable{
    public int value; //return code for non-coordinate returning values. can also define send or return.
    public int opcode; //represent different operations or requests.
    public int weight;
    public double xcoord; //gps locations
    public double ycoord; //gps locations
    public String name; //name of orderee. for database storage.
    public String contact;

    /**
     * Initialize object to send to server.
     *
     * @param value - Any extra data value server needs.
     * @param opcode - Operation server needs to perform.
     * @param xcoord - User location x coordinate.
     * @param ycoord - User location y coordinate.
     * @param name - User's name.
     * @param contact - User's contact info.
     */
    public Payload(int value, int opcode, double xcoord, double ycoord, String name, String contact){
        this.value = value;
        this.opcode = opcode;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.name = name;
        this.contact = contact;
    }



}
