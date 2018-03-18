package game.poker.holdem.domain;

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.HandDaoImpl;
import game.poker.holdem.dao.PlayerDaoImpl;
import game.poker.holdem.service.GameServiceImpl;
import game.poker.holdem.service.PokerHandServiceImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

import tools.AMSException;

public class Table {

	private Game game;
	private Map<String, Session> players = Collections
			.synchronizedMap(new HashMap<String, Session>());

	public Map<String, Session> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, Session> players) {
		this.players = players;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	};

	public void addPlayer(String user, Session session) {
		players.put(user, session);

	}

	public void removePlayer(String uid) {
		PlayerDaoImpl pdao = new PlayerDaoImpl();
		Player p = pdao.findById(uid, null);
		p.setGameId(0);
		p.setGamePosition(0);
		if (players.size() > 1 || !game.isStarted()) {
			p.setTotalChips(p.getChips() + p.getTotalChips());
		} else {
			p.setTotalChips(p.getChips() + p.getTotalChips()
					+ game.getCurrentHand().getTotalBetAmount());
		}
		p.setChips(0);
		if(game.getPlayerInBTN().equals(p));
		
		pdao.merge(p, null);
		GameDaoImpl gameDao = new GameDaoImpl();
		game.setPlayersRemaining(game.getPlayersRemaining()-1);
		
		players.remove(uid, players.get(uid));
	}

	public void sendToAll(String user) {
		GameServiceImpl gameService = new GameServiceImpl();
		if (players.size() == 2) {
			GameDaoImpl gameDao = new GameDaoImpl();
			game = gameDao.findById(game.getId(), null);
			if (!game.isStarted()) {
				try {
					if (!game.isStarted())
						game = gameService.startGame(game);
				} catch (AMSException e) {
					e.printStackTrace();
				}
			}
			PokerHandServiceImpl handService = new PokerHandServiceImpl();
			if (game.getCurrentHand() == null)
				handService.startNewHand(game);
		}
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

	public void setGameId(long guid) {
		GameDaoImpl gameDao = new GameDaoImpl();
		game = gameDao.findById(guid, null);
	}
}
