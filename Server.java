import java.net.ServerSocket;
import java.net.Socket;


// run thread with new Server().start();


public class Server extends Thread {
	public Server() {
		this.run();
	}

	public void run() {
		Runner worker = new Runner();
		
		ServerSocket srvr = new ServerSocket(13337);
		
		try {

			System.out.println("Waiting for connections ...");
			Socket skt = srvr.accept(); //I think you can set timeout values for this
			System.out.print("Client has connected to the server!!!\n");

			ObjectInputStream ois = new ObjectInputStream(skt.getInputStream()); // skt's
			// input
			// stream
		

			objecttype readData = null;

			while (true) {
				readData = (objecttype) ois.readObject();
				//wait for readData
				if (readData != null)
					//sleep time here
					continue;
				
				//execute code here based off read data.
				worker.dostuff(readData.values);	
			}

			ois.close();
			skt.close(); // close all resources
		
		} catch (Exception e) {
			e.printStackTrace();// "No connection found\n");
		}

		srvr.close(); //
	}
}
