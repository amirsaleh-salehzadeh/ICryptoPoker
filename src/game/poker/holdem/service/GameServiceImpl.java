/*
The MIT License (MIT)

Copyright (c) 2013 Jacob Kanipe-Illig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package game.poker.holdem.service;

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.PlayerDaoImpl;
import game.poker.holdem.domain.BlindLevel;
import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.GameStatus;
import game.poker.holdem.domain.GameStructure;
import game.poker.holdem.domain.GameType;
import game.poker.holdem.domain.Player;
import game.poker.holdem.util.GameUtil;
import game.poker.holdem.view.PlayerStatusObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tools.AMSException;

public class GameServiceImpl implements GameServiceInterface {

	public Game startGame(Game game) throws AMSException {
		GameDaoImpl gameDao = new GameDaoImpl();
		// game = gameDao.findById(game.getId(), null);
		if (game.getPlayers().size() < 2) {
			throw new AMSException("Not Enough Players");
		}
		if (game.getPlayers().size() > 10) {
			throw new AMSException("Too Many Players");
		}
		if (game.isStarted()) {
			throw new AMSException("Game already started");
		}
		// Set started flag
		game.setStarted(true);
		// Start at the first blind level for the game
		GameStructure gs = game.getGameStructure();
		if (game.getGameType() == GameType.TOURNAMENT) {
			List<BlindLevel> blinds = gs.getBlindLevels();
			Collections.sort(blinds);
			gs.setCurrentBlindLevel(blinds.get(0));
		}
		// Get all players associated with the game.
		// Assign random position. Save the player.
		List<Player> players = new ArrayList<Player>();
		players.addAll(game.getPlayers());
		Collections.shuffle(players);
		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			p.setGameId(game.getId());
			p.setGamePosition(i + 1);
			playerDao.merge(p, null);
		}

		// Set Button and Big Blind. Button is position 1 (index 0)
		Collections.sort(players);
		game.setPlayerInBTN(players.get(0));

		// Save and return the updated game
		return gameDao.merge(game, null);
	}

	public Player addNewPlayerToGame(Game game, Player player) {
		if (game.isStarted() && game.getGameType() == GameType.TOURNAMENT) {
			throw new IllegalStateException(
					"Tournament in progress, no new players may join");
		}
		// game = gameDao.merge(game, null);
		// game = gameDao.addOnePlayerToGame(game, null);
		if (game.getPlayers().size() >= 10) {
			throw new IllegalStateException(
					"Cannot have more than 10 players in one game");
		}
		player.setGameId(game.getId());
		if (game.getGameType() == GameType.TOURNAMENT) {
			player.setChips(game.getGameStructure().getStartingChips());
			player.setTotalChips(player.getTotalChips()
					- game.getGameStructure().getStartingChips());
		}
		PlayerDaoImpl playerDao = new PlayerDaoImpl();

		player = playerDao.merge(player, null);
		if (player == null) {
			return null;
		}
		GameDaoImpl gameDao = new GameDaoImpl();
		game.setPlayersRemaining(game.getPlayersRemaining() + 1);
		game = gameDao.merge(game, null);
		return player;
	}

	public String getGameStatusJSON(Game game, String playerId) {
		GameStatus gs = GameUtil.getGameStatus(game);
		Set<PlayerStatusObject> players = new HashSet<PlayerStatusObject>();
		PlayerServiceManagerImpl playerService = new PlayerServiceManagerImpl();
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("gameStatus", gs);
		results.put("players", players);
		for (Player p : game.getPlayers()) {
			PlayerStatusObject ptmp = playerService.buildPlayerStatus(
					game.getId(), p.getId());
			if ((!p.getId().equals(playerId) || !game.isStarted())
					&& !gs.equals(GameStatus.END_HAND)) {
				ptmp.setCard1("card1" + playerId);
				ptmp.setCard2("");
			}
			players.add(ptmp);
		}
		if (game.getCurrentHand() != null)
			results.put("handId", game.getCurrentHand().getId());
		else
			results.put("handId", 0);
		if (game.getGameStructure().getCurrentBlindLevel() != null) {
			results.put("smallBlind", game.getGameStructure()
					.getCurrentBlindLevel().getSmallBlind());
			results.put("bigBlind", game.getGameStructure()
					.getCurrentBlindLevel().getBigBlind());
		}
		if (game.getGameStructure().getCurrentBlindEndTime() != null) {
			long timeLeft = game.getGameStructure().getCurrentBlindEndTime()
					.getTime()
					- new Date().getTime();
			timeLeft = Math.max(0, timeLeft);
			results.put("blindTime", timeLeft);
		}
		if (game.getCurrentHand() != null) {
			results.put("pot", game.getCurrentHand().getPot());
			results.put("cards", game.getCurrentHand().getBoard()
					.getBoardCardsString());
		} else
			System.out.println("empty");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(results);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

}
