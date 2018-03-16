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

import game.poker.holdem.domain.Table;



@ServerEndpoint("/game/{guid}/{uid}")
public class TableWebsocket {
	static Set<Table> users = Collections.synchronizedSet(new HashSet<Table>()) ;
	
	
	@OnOpen
	public void handleOpen(Session user,@PathParam("uid") String uid,@PathParam("guid") Long guid){
		for(Table table: users) {
			  if(table.getGame().equals(guid)) {
				  table.addPlayer(uid, user);
				  System.out.println("player has joined");
				  return ;
			  }
		  }
		Table table = new Table() ;
		table.setGame(guid);
		table.addPlayer(uid, user);
		System.out.println("player has joined");
		
	}
	
	@OnMessage
	public void handleMessage(String message , Session userSession,@PathParam("uid") String uid,@PathParam("guid") Long guid){
	  for(Table table: users) {
		  if(table.getGame().equals(guid)) {
			  table.sendToAll(uid, message);
		  }
	  }
		
	}
	
	
	@OnError
	public void handleError(Throwable t){
		
		t.printStackTrace() ;
		
	}
	
	
	

}
