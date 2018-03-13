package services.websockets;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import org.codehaus.jettison.json.JSONObject;



@ServerEndpoint("/chat/{uid}")
public class TableWebsocket {
	private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	  @OnOpen
	  public void onOpen(Session session) {
	    sessions.add(session);
	    System.out.println("on open");
	  }

	  @OnClose
	  public void onClose(Session session) {
	    sessions.remove(session);
	    System.out.println("on close");
	  }

	  @OnMessage
	  public void onReciveMsg(Session clientSession, String msg, @PathParam("uid") String uid) throws InterruptedException {
	    System.out.println("on recive msg." +msg);
	    for (Session session : sessions) {
	      session.getAsyncRemote().sendText("msg for all clients: " + msg);
	    }

    Thread.sleep(5000);
	    clientSession.getAsyncRemote().sendText("your personal message from server");
//    Thread.sleep(5000);
//	    clientSession.getAsyncRemote().sendText("your msg was: " + msg);
//	  }
	
//	private String buildJsonData(String username,String message){
//		 String json = "" ;
//		 JSONObject temp = null ;
//		try{
//		ObjectMapper mapper = new ObjectMapper() ;
//		
//	    temp  = new JSONObject() ;
//		temp.put("mesage", username +":" +message) ;
//		Json as = new Json() ;
//		//String[] temp = {"message",};
//		  //json = mapper.writeValueAsString(temp) ;
//		}catch(Exception e){
//			e.printStackTrace() ;
//		}
//		
//		return String.valueOf(temp) ;
//		
//		
//	}
	
	  }
}
