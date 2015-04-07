package mow.tests;

import java.net.*;  //includes Socket, Object{Input,Output}Stream and more!
import java.util.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import mow.common.Payload;


class Client{
	public static void main(String[] args){
		
		try{
			Socket skt = new Socket("doyle.pw", 25565);
			ObjectOutputStream oos = new ObjectOutputStream(skt.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(skt.getInputStream());
	
			oos.writeObject(new Payload(1, 1, 47.120982, 88.562478, "Ryan Doyle", "5174047199"));
			
			while(true){
				//waiting for return payload
				Payload ret = null;
				ret = (Payload)ois.readObject();
				if(ret == null){
					continue;
				}
				else{
					//return payload has been found. do stuff with it
					System.out.println("return value is " + ret.value);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("connection fail");
		}
	}
}
