package com.ticktockx.socket;



import com.ticktockx.QQUser;
import com.ticktockx.SendPackageFactory;

import java.util.Date;


public class HeartBeat
{
	private QQUser user = null;
	private Thread thread = null;
	private long time_miles = 0;
	private Udpsocket socket = null;
	public HeartBeat(final QQUser _user,Udpsocket _socket){
		
		this.user = _user;
		this.socket = _socket;
		this.thread = new Thread(){
			public void run(){
				while(true){
					try
					{
						byte[] data = SendPackageFactory.get0058(_user);
						time_miles  = new Date().getTime();
						socket.sendMessage(data);
						this.sleep(20000);
					}
					catch (InterruptedException e)
					{
						System.out.println(e.getMessage());
					}
				}
				
			}
		};
		
	}
	
	
	public void start(){
		this.thread.start();
		
	}
	
	public void stop(){
		this.thread.stop();
	}
	
}
