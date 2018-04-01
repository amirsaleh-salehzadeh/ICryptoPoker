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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

import common.game.poker.holdem.GameENT;
import tools.AMSException;

public class Table {

	private GameENT game;
	private Map<String, Session> players = Collections
			.synchronizedMap(new HashMap<String, Session>());
	private int handCount;
	private PlayerDaoImpl pdao;
	private GameDaoImpl gdao;
	private PokerHandServiceImpl handService;

	public Table() {
		pdao = new PlayerDaoImpl();
		gdao = new GameDaoImpl();
		handService = new PokerHandServiceImpl();
		handCount = 0;
	}

	public Map<String, Session> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, Session> players) {
		this.players = players;
	}

	public GameENT getGame() {
		return game;
	}

	public void setGame(GameENT game) {
		this.game = game;
	};

	public void addPlayer(String user, Session session) {
		players.put(user, session);

	}

	public void removePlayer(String uid) {
		Player p = pdao.findById(uid, null);
		p.setGameId(0);
		p.setGamePosition(0);
		if (players.size() > 1 || !game.isStarted() || game.getCurrentHand() == null) {
			p.setTotalChips(p.getChips() + p.getTotalChips());
		} else {
			p.setTotalChips(p.getChips() + p.getTotalChips()
					+ game.getCurrentHand().getTotalBetAmount());
		}
		if (players.size() <= 1 && game.getCurrentHand() != null) {
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
		try {
			players.get(uid).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		players.remove(uid, players.get(uid));
	}

	public void sendToAll(String user) {
		GameServiceImpl gameService = new GameServiceImpl();
		game = gdao.findById(game.getId(), null);
		GameStatus gs = GameUtil.getGameStatus(game);
		//To check when to start next round
		if (game.isStarted() && game.getCurrentHand() != null) {
			Set<PlayerHand> pltmp = getValidPlayers();
			try {
				if (user.length() > 0
						&& handCount >= pltmp.size()
						&& PokerHandServiceImpl.isActionResolved(game
								.getCurrentHand()))
					GameUtil.goToNextStepOfTheGame(game, user);
				game = gdao.findById(game.getId(), null);
				GameStatus gsPrimary = GameUtil.getGameStatus(game);
				if (gsPrimary != gs)
					handCount = 0;
			} catch (AMSException e1) {
				e1.printStackTrace();
			}
		}
		if (players.size() > 1 && !game.isStarted()
				&& gs.equals(GameStatus.NOT_STARTED)) {
			handCount = 0;
			try {
				game = gameService.startGame(game);
			} catch (AMSException e) {
				e.printStackTrace();
			}
			if (game.getCurrentHand() == null)
				game.setCurrentHand(handService.startNewHand(game));
		}
		handCount++;
		Map<String, Object> results = gameService.getGameStatusMap(game);
		for (String cur : players.keySet()) {
			Map<String, Object> resultsTMP = results;
			String json = gameService.getGameStatusJSON(game, resultsTMP, cur);
			players.get(cur).getAsyncRemote().sendText(json);
		}
	}

	//counting the number of players with chips > 0 and have not folded
	private Set<PlayerHand> getValidPlayers() {
		Set<PlayerHand> result = new HashSet<PlayerHand>();
		//when 1st player sits
		for (PlayerHand ph : game.getCurrentHand().getPlayers()) {
			if (ph.getPlayer().getChips() > 0
					&& ph.getStatus() != PlayerHandStatus.FOLDED)
				result.add(ph);
		}
		return result;
	}

	public void setGameId(long guid) {
		game = gdao.findById(guid, null);
	}
}
