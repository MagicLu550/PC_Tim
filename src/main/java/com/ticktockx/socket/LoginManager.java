package com.ticktockx.socket;



import com.ticktockx.ParseRecivePackage;
import com.ticktockx.QQGlobal;
import com.ticktockx.QQUser;
import com.ticktockx.SendPackageFactory;
import com.ticktockx.utils.Util;

import java.util.Date;
import java.util.Scanner;

public class LoginManager
{
	private byte[] data = null;
	public Udpsocket socket = null;
	private QQUser _user;
	public LoginManager(QQUser user){
		this._user = user;
		user.TXProtocol.DwServerIP= "sz.tencent.com";
		socket = new Udpsocket(user);
		
		
		
	}
	
	public void relogin(){
		data = SendPackageFactory.get0825(_user);
		socket.sendMessage(data);
	}
	
	
	public void Login(){
		try
		{
		data = SendPackageFactory.get0825(_user);
		socket.sendMessage(data);
		byte[] result = socket.receiveMessage();
		//System.out.println(Util.byte2HexString(result));
		ParseRecivePackage parsereceive = new ParseRecivePackage(result,_user.QQPacket0825Key,_user);
		parsereceive.decrypt_body();
		parsereceive.parse_tlv();
		while(parsereceive.Header[0] == -2){
			Util.log("重定向到:"+_user.TXProtocol.DwRedirectIP);
			_user.TXProtocol.WRedirectCount += 1;
			_user.IsLoginRedirect = true;
			socket = new Udpsocket(_user);
			
			data = SendPackageFactory.get0825(_user);
			
			socket.sendMessage(data);
			
			result = socket.receiveMessage();
			parsereceive = new ParseRecivePackage(result,_user.QQPacket0825Key,_user);
			parsereceive.decrypt_body();
			parsereceive.parse_tlv();
		}
		
		Util.log("服务器连接成功,开始登陆");
		data = SendPackageFactory.get0836(_user,false);
		socket.sendMessage(data);
		result = socket.receiveMessage();
		parsereceive = new ParseRecivePackage(result,_user.TXProtocol.BufDhShareKey,_user);
		parsereceive.parse0836();
		if (parsereceive.Header[0] == 52){
			Util.log("密码错误");
			System.exit(100);
		}
			if (parsereceive.Header[0] == -5){
				Util.log("需要验证码");
				while(parsereceive.Status==0x1){
					while(true){//死循环获取验证码
						data = SendPackageFactory.get00ba(_user,"");
						socket.sendMessage(data);
						result = socket.receiveMessage();
						parsereceive = new ParseRecivePackage(result,_user.QQPacket00BaKey,_user);
						parsereceive.parse00ba();
						if(Util.display_verifpic(_user.QQPacket00BaVerifyCode)){
							break;//当验证码能够被显示时跳出循环
						}
					}
					//此时验证码已获取，开始输入验证码
					Scanner sc = new Scanner(System.in); 
					String code =sc.nextLine();
					if(code==null||code.isEmpty()||code.equals("")){
						code ="TICK";
					}
					data = SendPackageFactory.get00ba(_user,code);
					socket.sendMessage(data);
					result = socket.receiveMessage();
					parsereceive = new ParseRecivePackage(result,_user.QQPacket00BaKey,_user);
					parsereceive.parse00ba();
					if(parsereceive.Status!=0x0){
						return;//验证失败结束方法，成功则继续执行
					}
					
				}
				
				
			}	
		
			
		while (parsereceive.Header[0] != 0){
			Util.log("二次登陆");
			data = SendPackageFactory.get0836(_user,true);
			socket.sendMessage(data);
			result = socket.receiveMessage();
			parsereceive = new ParseRecivePackage(result,_user.TXProtocol.BufDhShareKey,_user);
			parsereceive.parse0836();
			
				Thread.sleep(1000);
			
		}
		if (parsereceive.Header[0] == 0){
			Util.log("成功获取用户信息: Nick: "+_user.NickName+" Age: "+_user.Age+" Sex: "+_user.Gender);
			_user.islogined = true;
			_user.logintime = new Date().getTime();
			data = SendPackageFactory.get0828(_user);
			socket.sendMessage(data);
	
			result = socket.receiveMessage();
			parsereceive = new ParseRecivePackage(result, _user.TXProtocol.BufTgtGtKey, _user);
			parsereceive.decrypt_body();
			parsereceive.parse_tlv();
	
			data = SendPackageFactory.get00ec(_user, QQGlobal.Online);
			socket.sendMessage(data);

			result = socket.receiveMessage();
			parsereceive = new ParseRecivePackage(result, _user.TXProtocol.SessionKey, _user);
			parsereceive.decrypt_body();
			
			
		}
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
