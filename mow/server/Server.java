package mow.server;

import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import mow.common.Payload;

// run thread with new Server().start();


public class Server extends Thread {
	private Socket skt = null;
	
	
	// Constructor for threading purposes. keeps socket in this particular instance of Server
	public Server(Socket sktin){
		this.skt = sktin;
	}

	//Waits for connects forever
	public static void main(String[] args) throws IOException {
		
		ServerSocket srvr = new ServerSocket(25565);

		while (true) {
			System.out.println("Waiting for connections ...");
			Socket skt = srvr.accept(); //Set timeout with setSoTimeout(int timeout) function where timeout is in milliseconds
			System.out.println("Client with ip of " + skt.getRemoteSocketAddress() + " has connected to the server!!!");
			(new Server(skt)).start();
		}
	}

	public void run() {
		//Links the database, maps, and server together? essentially!
		if(skt == null){
			System.out.println("bad things have happened");
			return;
		}


		Runner worker = new Runner();


		try {

			ObjectInputStream in = new ObjectInputStream(skt.getInputStream()); 	// skt's input stream for recieving data
			ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream()); // skt's output stream for returning data to app

			Payload readData = null; // this will be the payload coming in. 

			while (true) {

				//Checks if client disconnected before sending data and shuts down gracefully
				if (skt.isClosed()) {
					in.close();
					out.close();
					skt.close(); // close all resources
					break;
				}

				readData = (Payload) in.readObject();
				
				//wait for readData to come in from the client
				if (readData == null)
					continue;
				
				
				//Define at later date, this should return the data to give to app and may be a switch herea
				//We could probably get away with the same object type for writing data back. (coords and other values just serve a different purpose etc.)
				
				Payload writeData = null;

				try{

					//sending full payload to the parser
					writeData = worker.parseOps(readData);	
					
					//returning the output payload
					out.writeObject(writeData);

				}catch(Exception e){

					e.printStackTrace();
					System.out.println("Probably Shoudln't write the data... But we will anyway");
					//break;

				}
			}



		} 
		// Could be no connection found, security, io, or corruption in stream
		catch (EOFException e) {
			System.out.println("Client has disconnected");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
