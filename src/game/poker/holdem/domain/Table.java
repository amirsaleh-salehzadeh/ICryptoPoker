package game.poker.holdem.domain;

<<<<<<< HEAD
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
=======
import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.HandDaoImpl;
import game.poker.holdem.dao.PlayerDaoImpl;
import game.poker.holdem.service.GameServiceImpl;
import game.poker.holdem.service.PlayerActionServiceImpl;
import game.poker.holdem.service.PlayerServiceManagerImpl;
import game.poker.holdem.service.PokerHandServiceImpl;
import game.poker.holdem.util.GameUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
>>>>>>> origin/AmirV1

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

<<<<<<< HEAD
public class Table {

	private long game;
=======
import tools.AMSException;

public class Table {

	private Game game;
>>>>>>> origin/AmirV1
	private Map<String, Session> players = Collections
			.synchronizedMap(new HashMap<String, Session>());

	public Map<String, Session> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, Session> players) {
		this.players = players;
	}

<<<<<<< HEAD
	public long getGame() {
		return game;
	}

	public void setGame(long game) {
=======
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
>>>>>>> origin/AmirV1
		this.game = game;
	};

	public void addPlayer(String user, Session session) {
		players.put(user, session);

	}

	public void removePlayer(String uid) {
<<<<<<< HEAD
		players.remove(uid, players.get(uid));
	}

	public void sendToAll(String user, String message) {
		for (String cur : players.keySet()) {
			players.get(cur).getAsyncRemote().sendText(message);
=======
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
		pdao.merge(p, null);
		game.setPlayersRemaining(game.getPlayersRemaining() - 1);
		players.remove(uid, players.get(uid));
	}

	public void sendToAll(String user) {
		GameServiceImpl gameService = new GameServiceImpl();
		GameDaoImpl gameDao = new GameDaoImpl();
		game = gameDao.findById(game.getId(), null);
		GameStatus gs = GameUtil.getGameStatus(game);
		if (players.size() >= 2 && !game.isStarted()
				&& gs.equals(GameStatus.NOT_STARTED)) {
			try {
				game = gameService.startGame(game);
			} catch (AMSException e) {
				e.printStackTrace();
			}
			PokerHandServiceImpl handService = new PokerHandServiceImpl();
			if (game.getCurrentHand() == null)
				game.setCurrentHand(handService.startNewHand(game));
		}
		GameServiceImpl gservice = new GameServiceImpl();
		Map<String, Object> results = gservice.getGameStatusMap(game);
		for (String cur : players.keySet()) {
			Map<String, Object> resultsTMP = results;
			String json = gservice.getGameStatusJSON(game, resultsTMP, cur);
			players.get(cur).getAsyncRemote().sendText(json);
>>>>>>> origin/AmirV1
		}
	}

	public void nextPlayerTurn(Message message) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		for (String cur : players.keySet()) {
<<<<<<< HEAD

			try {
				json = mapper.writeValueAsString(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			players.get(cur).getAsyncRemote().sendText(json);

		}

	}
=======
			try {
				json = mapper.writeValueAsString(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			players.get(cur).getAsyncRemote().sendText(json);
		}

	}

	public void setGameId(long guid) {
		GameDaoImpl gameDao = new GameDaoImpl();
		game = gameDao.findById(guid, null);
	}
>>>>>>> origin/AmirV1
}
