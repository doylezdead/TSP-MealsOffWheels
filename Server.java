import java.net.ServerSocket;
import java.net.Socket;


// run thread with new Server().start();


public class Server extends Thread {
	public Server() {
		this.run();
	}

	public void run() {
		robot = new WorkerRobot();
		try {

			System.out.println("Waiting for connections ...");
			ServerSocket srvr = new ServerSocket(13337);
			Socket skt = srvr.accept();
			System.out.print("Client has connected to the server!!!\n");

			ois = new ObjectInputStream(skt.getInputStream()); // skt's
			// input
			// stream

			clientSS = (Dimension) ois.readObject();
			wRatio = serverSS.width / clientSS.width;
			hRatio = serverSS.height / clientSS.height;
			clientSS.setSize(clientSS.width, clientSS.height);

			PrintWriter out = new PrintWriter(skt.getOutputStream(), true); // skt's
			// output
			// stream

			int[] readArray = null;

			while (true) {
				readArray = (int[]) ois.readObject();
				if (readArray[0] == 10)
					break;
				//
				// for (int i = 0; i < readArray.length; i++) {
				// System.out.print(readArray[i] + ", ");
				// }
				// System.out.println();
				robot.work(readArray);
			}

			out.close(); //
			skt.close(); // close all resources
			srvr.close(); //
		} catch (Exception e) {
			e.printStackTrace();// "No connection found\n");
		}
	}
}
