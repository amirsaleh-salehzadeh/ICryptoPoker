package game.poker.holdem.domain;

import game.poker.holdem.dao.GameDaoImpl;
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
import javax.websocket.Session;
import common.game.poker.holdem.GameENT;
import tools.AMSException;

public class Table {

	private GameENT game;
	private Map<String, Session> playerSessions = Collections
			.synchronizedMap(new HashMap<String, Session>());
	private int handCount;
	private PlayerDaoImpl pdao;
	private GameDaoImpl gdao;
	private PokerHandServiceImpl handService;
	private PlayerActionServiceImpl playerActionService;
	private GameServiceImpl gameService;

	public Table() {
		pdao = new PlayerDaoImpl();
		gdao = new GameDaoImpl();
		handService = new PokerHandServiceImpl();
		handCount = 0;
		playerActionService = new PlayerActionServiceImpl();
		gameService = new GameServiceImpl();
	}

	public Map<String, Session> getPlayerSessions() {
		return playerSessions;
	}

	public void setPlayerSessions(Map<String, Session> playerSessions) {
		this.playerSessions = playerSessions;
	}

	public GameENT getGame() {
		return game;
	}

	public void setGame(GameENT game) {
		this.game = game;
	};

	public void addPlayer(String uid, Session session) {
		// Player p = pdao.findById(uid, null);
		// if (p.isSittingOut())
		// playerActionService.sitIn(pdao.findById(uid, null));
		playerSessions.put(uid, session);
	}

	public void removePlayer(String uid) {
		// if the user is in the game
		game = gdao.findById(game.getId(), null);
		for (Player p : game.getPlayers()) {
			if (p.getId().equals(uid)) {
				handService.sitOutCurrentPlayer(game, p);
			}
		}
		// Player p = pdao.findById(uid, null);
		// if (playerSessions.size() == 2)
		// playerActionService.fold(p, game, true);
		// if (playerSessions.size() >= 2 && game.isStarted()
		// && game.getCurrentHand() != null)
		// // game.setCurrentHand(handService.sitOutCurrentPlayer(game, p));
		// gameService.leaveTheGame(p);
		// try {
		// playerSessions.get(uid).close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		try {
			playerSessions.get(uid).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		playerSessions.remove(uid, playerSessions.get(uid));
		// if (playerSessions.size() == 1 && game.getCurrentHand() != null) {
		// try {
		// handService.endHand(game);
		// } catch (AMSException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// game = gdao.findById(game.getId(), null);
		// }
		handCount--;
		sendToAll("");
		System.out.println("removed from table " + uid);
	}

	public void sendToAll(String user) {
		if (playerSessions.size() <= 0) {
			gameService.closeTheGame(game, null);
		}
		game = gdao.findById(game.getId(), null);
		GameStatus gs = GameUtil.getGameStatus(game);
		// To check when to start next round
		if (game.isStarted() && game.getCurrentHand() != null) {
			Set<PlayerHand> pltmp = getValidPlayers();
			// System.out.println("getValidPlayers SIZE in senttoall "
			// + pltmp.size());
			try {
				if (handCount >= pltmp.size() || pltmp.size() == 1)
					GameUtil.goToNextStepOfTheGame(game);
				game = gdao.findById(game.getId(), null);
				GameStatus gsPrimary = GameUtil.getGameStatus(game);
				if (gsPrimary != gs)
					handCount = 0;
			} catch (AMSException e1) {
				e1.printStackTrace();
			}
		}

		// START THE GAME AND HAND
		if (getPlayerHands().size() > 1 && !game.isStarted()
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

		// POSTING TO PLAYER SESSIONS
		if (playerSessions.size() > 0)
			for (String cur : playerSessions.keySet()) {
				Map<String, Object> resultsTMP = results;
				String json = gameService.getGameStatusJSON(game, resultsTMP,
						cur);
				if (playerSessions.get(cur).isOpen())
					playerSessions.get(cur).getAsyncRemote().sendText(json);
				else {
					try {
						playerSessions.get(cur).close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					playerSessions.remove(cur, playerSessions.get(cur));
				}
			}
		// TODO else destroy the sessions, close the game and clean the game
		// from tableSocket
	}

	// counting the number of players with chips > 0 and have not folded
	private Set<PlayerHand> getValidPlayers() {
		Set<PlayerHand> result = new HashSet<PlayerHand>();
		// when 1st player sits
		for (PlayerHand ph : game.getCurrentHand().getPlayers()) {
			if (ph.getPlayer().getChips() > 0
					&& ph.getStatus() != PlayerHandStatus.FOLDED
					&& !ph.getPlayer().isSittingOut())
				result.add(ph);
		}
		return result;
	}

	private Set<Player> getPlayerHands() {
		Set<Player> playersInSeat = new HashSet<Player>();
		for (Player p : game.getPlayers()) {
			if (!p.isSittingOut())
				playersInSeat.add(p);
		}
		return playersInSeat;
	}

	public void setGameId(long guid) {
		game = gdao.findById(guid, null);
	}
	
	public boolean isPlayingTheGame(Player p){
		boolean res = false;
		for(Player player: game.getPlayers()){
			if(player.equals(p))
				res = true;
		}
		return res;
	}
}
