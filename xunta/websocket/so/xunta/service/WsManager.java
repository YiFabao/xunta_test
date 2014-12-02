package so.xunta.service;

import java.io.IOException;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.catalina.websocket.MessageInbound;
import org.json.JSONException;
import org.json.JSONObject;

import so.xunta.ipseeker.IPSeeker;
import so.xunta.localcontext.LocalContext;
import so.xunta.utils.TextUtil;
import so.xunta.websocket.servlet.MyWebsocket;


public class WsManager {
	
	public static boolean flag=false;
	
	private WsManager(){};
	
	private static WsManager instance=new WsManager();
	
	
	//读取文本值
	public static List<JSONObject> searchList=TextUtil.readFileMethod();//ip searchKey
	public static List<JSONObject> search_history=new ArrayList<JSONObject>(20);//ip searchKey time
	
	public static IPSeeker ipseeker=new IPSeeker("QQWry.dat",LocalContext.INSTALL_DIR);

	public static WsManager getInstance()
	{
		return instance;
	}
	

	/**
	 * 发消息的方法
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	public synchronized void broadcast(JSONObject json)
	{
		
		System.out.println("广播方法里："+json);

		try {
			System.out.println("添加前："+WsManager.search_history.size());
			WsManager.search_history.add(json);
			System.out.println("添加后:"+WsManager.search_history.size());
			if(WsManager.search_history.size()>20)
			{
				WsManager.search_history.remove(0);
			}

			System.out.println("准备for");
			for(MessageInbound ws:WebSocketQueue.getInstance().wslist)
			{
				System.out.println("打印："+ws);
				
					
					MyWebsocket mw=(MyWebsocket)ws;
					System.out.println(mw);
					
					
					mw.getWsOutbound().writeTextMessage(CharBuffer.wrap(json.toString()));
					mw.print();
			}
		} catch (IOException e) {
			System.out.println("发送消息出错："+e.getMessage());
		} 
	}
	
	/**
	 * 循环发消息
	 */
	public void broadcastWhenTrue()
	{
		
		while(flag)
		{
			int h=Calendar.getInstance().get(Calendar.HOUR);
			int f=30;
			if(h>=1&&h<5)
			{
				f=120;
			}
			else if(h>=6&&h<9)
			{
				f=60;
			}
			else if(h>=9&&h<17)
			{
				f=20;
			}
			else if(h>=17&&h<21)
			{
				f=20;
			}
			else if(h>=21&&h<=24||h<=0&&h<1)
			{
				f=40;
			}
			long m=(long)(Math.random()*f*1000);
			System.out.println("等待:"+(m/1000)+"秒");
			try {
				Thread.sleep(m);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("循环广播一次"+new Date()+" 当前用户有："+WsManager.getInstance().getNumOfUser());
			int c=(int)(Math.random()*(searchList.size()));
			JSONObject msg=searchList.get(c);
			if(msg.has("time"))
			{
				msg.remove("time");
			}
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time=sdf.format(date);
			
			try {
				msg.append("time",time);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			broadcast(msg);
		}
	}
	
	
	/**
	 * 添加订阅者
	 */
	
	public void addSubscriber(MessageInbound ws)
	{
		System.out.println("添加订阅者");
		WebSocketQueue.getInstance().wslist.add(ws);
	}
	
	/**
	 * 移除定阅者
	 */
	
	public void removeSubscriber(MessageInbound ws)
	{
		System.out.println("移除定阅者");
		WebSocketQueue.getInstance().wslist.remove(ws);
	}
	
	/**
	 * 打印多少个用户在线
	 */
	public int getNumOfUser()
	{
		return WebSocketQueue.getInstance().wslist.size();
	}

}
