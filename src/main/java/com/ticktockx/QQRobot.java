package com.ticktockx;


import com.ticktockx.socket.Udpsocket;
import com.ticktockx.utils.Util;
import com.ticktockx.sdk.Plugin;
import com.ticktockx.sdk.QQMessage;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QQRobot
{
	private Udpsocket socket = null;
	private QQUser user = null;
	private RobotApi api;
	private String exact_directory;
	List<Plugin> plugins =new ArrayList<Plugin>();

	public QQRobot(Udpsocket _socket, QQUser _user)
	{
		this.socket = _socket;
		this.user = _user;
		this.api = new RobotApi(this.socket, this.user);
		File directory = new File("");
		try
		{
			this.exact_directory  = directory.getCanonicalPath();
			File plugin_path = new File(exact_directory + "/plugin");
			String[] plugin_list = plugin_path.list();
			if (plugin_list != null)
			{
				List<String> list = Arrays.asList(plugin_list);
				for (String file: list)
				{
					if (file.endsWith(".jar"))
					{
						ClassLoader loader = new URLClassLoader(new URL[]{new URL("file://" + this.exact_directory + "/plugin/" + file)});
						Class<?> pluginCls = loader.loadClass("com.robot.com.ticktockx.Main");
						final Plugin plugin = (Plugin)pluginCls.newInstance();
						plugins.add(plugin);
						new Thread(){
							@Override public void run()
							{
								plugin.onLoad(api);
							}
						}.start();
						Util.log("[插件] 加载成功 [插件名]: " + plugin.name());
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}

	}

	public void loadPlugins() {

	}

//	public Plugin loadPlugin(String file){
//
//	}

	public void call(final QQMessage qqmessage)
	{
		for (final Plugin plugin : this.plugins)
		{
			new Thread(){
				public void run()
				{
					plugin.onMessageHandler(qqmessage);

				}
			}.start();
		}
	}

}




