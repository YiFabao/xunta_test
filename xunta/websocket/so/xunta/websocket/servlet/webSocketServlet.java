package so.xunta.websocket.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import so.xunta.service.WsManager;

public class webSocketServlet extends WebSocketServlet {

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request) {

		MessageInbound ws = new MyWebsocket();
		//添加定阅者
		WsManager.getInstance().addSubscriber(ws);
		return ws;
	}
}