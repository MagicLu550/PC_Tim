package com.ticktockx;



import com.ticktockx.socket.HeartBeat;
import com.ticktockx.socket.LoginManager;
import com.ticktockx.socket.MessageService;
import com.ticktockx.utils.Util;

import java.util.Scanner;

public class Main
{
	public static void main(final String[] args)
	{


		Thread keep_alive = new Thread(){
			QQUser user ;
			LoginManager manager ;
			HeartBeat heartbeat;
			QQRobot robot;
			MessageService messageservice;

			public void run()
			{
				while (true)
				{
					String qq = Util.read_property("account");
					String password = Util.read_property("password");
					byte[] passwordmd5=new byte[0];
					if (this.user == null)
					{
						if(qq==null||password==null){
							Scanner sc = new Scanner(System.in); 
							Util.log("请输入账号：");
							qq = sc.nextLine();
							sc = new Scanner(System.in); 
							Util.log("请输入密码：");
							password = sc.nextLine();
							passwordmd5=Util.MD5(password);
						}else{
							passwordmd5=Util.str_to_byte(password);
						}
						
						this.user = new QQUser(Long.parseLong(qq), passwordmd5);
					}
					if (user.offline)
					{
						this.messageservice.stop();
						this.heartbeat.stop();
					}
					while (!user.islogined)
					{
						this.user = new QQUser(Long.parseLong(qq), passwordmd5);
						this.manager = new LoginManager(this.user);
						this.manager.Login();
						if (user.islogined)
						{
							this.heartbeat = new HeartBeat(this.user, this.manager.socket);
							this.robot = new QQRobot(this.manager.socket, this.user);
							this.messageservice = new MessageService(this.user, this.manager.socket, this.robot);
							this.user.offline = false;
							this.heartbeat.start();
							this.messageservice.start();
							Util.write_property("account",qq);
							Util.write_property("password",Util.byte2HexString(passwordmd5));
							
							break;
						}
					}

					try
					{
						this.sleep(30000);
					}
					catch (InterruptedException e)
					{}
				}
			}
		};
		
		
		keep_alive.start();
	}
}


