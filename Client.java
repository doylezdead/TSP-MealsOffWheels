import java.net.Socket;

skt = new Socket("doyle.pw", 13337);
oos = new ObjectOutputStream(skt.getOutputStream());
oos.writeObject(arbitrary object here);
