package game.poker.holdem.domain;

import game.poker.holdem.dao.HandDaoImpl;
import game.poker.holdem.service.GameServiceImpl;
import game.poker.holdem.service.PokerHandServiceImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

import tools.AMSException;

public class Table {

	private long game;
	private Map<String, Session> players = Collections
			.synchronizedMap(new HashMap<String, Session>());

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
		players.remove(uid, players.get(uid));
	}

	public void sendToAll(String user) {
		GameServiceImpl gameService = new GameServiceImpl();
		if (players.size() == 2) {
			Game g = gameService.getGameById(game, false);
			if (!g.isStarted()) {
				try {
					if (!g.isStarted())
						g = gameService.startGame(g);
				} catch (AMSException e) {
					e.printStackTrace();
				}
			}
			PokerHandServiceImpl handService = new PokerHandServiceImpl();
			if (g.getCurrentHand() == null)
				handService.startNewHand(g);
		}
		System.out.println("hi");
		GameServiceImpl gservice = new GameServiceImpl();
		for (String cur : players.keySet()) {
			String json = gservice.getGameStatusJSON(game, cur);
			players.get(cur).getAsyncRemote().sendText(json);
		}
	}

	public void nextPlayerTurn(Message message) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		for (String cur : players.keySet()) {

			try {
				json = mapper.writeValueAsString(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			players.get(cur).getAsyncRemote().sendText(json);

		}

	}
}
