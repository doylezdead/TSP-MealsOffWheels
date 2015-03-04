package mow.client;

import java.net.*;  //includes Socket, Object{Input,Output}Stream and more!
import java.util.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import mow.common.Payload;


class Client{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		System.out.print("Enter an integer to double: ");
		int sval = in.nextInt();
		
		try{
			Socket skt = new Socket("doyle.pw", 25565);
			ObjectOutputStream oos = new ObjectOutputStream(skt.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(skt.getInputStream());
	
			oos.writeObject(new Payload(sval, 47.120982, 88.562478, "Ryan Doyle"));
			
			while(true){
				Payload ret = null;
				ret = (Payload)ois.readObject();
				if(ret == null){
					continue;
				}
				else{
					System.out.println("return value is " + ret.value);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("fail");
		}
	}
}
