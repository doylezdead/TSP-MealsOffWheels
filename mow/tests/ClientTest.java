package mow.tests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Test;

public class ClientTest {

	@Test
	public void testClientToServer(){
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try{
			//Socket(serverip,port)
			Socket skt = new Socket("127.0.0.1", 8998);
			oos = new ObjectOutputStream(skt.getOutputStream());
			ois = new ObjectInputStream(skt.getInputStream());
			
			//send Payload instance to server
			oos.writeObject(new Payload(1, 1, 47.120982, 88.562478, "Ryan Doyle", "5175057199"));
			
			// Monitor and receive the information returned by the server
			while(true){
				try {
					//waiting for server return payload
					Payload ret = null;
					ret = (Payload)ois.readObject();
					if(ret != null){
						//return payload has been found. do stuff with it
						System.out.println("return value is " + ret.value);
					}else {
						System.out.println("Connect to server fail!Check please! ");
						break;
					}
				} catch (Exception e) {
					//invalidate messsage from server ,not Payload instance?
					System.out.println("The server returned an invalid information!Check your server code please!");
					e.printStackTrace();
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Connect to server fail!");
			e.printStackTrace();
		} finally{
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					System.out.println("close ObjectOutputStream error!");
					e.printStackTrace();
				}
			}
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					System.out.println("close ObjectInputStream error!");
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Test
	public void testCheckDeliverablity(){
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try{
			//Socket(serverip,port)
			Socket skt = new Socket("127.0.0.1", 8998);
			oos = new ObjectOutputStream(skt.getOutputStream());
			ois = new ObjectInputStream(skt.getInputStream());
			
			//send Payload instance to server
			oos.writeObject(new Payload(0, 0, 47.120982, 88.562478, "Ryan Doyle", "5175057199"));
			//oos.writeObject(new Payload(0, 0, 47.120982, 88.562478, "Test0", "5175057199"));
			//oos.writeObject(new Payload(0, 0, 47.120982, 88.562478, "Test1", "5175057199"));
			//oos.writeObject(new Payload(0, 0, 47.120982, 88.562478, "Test2", "5175057199"));
			
			// Monitor and receive the information returned by the server
			while(true){
				try {
					//waiting for server return payload
					Payload ret = null;
					ret = (Payload)ois.readObject();
					if(ret != null){
						//return payload has been found. do stuff with it
						System.out.println("return storeID is " + ret.value);
						Assert.assertTrue("return storeID is negative value!", (ret.value > 0));
					}else {
						System.out.println("Connect to server fail!Check please! ");
						break;
					}
				} catch (Exception e) {
					//invalidate messsage from server ,not Payload instance?
					System.out.println("The server returned an invalid information!Check your server code please!");
					e.printStackTrace();
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Connect to server fail!");
			e.printStackTrace();
		} finally{
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					System.out.println("close ObjectOutputStream error!");
					e.printStackTrace();
				}
			}
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					System.out.println("close ObjectInputStream error!");
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Test
	public void testOrderID(){
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try{
			//Socket(serverip,port)
			Socket skt = new Socket("127.0.0.1", 8998);
			oos = new ObjectOutputStream(skt.getOutputStream());
			ois = new ObjectInputStream(skt.getInputStream());
			
			//send Payload instance to server
			Payload p = new Payload(1, 1, 47.120982, 88.562478, "Ryan Doyle", "5175057199");
			//Payload p = new Payload(2, 1, 47.120982, 88.562478, "Ryan Doyle", "5175057199");
			//Payload p = new Payload(3, 1, 47.120982, 88.562478, "Ryan Doyle", "5175057199");
			//Payload p = new Payload(4, 1, 47.120982, 88.562478, "Ryan Doyle", "5175057199");
			//Payload p = new Payload(5, 1, 47.120982, 88.562478, "Ryan Doyle", "5175057199");
			
			p.weight = 1;
			oos.writeObject(p);
			
			// Monitor and receive the information returned by the server
			while(true){
				try {
					//waiting for server return payload
					Payload ret = null;
					ret = (Payload)ois.readObject();
					if(ret != null){
						//return payload has been found. do stuff with it
						System.out.println("return orderID is " + ret.value);
						Assert.assertTrue("fail to place order!", (ret.value > 0));
					}else {
						System.out.println("Connect to server fail!Check please! ");
						break;
					}
				} catch (Exception e) {
					//invalidate messsage from server ,not Payload instance?
					System.out.println("The server returned an invalid information!Check your server code please!");
					e.printStackTrace();
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Connect to server fail!");
			e.printStackTrace();
		} finally{
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					System.out.println("close ObjectOutputStream error!");
					e.printStackTrace();
				}
			}
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					System.out.println("close ObjectInputStream error!");
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Test
	public void testOrderStatus(){
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try{
			//Socket(serverip,port)
			Socket skt = new Socket("127.0.0.1", 8998);
			oos = new ObjectOutputStream(skt.getOutputStream());
			ois = new ObjectInputStream(skt.getInputStream());
			
			//send Payload instance to server
			oos.writeObject(new Payload(1, 2, 47.120982, 88.562478, "Ryan Doyle", "5175057199"));
			//oos.writeObject(new Payload(2, 2, 47.120982, 88.562478, "Ryan Doyle", "5175057199"));
			//oos.writeObject(new Payload(3, 2, 47.120982, 88.562478, "Ryan Doyle", "5175057199"));
			//oos.writeObject(new Payload(4, 2, 47.120982, 88.562478, "Ryan Doyle", "5175057199"));
			//oos.writeObject(new Payload(5, 2, 47.120982, 88.562478, "Ryan Doyle", "5175057199"));
			//oos.writeObject(new Payload(6, 2, 47.120982, 88.562478, "Ryan Doyle", "5175057199"));
			
			// Monitor and receive the information returned by the server
			while(true){
				try {
					//waiting for server return payload
					Payload ret = null;
					ret = (Payload)ois.readObject();
					if(ret != null){
						//return payload has been found. do stuff with it
						System.out.println("return value is " + ret.value);
						Assert.assertTrue("order status is not good!", (ret.value != 0));
					}else {
						System.out.println("Connect to server fail!Check please! ");
						break;
					}
				} catch (Exception e) {
					//invalidate messsage from server ,not Payload instance?
					System.out.println("The server returned an invalid information!Check your server code please!");
					e.printStackTrace();
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Connect to server fail!");
			e.printStackTrace();
		} finally{
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					System.out.println("close ObjectOutputStream error!");
					e.printStackTrace();
				}
			}
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					System.out.println("close ObjectInputStream error!");
					e.printStackTrace();
				}
			}
		}
	}
	
	
}

