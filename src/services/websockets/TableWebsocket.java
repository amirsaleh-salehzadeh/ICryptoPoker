package services.websockets;

import game.poker.holdem.domain.ChatRoom;

import java.io.StringWriter;
import java.sql.Date;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.json.Json;
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



@ServerEndpoint("/game/{uid}")
public class TableWebsocket {
//<<<<<<< HEAD
	private static Set<Session> sessions= Collections.synchronizedSet(new HashSet<Session>());
	

	  @OnOpen
	  public void onOpen(Session session) {
	    
		  sessions.add(session) ;
		  System.out.print("player added") ;
		      
		  }

	  @OnClose
	  public void onClose(Session session) {
		
		          sessions.remove(session) ;
				  System.out.println("player left");
				  return ;
			  
			 
		  }
	      
	   @OnMessage
	  public void onReciveMsg(Session clientSession, String msg ,String gid,@PathParam("uid") String uid) throws InterruptedException {
	    System.out.println("on recive msg." +msg);
	      
	      for(Session session :sessions){
	    	  session.getAsyncRemote().sendText(String.valueOf(java.time.LocalTime.now().getSecond())) ;	      }

//        Thread.sleep(5000);
//	    clientSession.getAsyncRemote().sendText("your personal message from server");
	  }
	  }
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
/*=======
	static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>()) ;
	
	
	@OnOpen
	public void handleOpen(Session user){
		
		users.add(user) ;
		System.out.println("player has joined");
	}
	
	@OnMessage
	public void handleMessage(String message , Session userSession){
		String username = (String)userSession.getUserProperties().get("username") ;
		if(username==null){
			userSession.getUserProperties().put("username",message) ;
//			userSession.getBasicRemote().sendText(buildJsonData("System", message)) ;
		}else{
			Iterator<Session> iterator = users.iterator() ;
//			while(iterator.hasNext()){
//				iterator.next().getBasicRemote().sendText(buildJsonData(username,message)) ;
//			}
		}
		
	}
	
	@OnClose
	public void handleClose(Session user){
		users.remove(user);
		System.out.println("player has lefted");
	}
	
	@OnError
	public void handleError(Throwable t){
		
		t.printStackTrace() ;
		
	}
	
	private String buildJsonData(String username,String message){
		 
		
		ObjectMapper mapper = new ObjectMapper() ;
		
		String[] temp = {"message",username +":" +message};
//		 String json = mapper.writeValueAsString(temp) ;
		
		return "" ;
		
	}
>>>>>>> origin/NeilV0
	*/
/*	  }
}*/
