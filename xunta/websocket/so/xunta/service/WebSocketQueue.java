package so.xunta.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.websocket.MessageInbound;

public class WebSocketQueue {
	public List<MessageInbound> wslist=new ArrayList<MessageInbound>();
	
	private WebSocketQueue(){};
	
	public static WebSocketQueue instance=null;
	 
	public static WebSocketQueue getInstance()
	{
		if(instance==null)
		{
			instance=new WebSocketQueue();
		}
		return instance;
	}
	
}
