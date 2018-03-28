package game.poker.holdem.domain;

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

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

import tools.AMSException;

public class Table {

	private Game game;
	private Map<String, Session> players = Collections
			.synchronizedMap(new HashMap<String, Session>());
	private PlayerDaoImpl pdao;
	private GameDaoImpl gdao;
	private PokerHandServiceImpl handService;

	public Table() {
		pdao = new PlayerDaoImpl();
		gdao = new GameDaoImpl();
		handService = new PokerHandServiceImpl();
	}

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
		Player p = pdao.findById(uid, null);
		p.setGameId(0);
		p.setGamePosition(0);
		if (players.size() > 1 || !game.isStarted()) {
			p.setTotalChips(p.getChips() + p.getTotalChips());
		} else {
			p.setTotalChips(p.getChips() + p.getTotalChips()
					+ game.getCurrentHand().getTotalBetAmount());
		}
		if (players.size() <= 1) {
			try {
				handService.endHand(game);
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			game.setPlayersRemaining(game.getPlayersRemaining() - 1);
		}
		p.setChips(0);
		pdao.merge(p, null);
		game = gdao.merge(game, null);
		players.remove(uid, players.get(uid));
	}

	public void sendToAll(String user) {
		GameServiceImpl gameService = new GameServiceImpl();
		game = gdao.findById(game.getId(), null);
		GameStatus gs = GameUtil.getGameStatus(game);
		if (players.size() >= 2 && !game.isStarted()
				&& gs.equals(GameStatus.NOT_STARTED)) {
			try {
				game = gameService.startGame(game);
			} catch (AMSException e) {
				e.printStackTrace();
			}
			if (game.getCurrentHand() == null)
				game.setCurrentHand(handService.startNewHand(game));
		}
		Map<String, Object> results = gameService.getGameStatusMap(game);
		for (String cur : players.keySet()) {
			Map<String, Object> resultsTMP = results;
			String json = gameService.getGameStatusJSON(game, resultsTMP, cur);
			players.get(cur).getAsyncRemote().sendText(json);
		}
	}

	public void setGameId(long guid) {
		game = gdao.findById(guid, null);
	}
}
