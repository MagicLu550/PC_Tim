package com.ticktockx.utils;



import com.ticktockx.PictureKeyStore;
import com.ticktockx.PictureStore;
import com.ticktockx.QQUser;
import com.ticktockx.sdk.QQMessage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.zip.CRC32;

public class Util
{
	private static Date BaseDateTime = new Date(0);
	
	public static boolean display_verifpic(byte[] data) {
		final String base = "@#&$%*o!;.";// 字符串由复杂到简单
		BufferedImage image = null;
		try
		{
			image = Util.byte_to_img(data);
		}
		catch (IOException e)
		{
			return false;
		}
	
		for (int y = 0; y < image.getHeight(); y += 2) {
			for (int x = 0; x < image.getWidth(); x++) {
				final int pixel = image.getRGB(x, y);
				final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
				final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
				final int index = Math.round(gray * (base.length() + 1) / 255);
				System.out.print(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
			}
			System.out.print("\n");
		}
		
     return true;
	}
	
	
	
	public static BufferedImage byte_to_img(byte[] data) throws IOException{
		InputStream stream = new ByteArrayInputStream(data);
	
			return ImageIO.read(stream);
		
	}
	
	public static BufferedImage  zoomOutImage(BufferedImage  originalImage, float times){

		int width = new Float(originalImage.getWidth()/times).intValue();
		if(width < 0){
			width=originalImage.getWidth();
		}
		int height = new Float(originalImage.getHeight()/times).intValue();
		if(height < 0){
			height=originalImage.getHeight();
		}
		if(times == -1){
			width= 0;
			height =0;
		}
		BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());

		Graphics g = newImage.getGraphics();

		g.drawImage(originalImage, 0,0,width,height,null);

		g.dispose();

		return newImage;

	}
	
	
	public static String read_property(String key){
		File property_file = new File(Util.get_root_path()+"/config/record.conf");
		Properties properties = new Properties();

		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists()){
				property_file.createNewFile();
			}

			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		// 获取key对应的value值
		return properties.getProperty(key);

	}
	public static String read_config(String key){
		File property_file = new File(Util.get_root_path()+"/config/Settings.conf");
		Properties properties = new Properties();

		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists()){
				property_file.createNewFile();
			}

			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		// 获取key对应的value值
		return properties.getProperty(key);

	}
	public static void write_property(String key,String value){
		File property_file = new File(Util.get_root_path()+"/config/record.conf");
		Properties properties = new Properties();
		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists()){
				property_file.createNewFile();
			}
			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(property_file)));

			properties.setProperty(key,value);
			properties.store(bw,value);

		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}

	}

	public static String get_root_path(){
		File directory = new File("");
		String exact_directory = "";
		try
		{
			exact_directory  = directory.getCanonicalPath();

		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return exact_directory;
	}
	
	
	
	
	
	public static void log(String string){
		SimpleDateFormat format0 = new SimpleDateFormat("[HH:mm:ss]");
        String log = format0.format(new Date().getTime());
		System.out.println(log+" "+string);
	}
	
	public static byte[] reverse_byte(byte[] data){
		byte[] Fuck = new byte[data.length];
		for (int time = 0;time < data.length;time++){
			Fuck[time] = data[data.length-time-1];
			
		}
		
		return Fuck;
	}
	
	public static long getfilelength(String file){
		File f= new File(file);
		return f.length();
	}
	
	public static byte[] get_crc32(byte[] data){
		CRC32 crc32 = new CRC32();
		crc32.update(data);
		return reverse_byte(str_to_byte(Long.toHexString(crc32.getValue())));
	}
	public static String getMD5(File file) {
		FileInputStream fileInputStream = null;
		try {
			MessageDigest MD5 = MessageDigest.getInstance("MD5");
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				MD5.update(buffer, 0, length);
			}
			return byte2HexString(MD5.digest()).replace(" ","");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fileInputStream != null){
					fileInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String GetMD5HashFromFile(String fileName)
	{
		
			File file = new File(fileName);
			
		return getMD5(file);
	}
	
	public static String GetMD5ToGuidHashFromFile(String fileName)
	{
		String md5 = GetMD5HashFromFile(fileName);
		return md5.substring(0, 8)+"-"+md5.substring(8, 12)+"-"+md5.substring(12, 16)+"-"+md5.substring(16, 20)+"-"+md5.substring(20,md5.length());
	}
	
	
	private static byte[] toByteArray(InputStream in) throws IOException {
 
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024 * 4];
    int n = 0;
    while ((n = in.read(buffer)) != -1) {
        out.write(buffer, 0, n);
    }
    return out.toByteArray();
}

	public static String http_dns(String host)
	{  
		try
		{  
			URL lll = new URL("http://119.29.29.29/d?dn="+host);
			HttpURLConnection connection = (HttpURLConnection) lll.openConnection();// 打开连接  
			connection.setRequestMethod("GET");
			connection.connect();// 连接会话  
			// 获取输入流  
			BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  
			String line;  
			StringBuilder sb = new StringBuilder();  
			while ((line = br.readLine()) != null)
			{// 循环读取流  
				sb.append(line);  
			}  
			br.close();// 关闭流
			connection.disconnect();// 断开连接  
			String hosts = sb.toString();
			if(hosts.contains(";")){
				return hosts.split(";")[0];
			}else{
				return hosts;
			}
		}
		catch (Exception e)
		{  
			System.out.println(e.toString());
		}
		return null;
	}


	public static PictureStore uploadimg(PictureKeyStore keystore, QQUser user, int pictureid)
	{
		PictureStore store = null;

		for(PictureStore onestore: user.imgs){
			
			
			if (onestore.pictureid == pictureid){
				
				store = onestore;
				user.imgs.remove(onestore);
				
				break;
			}
			
		}

	 
		String file = store.File;
	    URL u = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        //尝试发送请求
        try {
             u = new URL("http://"+Util.http_dns("htdata2.qq.com")+"/cgi-bin/httpconn?htcmd=0x6ff0071&ver=5515&term=pc&ukey=" +Util.byte2HexString(keystore.ukey).replace(" ","")+ "&filesize=" +getfilelength(file)+ "&range=0&uin=" + user.QQ+"&groupcode=" + store.Group);
          con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "binary/octet-stream");
            con.setRequestProperty("User-Agent", "QQClient");
            OutputStream outStream = con.getOutputStream();
			
			DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] bufferOut = new byte[1024];
			int bytes = 0;
			// 每次读1KB数据,并且将文件数据写入到输出流中
			while ((bytes = in.read(bufferOut)) != -1) {
				outStream.write(bufferOut, 0, bytes);
			}
            outStream.flush();
            outStream.close();
            //读取返回内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;

			while ((line = reader.readLine()) != null) {

				System.out.println(line);

			}
			
 
        } catch (Exception e) {
            e.printStackTrace();
        } 
		
		return store;
		
	}
	
	public static String Length_toPB(String d)
	{
		String binary = Long.toString(GetQQNumRetUint(d), 2);
		String temp = "";
		while (!(binary).isEmpty())
		{
			temp = temp + binary.substring(binary.length() - 7,binary.length() );
			if (binary.length() >= 8)
			{
				binary = binary.substring(0, binary.length() - 8);
			}
			else
			{
				break;
			}
		}

		return Long.toHexString(Long.parseLong(temp, 2));
	}
	
	public static long GetQQNumRetUint(String six)
	{
		return Long.parseLong(six.replace(" ", ""), 16);
	}
	
	public static String PB_toLength(long d)
	{
		String binary = Long.toString(d, 2); //转换length为二级制
		String temp = "";
		while (!(binary.isEmpty()||binary==null))
		{
			String binary1 = "0000000" + binary;
			temp = temp + "1" + binary1.substring(binary1.length() - 7,binary1.length());
			if (binary.length() >= 7)
			{
				binary = binary.substring(0, (binary.length() - 7));
			}
			else
			{
				//temp = temp + "0" + binary;
				break;
			}
		}
		String temp1 = temp.substring(temp.length() - 7, temp.length());
		temp = temp.substring(0, temp.length() - 8) + "0" + temp1;

		return Long.toHexString(Long.parseLong(temp, 2));
	}
	
	public static byte[] Bufferedimg_tobytes(BufferedImage img,String type)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try
		{
		ImageIO.write( img, type, baos );
	
			baos.flush();
		}
		catch (IOException e)
		{
		 e.printStackTrace();
		}
		byte[] imageInByte = baos.toByteArray();
		try
		{
			baos.close();
		}
		catch (IOException e)
		{
		 e.printStackTrace();
		}
		return imageInByte;
	}

	public static BufferedImage get_img(String file_name){
		BufferedImage img_to_send = null;
		File directory = new File("");
		String exact_directory = "";
		
		
		try
		{
			exact_directory  = directory.getCanonicalPath();
			img_to_send = ImageIO.read(new File(file_name));
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		return img_to_send;
	}

	public static byte[] constructxmlmessage(QQUser user, byte[] data)
	{
		
		ByteBuilder builder = new ByteBuilder();
		builder.writebytes(Util.RandomKey(4));
		builder.writebytes(Util.str_to_byte("0000000009008600"));
		builder.writebytes(new byte[]{0x00,0x0c});
		builder.writebytes(Util.str_to_byte("E5BEAEE8BDAFE99B85E9BB91"));
		builder.writebytes(new byte[] { 0x00, 0x00, 0x14 });
		builder.writeint(data.length + 11);
		builder.writebyte((byte) 0x01);
		builder.writeint(data.length + 1);
		builder.writebyte((byte) 0x01);
		builder.writebytes(data);
		builder.writebytes(new byte[] { 0x02, 0x00, 0x04, 0x00, 0x00, 0x00, 0x4D });
		return builder.getdata();
	}

	public static byte[] constructmessage(QQUser user, byte[] data)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writebyte((byte)0x01);
		builder.writeint((data.length + 3));
		builder.writebyte((byte)0x01);
		builder.writeint(data.length);
		builder.writebytes(data);
		
		return builder.getdata();
	}
	public static long ConvertQQGroupId(long code)
	{
		String group = String.valueOf(code);
		long left = Long.parseLong(group.substring(0, group.length() - 6));
		
		String right = "", gid = "";
		if (left >= 1 && left <= 10)
		{
			right = group.substring(group.length() - 6, group.length());
			gid = String.valueOf(left + 202) + right;
		}
		else if (left >= 11 && left <= 19)
		{
			right = group.substring(group.length() - 6, group.length());
			gid = String.valueOf(left + 469)+ right;
		}
		else if (left >= 20 && left <= 66)
		{
			left = Long.parseLong(String.valueOf(left).substring(0, 1));
			right = group.substring(group.length() - 7, group.length());
			gid = String.valueOf(left + 208) + right;
		}
		else if (left >= 67 && left <= 156)
		{
			right = group.substring(group.length() - 6, group.length());
			gid = String.valueOf(left + 1943) + right;
		}
		else if (left >= 157 && left <= 209)
		{
			left = Long.parseLong(String.valueOf(left).substring(0, 2));
			right = group.substring(group.length() - 7, group.length());
			gid = String.valueOf(left + 199) + right;
		}
		else if (left >= 210 && left <= 309)
		{
			
			
			left = Long.parseLong(String.valueOf(left).substring(0, 2));
			
			right = group.substring(group.length() - 7, group.length());
			
			gid = String.valueOf(left + 389) + right;
			
		
		}
		else if (left >= 310 && left <= 499)
		{
			left = Long.parseLong(String.valueOf(left).substring(0, 2));
			right = group.substring(group.length() - 7, group.length());
			gid = String.valueOf(left + 349)+ right;
		}
		else
		{
			return code;
		}

		return Long.parseLong(gid);
	}
	
	
	public static void parseRichText(QQMessage qqmessage, byte[] rich_data)
	{
		ByteFactory bytefactory = new ByteFactory(rich_data);
		int messagetype = bytefactory.readBytes(1)[0];

		int messagelength = bytefactory.readint();
		int position = bytefactory.position;
		
		while (position + messagelength <= bytefactory.data.length)
		{
			bytefactory.readBytes(1);
			
			
			switch (messagetype)
			{
				case 0x01: // 纯文本消息、@
					{
						qqmessage.contain_type = 1;
						String messageStr = bytefactory.readStringbylength();
						if (messageStr.startsWith("@") && position + messagelength - bytefactory.position == 16)
						{
							if (qqmessage.Isat == false){
								qqmessage.Isat = true;
							}
							bytefactory.readBytes(10);
							qqmessage.Atlist.add(messageStr+" Target: "+bytefactory.readlong());

						}
						else
						{
							qqmessage.Message+=messageStr;
						}

						break;
					}
				case 0x03: // 图片
					{
						qqmessage.contain_type = 2;
						qqmessage.Message+= bytefactory.readStringbylength();
						break;
					}
				case 0x0A: // 音频
					{
						qqmessage.contain_type = 3;
						qqmessage.Message+= bytefactory.readBytesbylength();
						break;
					}
				case 0x0E: // 未知
					{
						break;
					}
				case 0x12: // 群名片
					{
						ByteFactory cardfactory = new ByteFactory(Util.subByte(rich_data,position,rich_data.length-position));
						int cardtype = cardfactory.readBytes(1)[0];
						
						int cardlength = cardfactory.readint();
						int cardposition = cardfactory.position;

						while (cardposition + cardlength <= cardfactory.data.length)
						{
							
							
							if (cardtype == 0x01||cardtype == 0x02){
								qqmessage.SendName =cardfactory.readString(cardlength);
							}
							
							if (cardposition +cardlength == cardfactory.data.length){
								break;
							}
							cardfactory.readBytes( (cardposition + cardlength - cardfactory.position));

							cardtype = cardfactory.readBytes(1)[0];
							cardlength = cardfactory.readint();
							cardposition = cardfactory.position;
						}
						
					
						break;
					}
				case 0x14: // xml
					{
					    qqmessage.contain_type = 1;
						ByteFactory xmlfactory = new ByteFactory(Util.subByte(rich_data,position,rich_data.length-position));
						xmlfactory.readBytes(1);
						int length = xmlfactory.readint();
						xmlfactory.readBytes(1);
						byte[] xml = xmlfactory.readBytes(length);
						qqmessage.Message = new String(ZLibUtils.decompress(xml));
						
						break;
						
					}
				case 0x18: // 群文件
					{
						System.out.println("Fuck");
						break;
					}
				case 0x19: // 红包
					{
						
						break;
						
					}
				default:
					{
						break;
					}
			}
			
			if (position +messagelength == bytefactory.data.length){
			 break;
			}
			bytefactory.readBytes( (position + messagelength - bytefactory.position));
			
			messagetype = bytefactory.readBytes(1)[0];
			messagelength = bytefactory.readint();
			position = bytefactory.position;
		
		}


	}
	
	
	
	public static String getHostIP() {
		
		String hostIp = null;
		try {
			Enumeration nis = NetworkInterface.getNetworkInterfaces();
			InetAddress ia = null;
			while (nis.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					ia = ias.nextElement();
					if (ia instanceof Inet6Address) {
						continue;// skip ipv6
					}
					String ip = ia.getHostAddress();
					if (!"127.0.0.1".equals(ip)) {
						hostIp = ia.getHostAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			
			e.printStackTrace();
		}
		return hostIp;
	}
	public static long GetTimeSeconds(Date dateTime)
	{
		return (long) dateTime.getTime() - BaseDateTime.getTime()/1000;
	}
	
	
	public static byte[] GetQdData(QQUser user)
	{
		byte[] data = new byte[]{};
		data = Util.byteMerger(data,Util.IPStringToByteArray(user.TXProtocol.DwServerIP));
		byte[] qddata = new byte[]{};
		qddata=Util.byteMerger(qddata,user.TXProtocol.DwQdVerion_Byte);
		qddata=Util.byteMerger(qddata,new byte[]{0x00,0x00,0x00,0x00});
		qddata=Util.byteMerger(qddata,user.TXProtocol.DwPubNo);
		qddata=Util.byteMerger(qddata,Util.subByte(Util.ToByte(user.QQ),4,4));
		qddata=Util.byteMerger(qddata,Util.subByte(Util.ToByte(data.length),2,2));
		
		data = new byte[]{};
		data=Util.byteMerger(data,user.TXProtocol.QdPreFix);
		data=Util.byteMerger(data,user.TXProtocol.CQdProtocolVer_Byte);
		data=Util.byteMerger(data,user.TXProtocol.DwQdVerion_Byte);
		data=Util.byteMerger(data,new byte[]{0x00});
		data=Util.byteMerger(data,user.TXProtocol.WQdCsCmdNo_Byte);
		data=Util.byteMerger(data,user.TXProtocol.CQdCcSubNo);
		data=Util.byteMerger(data,Util.str_to_byte("0E88"));
		data=Util.byteMerger(data,new byte[]{0x00,0x00,0x00,0x00});
		data=Util.byteMerger(data,user.TXProtocol.BufComputerIdEx);
		data=Util.byteMerger(data,user.TXProtocol.COsType);
		data=Util.byteMerger(data,user.TXProtocol.BIsWow64);
		data=Util.byteMerger(data,user.TXProtocol.DwPubNo);
		data=Util.byteMerger(data,Util.subByte(user.TXProtocol.DwClientVer,2,2));
		data=Util.byteMerger(data,new byte[]{0x00,0x00});
		data=Util.byteMerger(data,user.TXProtocol.DwDrvVersionInfo);
		data=Util.byteMerger(data,new byte[]{0x00,0x00,0x00,0x00});
		data=Util.byteMerger(data,user.TXProtocol.BufVersionTsSafeEditDat);
		data=Util.byteMerger(data,user.TXProtocol.BufVersionQScanEngineDll);
		data=Util.byteMerger(data,new byte[]{0x00});
		Crypter crypter = new Crypter();
		data=Util.byteMerger(data,crypter.encrypt(qddata,user.TXProtocol.BufQdKey));
		data=Util.byteMerger(data,user.TXProtocol.QdSufFix);
		
		int size = data.length + 3;
		qddata = new byte[]{};
		qddata=Util.byteMerger(qddata,user.TXProtocol.QdPreFix);
		qddata=Util.byteMerger(qddata,Util.subByte(Util.ToByte(size),2,6));
		qddata=Util.byteMerger(qddata,new byte[]{0x00,0x00});
		qddata=Util.byteMerger(qddata,Util.subByte(Util.ToByte(data.length),2,6));
		qddata=Util.byteMerger(qddata,new byte[]{0x00,0x00});
		
		user.TXProtocol.QdData=data;
		return data;
	}


	public static byte[] random_byte(int size)
	{
		byte [] b=new byte[size];
		Random random=new Random();
		random.nextBytes(b);
		
		return b;
	}

	
	public static short bytesToshort(byte[] input)
	{
		byte high = input[0];
        byte low = input[1];
        short z = (short)(((high & 0x00FF) << 8) | (0x00FF & low));
        return z;

   
		// TODO: Implement this metho
	}

	public static String GetIpStringFromBytes(byte[] input)
	{
		String u1 = String.valueOf((int)input[0]& 0xff);
		String u2 = String.valueOf((int)input[1]& 0xff);
		String u3 = String.valueOf((int)input[2]& 0xff);
		String u4 = String.valueOf((int)input[3]& 0xff);
		return u1 +"." + u2 +"." +u3 +"."+u4;
	}

	public static long bytesToLong(byte[] input, int offset, boolean littleEndian) {
// 将byte[] 封装为 ByteBuffer

		ByteBuffer buffer = ByteBuffer.wrap(Util.byteMerger(new byte[8-input.length],input));   

		if(littleEndian){            
			// ByteBuffer.order(ByteOrder) 方法指定字节序,即大小端模式(BIG_ENDIAN/LITTLE_ENDIAN)
			// ByteBuffer 默认为大端(BIG_ENDIAN)模式
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		}
		
		return buffer.getLong();
	}
	
	public static int GetInt(byte[] data, int offset, int length)
	{
        byte[] test = new byte[]{0x00,0x00,data[offset],data[offset+1],0x00,0x00,0x00,0x00};
		ByteBuffer u = ByteBuffer.wrap(test);
		
		return u.getInt();
	}
	
	public static int GetInt(byte[] data)
	{
        byte[] test = new byte[]{0x00,0x00,data[0],data[1],0x00,0x00,0x00,0x00};

		ByteBuffer u = ByteBuffer.wrap(test);

		return u.getInt();
	}
	
	public static long GetLong(byte[] data)
	{
        byte[] test = new byte[]{0x00,0x00,0x00,0x00,data[0],data[1],data[2],data[3]};

		ByteBuffer u = ByteBuffer.wrap(test);

		return u.getLong();
	}
	
	public static short GetShort(byte[] data)
	{
        byte[] test = new byte[]{data[0],data[1],0x00,0x00,0x00,0x00,0x00,0x00};

		ByteBuffer u = ByteBuffer.wrap(test);

		return u.getShort();
	}
	
	public static Date GetDateTimeFromMillis(long timeMillis)
	{
		Date date = new Date(timeMillis);
		return date;
	}

	public static byte[] GetBytes(String string)
	{
		
		
		// TODO: Implement this method
		return string.getBytes();
	}
	
	
	public static String GET_GTK(String skey)
	{
		String arg = "tencentQQVIP123443safde&!%^%1282";
		List<Integer> list = new ArrayList<Integer>();
		int num = 5381;
		list.add(172192);
		int i = 0;
		for (int length = skey.length(); i < length; i++)
		{
			int num2 = (skey).charAt(i);
			list.add((num << 5) + num2);
			num = num2;
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (i = 0; i < list.size(); i++)
		{
			stringBuilder.append(list.get(i).toString());
		}

		return Md5(stringBuilder + arg);
	}
	public static String Md5(String text)
	{
		
		try
		{
		MessageDigest md = MessageDigest.getInstance("MD5");
		
        byte[] bytes = md.digest(text.getBytes());
		String result = "";
		for (byte b : bytes)
		{
			result += String.format("%02x", b);
		}

		return result;
		}
		catch (NoSuchAlgorithmException e)
		{}
		return null;
	}
	
	public static byte[] MD5(String arg)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
		

        byte[] bytes = md.digest(arg.getBytes());
		return bytes;
		}
		catch (NoSuchAlgorithmException e)
		{}
		return null;
	}
	public static byte[] MD5(byte[] arg)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");


			byte[] bytes = md.digest(arg);
			return bytes;
		}
		catch (NoSuchAlgorithmException e)
		{}
		return null;
	}
	public static String GetBkn(String skey)
	{
		int num = 5381;
		for (int i = 0; i <= skey.length() - 1; i++)
		{
			num += (num << 5) + Integer.parseInt(skey.substring(i, i + 1));

		}

		return String.valueOf((num & 0x7FFFFFFF));
	}
	
	public static String ToHex(byte[] data, String p1, String p2)
	{
		String hex= "";
        if (data != null) {
            for (Byte b : data) {
                hex += String.format("%02X", b.intValue() & 0xFF);
            }
        }
        return hex;
	}

	
	public static String NumToHexString(int qq, int Length)
	{
	
		String text = String.valueOf(qq);
		if (text.length() == Length)
		{
			return text;
		}

		if (text.length() > Length)
		{
			return null;
		}
		return null;
	}

	public static byte[] byteMerger(List<byte[]> bytes)
	{
		int total_length = 0;
		int offset= 0;
		for (byte[]  one : bytes){
			total_length = total_length + one.length;
		}
		byte[] byte_3 = new byte[total_length];
		
		for (byte[] one_byte : bytes){
			System.arraycopy(one_byte, 0, byte_3, offset, one_byte.length); 
            offset= offset + one_byte.length;
		}
          return byte_3;
	}

	
	public static byte[] IPStringToByteArray(String ip)
	{
		byte[] array = new byte[4];
		String[] array2 = ip.split("[.]");
		if (array2.length == 4)
		{
			for (int i = 0; i < 4; i++)
			{
				array[i] = (byte) Integer.parseInt(array2[i]);
			}
		}

		return array;
	}
	
	

	public static byte[] RandomKey()
	{
		byte[] key = new byte[16];
		new Random().nextBytes(key);
		return key;
	}
	public static byte[] RandomKey(int size)
	{
		byte[] key = new byte[size];
		new Random().nextBytes(key);
		return key;
	}
	
	public static byte[] str_to_byte(String str) {
        String replaceAll = str.replaceAll(" ", "");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(replaceAll.length() >> 1);
        for (int i = 0; i <= replaceAll.length() - 2; i += 2) {
            byteArrayOutputStream.write(Integer.parseInt(replaceAll.substring(i, i + 2), 16) & 255);
        }
        return byteArrayOutputStream.toByteArray();
    }
	
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        return byte_3;  
    }
	
	public static byte[] ToByte(long x) {  
		ByteBuffer buffer = ByteBuffer.allocate(8);

		buffer.putLong(x);
		return  buffer.array();  
	}
	public static byte[] ToByte(int x) {  
        ByteBuffer buffer = ByteBuffer.allocate(8);

		buffer.putInt(x);
		return  buffer.array();  
    }
	public static byte[] ToByte(short x) {  
        ByteBuffer buffer = ByteBuffer.allocate(8);

		buffer.putShort(x);
		return  buffer.array();  
    }
	public static String byte2HexString(byte[] bytes) {
        String hex= "";
        if (bytes != null) {
            for (Byte b : bytes) {
                hex += String.format("%02X", b.intValue() & 0xFF)+" ";
            }
        }
        return hex;

    }
	
	public static byte[] subByte(byte[] b,int off,int length){
		
			if (b.length != 0 && b != null){
				byte[] b1 = new byte[length];
				System.arraycopy(b, off, b1, 0, length);
				return b1;
			}
			return new byte[1];
		
		
	}
	
}
