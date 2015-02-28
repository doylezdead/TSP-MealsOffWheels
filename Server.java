import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


// run thread with new Server().start();


public class Server extends Thread {
	public Server() {

	}

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

	private static void run(Socket skt ) {
		//Links the database, maps, and server together?
		Runner worker = new Runner();


		try {

			ObjectInputStream in = new ObjectInputStream(skt.getInputStream()); 	// skt's input stream
			ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());


			//Define at later date
			objecttype readData = null;

			while (true) {

				//Checks if client disconnected and shuts down gracefully
				if (skt.isClosed()) {
					in.close();
					out.close();
					skt.close(); // close all resources
					break;
				}

				readData = (objecttype) in.readObject();

				//wait for readData
				if (readData != null)
					continue;

				//If data is not secure, don't execute, make a security report, and close
				if (worker.checkSecurity(readData.password)) {
					in.close();
					out.close();
					skt.close(); // close all resources
					break;
				}

				//Define at later date, this should return the data to give to app and may be a switch here
				objecttype2 writeData = worker.dostuff(readData);	

				out.writeObject(writeData);
			}



		} 
		// Could be no connection found, security, io, or corruption in stream
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
