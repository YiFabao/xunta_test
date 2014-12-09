package so.xunta.websocket.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.List;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.json.JSONObject;

import so.xunta.service.WsManager;

public class MyWebsocket extends MessageInbound {


	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		System.out.println("onBinaryMessage:"+arg0);
		
	}

	@Override
	protected void onTextMessage(CharBuffer arg0) throws IOException {
		//不做任何处理
		System.out.println("非法连接，关闭该用户连接");
		WsManager.getInstance().removeSubscriber(this);
/*		 //收取客户端发来的消息 
		System.out.println("来自客户端的消息："+arg0);
		//将该消息推到所有客户端
		//将该消息保存到textList中
		if(WsManager.searchList.contains(arg0.toString()))
		{
			System.out.println("存在");
		}
		else
		{
			WsManager.searchList.add(arg0.toString());
		}
		WsManager.getInstance().broadcast(arg0.toString());*/
		
	}

	@Override
	protected void onClose(int status) {
		// TODO Auto-generated method stub
		System.out.println("status:"+status);
		WsManager.getInstance().removeSubscriber(this);
		if(status==1000)
		{
			System.out.println("退出一个用户");
			System.out.println("检查当前用户数是否为0");
		}
		int userNum=WsManager.getInstance().getNumOfUser();
		if(userNum==0)
		{
			System.out.println("用户数为0了,此时不应该发消息了,将flag设为false");
			WsManager.flag=false;
		}
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		// TODO Auto-generated method stub
		System.out.println("连接成功："+outbound);
		System.out.println("进来一个用户，开始循环向其推送消息");
		//检查flag是否为true,如果是为true说明，存在上线用户,如果不存在就要将其设为true,并调用循环发送消息的方法
		//先将历史的消息先推给他
		List<JSONObject> history=WsManager.search_history;
		
		for(int i=0;i<history.size();i++)
		{
			try {
				JSONObject json=history.get(i);
				outbound.writeTextMessage(CharBuffer.wrap(json.toString()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("出错了");
			}
		}

		if(!WsManager.flag)
		{
			System.out.println("将flag设为true");
			WsManager.flag=true;			
			System.out.println("启动循环发消息的方法");
			WsManager.getInstance().broadcastWhenTrue();
		}
	}
	
	public void print()
	{
		System.out.println("调用其他方法成功");
	}
}
