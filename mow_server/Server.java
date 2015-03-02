package mow_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


// run thread with new Server().start();


public class Server extends Thread {
	
	
	//Waits for connects forever
	public static void main(String[] args) throws IOException {

		ServerSocket srvr = new ServerSocket(13337);

		while (true) {
			System.out.println("Waiting for connections ...");
			Socket skt = srvr.accept(); //Set timeout with setSoTimeout(int timeout) function where timeout is in milliseconds
			System.out.print("Client has connected to the server!!!\n");
			run(skt);
		}
	}

	private static void run(Socket skt) {
		//Links the database, maps, and server together? essentially!

		Runner worker = new Runner();


		try {

			ObjectInputStream in = new ObjectInputStream(skt.getInputStream()); 	// skt's input stream for recieving data
			ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream()); // skt's output stream for returning data to app


			//Define at later date
			//Maybe make this a double array or something of the sorts for parsing the data. or go full custom payload
			Payload readData = null;

			while (true) {

				//Checks if client disconnected before sending data and shuts down gracefully
				if (skt.isClosed()) {
					in.close();
					out.close();
					skt.close(); // close all resources
					break;
				}

				readData = (Payload) in.readObject();

				
				//wait for readData
				if (readData != null)
					continue;


				//I like this idea
				//If data is not secure, don't execute, make a security report, and close
				if (worker.checkSecurity(readData.password)) {
					in.close();
					out.close();
					skt.close(); // close all resources
					break;
				}

				//Define at later date, this should return the data to give to app and may be a switch herea
				//We could probably get away with the same object type for writing data back. (coords and other values just serve a different purpose etc.)
				Payload writeData = worker.parseOps(readData);	

				out.writeObject(writeData);
			}



		} 
		// Could be no connection found, security, io, or corruption in stream
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
