package services.websockets;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import org.codehaus.jettison.json.JSONObject;



@ServerEndpoint("/TableWebsocket")
public class TableWebsocket {
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
			userSession.getBasicRemote().sendText(buildJsonData("System", message)) ;
		}else{
			Iterator<Session> iterator = users.iterator() ;
			while(iterator.hasNext()){
				iterator.next().getBasicRemote().sendText(buildJsonData(username,message)) ;
			}
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
		 String json = mapper.writeValueAsString(temp) ;
		
		return json ;
		
	}
	

}
