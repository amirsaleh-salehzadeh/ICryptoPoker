package game.poker.holdem.domain;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

public class Table {

	private long game;
	private Map<String, Session> players = Collections.synchronizedMap(new HashMap<String, Session>());

	public Map<String, Session> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, Session> players) {
		this.players = players;
	}

	public long getGame() {
		return game;
	}

	public void setGame(long game) {
		this.game = game;
	};

	public void addPlayer(String user, Session session) {
		players.put(user, session);

	}

	public void removePlayer(String uid) {
		players.remove(uid, players.get(uid)) ;
	}

	public void sendToAll(String user, String message) {
		ObjectMapper mapper = new ObjectMapper() ;
		Message temp = null;
		try {
			temp = mapper.readValue(message, Message.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String json = "" ;
		for (String cur : players.keySet()) {
//			if (!(cur.equals(user))) {
				try {
					json = mapper.writeValueAsString(temp) ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				players.get(cur).getAsyncRemote().sendText(json) ;
//			}
		}
	}

	public void nextPlayerTurn(Message message) {
		ObjectMapper mapper = new ObjectMapper() ;
		String json = "" ;
		for (String cur : players.keySet()) {
			
				try {
					json = mapper.writeValueAsString(message) ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				players.get(cur).getAsyncRemote().sendText(json) ;
			
		}
        
	}
}
