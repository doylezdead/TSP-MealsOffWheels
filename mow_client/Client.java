package mow_client;

import java.net.*  //includes Socket, Object{Input,Output}Stream and more!

class Client{

	skt = new Socket("doyle.pw", 13337);
	oos = new ObjectOutputStream(skt.getOutputStream());
	ois = new ObjectInputStream(skt.getInputStream());
	
	oos.writeObject(new Payload(0, 47.120982, -88.562478, "Ryan Doyle"));

}
