package com.ticktockx.socket;


import com.ticktockx.*;
import com.ticktockx.utils.Util;
import com.ticktockx.sdk.MessageFactory;
import com.ticktockx.sdk.QQMessage;

public class MessageManager
{
	private QQUser user = null;
	private Udpsocket socket;

	private QQRobot robot;
	
	public MessageManager(QQUser _user,Udpsocket _socket,QQRobot _robot){
		this.user = _user;
		this.socket = _socket;
		this.robot = _robot;
	}
	
	public void manage(byte[] data)
	{
		
		final ParseRecivePackage parsereceive = new ParseRecivePackage(data, user.TXProtocol.SessionKey, user);
		Util.log("[接收包] 命令: "+Util.byte2HexString(parsereceive.Command));
		if (Util.GetInt(parsereceive.Command)== 23){
			QQMessage qqmessage = parsereceive.parse0017();
			if (qqmessage != null){
			byte[] data_to_send = SendPackageFactory.get0017(this.user,parsereceive.Message_To_Respone,parsereceive.Sequence);
			this.socket.sendMessage(data_to_send);
			if  (qqmessage != null){

				if (qqmessage.Sender_Uin != 0 && qqmessage.Sender_Uin != user.QQ){
				    if (user.logintime > qqmessage.Send_Message_Time){

						Util.log("[群消息(作废)] 来自群:"+qqmessage.Group_uin+" 的成员: "+qqmessage.SendName+ " [消息] "+qqmessage.Message);
					}else{
						Util.log("[群消息] 来自群:"+qqmessage.Group_uin+" 的成员: "+qqmessage.SendName+ " [消息] "+qqmessage.Message);
						this.robot.call(qqmessage);

					}
				}
			}
			}
		}else if (Util.GetInt(parsereceive.Command)== 88){
			parsereceive.decrypt_body();
			if(parsereceive.body_decrypted[0] != 0){
				user.offline = true;
				user.islogined = false;
				
			}
		}
		else if (Util.GetInt(parsereceive.Command)== 904){
			PictureStore store = null;
			final PictureKeyStore keystore = parsereceive.parse0388();
		if (keystore.uploaded == false){
			new Thread(){
				public void run(){
					PictureStore  new_store = Util.uploadimg(keystore,user,Util.GetInt(parsereceive.Sequence));
				 
			     MessageFactory factory = new MessageFactory();
			     factory.message_type =2;
					factory.Message = new_store.File;
					factory.Group_uin = new_store.Group;
			     byte[] data_to_send = SendPackageFactory.sendpic(user,factory);
			     socket.sendMessage(data_to_send);
		   }
			}.start();
		}else{
			
			
			for(PictureStore onestore: user.imgs){


				if (onestore.pictureid == Util.GetInt(parsereceive.Sequence)){

					store = onestore;
					user.imgs.remove(onestore);

					break;
				}

			}
				MessageFactory factory = new MessageFactory();
				factory.message_type =2;
				factory.Message = store.File;
				factory.Group_uin = store.Group;
				byte[] data_to_send = SendPackageFactory.sendpic(this.user,factory);
				this.socket.sendMessage(data_to_send);
			
		}
			
		}
		else if (Util.GetInt(parsereceive.Command)== 206){
			QQMessage qqmessage = parsereceive.parse00ce();
			byte[] data_to_send = SendPackageFactory.get00ce(this.user,parsereceive.Message_To_Respone,parsereceive.Sequence);
			this.socket.sendMessage(data_to_send);
			data_to_send = SendPackageFactory.get0319(this.user,parsereceive.Friend_Message_QQ,parsereceive.Friend_Message_TIME);
			this.socket.sendMessage(data_to_send);
			if  (qqmessage != null){

				if (qqmessage.Sender_Uin != 0 && qqmessage.Sender_Uin != user.QQ){
				    if (user.logintime > qqmessage.Send_Message_Time){

						Util.log("[好友消息(作废)] 来自好友: "+qqmessage.Sender_Uin+ " [消息] "+qqmessage.Message);
					}else{
						Util.log("[好友消息] 来自好友: "+qqmessage.Sender_Uin+ " [消息] "+qqmessage.Message);
						this.robot.call(qqmessage);
					}
				}
			}
		}
	}
}

