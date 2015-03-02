package com.mealsoffwheels.dronedelivery;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    public final int HEADER = 0x4D4F5754; // MOWT in ASCII (meals off wheels team)



    private void writeObject(java.io.ObjectOutputStream out) throws IOException {

    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.readInt();
    }

    private void readObjectNoData() throws ObjectStreamException {

    }
}
