package com.ticktockx;


import com.ticktockx.socket.Udpsocket;
import com.ticktockx.sdk.API;
import com.ticktockx.sdk.MessageFactory;

public class RobotApi implements API {

private Udpsocket socket = null;
	private QQUser user = null;
	 
 public RobotApi(Udpsocket _socket,QQUser _user){
   this.user = _user;
   this.socket = _socket;
 
 }
	@Override
	public void SendGroupMessage(MessageFactory factory){

		SendMessage.SendGroupMessage(this.user,this.socket,factory);

	}
	@Override
	public void SendFriendMessage(MessageFactory factory){

		SendMessage.SendFriendMessage(this.user,this.socket,factory);

	}




}
