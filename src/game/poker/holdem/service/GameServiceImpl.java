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
import game.poker.holdem.domain.GameStatus;
import game.poker.holdem.domain.GameStructure;
import game.poker.holdem.domain.GameType;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerHand;
import game.poker.holdem.eval.HandRank;
import game.poker.holdem.holder.Board;
import game.poker.holdem.util.GameUtil;
import game.poker.holdem.util.PlayerUtil;
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

import common.game.poker.holdem.GameENT;
import tools.AMSException;

public class GameServiceImpl implements GameServiceInterface {

	public GameENT startGame(GameENT game) throws AMSException {
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
		if (game.getGameType() == GameType.TOURNAMENT) {
			GameStructure gs = game.getGameStructure();
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

	public Player addNewPlayerToGame(GameENT game, Player player) {
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

	public String getGameStatusJSON(GameENT game, Map<String, Object> results,
			String playerId) {
		PlayerServiceManagerImpl playerService = new PlayerServiceManagerImpl();
		GameStatus gs = (GameStatus) results.get("gameStatus");
		Set<PlayerStatusObject> players = new HashSet<PlayerStatusObject>();
		Board board = null;
		HandEntity h = new HandEntity();
		if (game.getCurrentHand() != null)
			h = game.getCurrentHand();
		if (gs.equals(GameStatus.END_HAND)) {
			board = new Board(h.getBoard().getFlop1(), h.getBoard().getFlop2(),
					h.getBoard().getFlop3(), h.getBoard().getTurn(), h
							.getBoard().getRiver());
		}
		for (Player p : game.getPlayers()) {
			PlayerStatusObject ptmp = playerService.buildPlayerStatus(
					game.getId(), p.getId());
			h.setGame(game);
			if ((!p.getId().equals(playerId) || !game.isStarted())
					&& !gs.equals(GameStatus.END_HAND)) {
				ptmp.setCard1("card1" + playerId);
				ptmp.setCard2("");
			} else if (gs.equals(GameStatus.END_HAND)) {
				for (PlayerHand ph : h.getPlayers()) {
					if (ph.getPlayer().equals(p)
							&& h.getBoard().getRiver() != null) {
						HandRank rank = PlayerUtil.evaluator.evaluate(board,
								ph.getHand());
						ptmp.setHandRank(rank.getHandType().toString());
					}
				}
			}
			players.add(ptmp);
		}
		results.put("players", players);
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

	public Map<String, Object> getGameStatusMap(GameENT game) {
		GameStatus gs = GameUtil.getGameStatus(game);
		Map<String, Object> results = new HashMap<String, Object>();
		PokerHandServiceImpl handService = new PokerHandServiceImpl();
		results.put("gameStatus", gs);
		HandEntity h = new HandEntity();
		if (game.getCurrentHand() != null)
			h = game.getCurrentHand();
		h.setGame(game);
		if (game.isStarted() && game.getCurrentHand() != null
				&& gs.equals(GameStatus.PREFLOP)) {
			results.put("POST_SB", handService.getPlayerInSB(h).getId());
			results.put("POST_BB", handService.getPlayerInBB(h).getId());
			results.put("DEALER", game.getPlayerInBTN().getId());
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
		}
		return results;
	}

	/*
	 * Finalize a game or the game which only has one player. Post Null to
	 * choose a case
	 */

	@Override
	public void closeTheGame(GameENT eitherGame, Player eitherPlayer) {
		GameDaoImpl gdao = new GameDaoImpl();
		GameENT game = eitherGame;
		if (eitherGame == null)
			game = gdao.findById(eitherPlayer.getGameId(), null);
		else
			game = eitherGame;
		if (game == null)
			return;
		Set<Player> players = game.getPlayers();
		double shareOfPlayers = 0;
		if (game.getCurrentHand() != null)
			shareOfPlayers = game.getCurrentHand().getTotalBetAmount()
					/ game.getPlayers().size();
		if (game.isStarted() && players.size() > 0) {
			PlayerDaoImpl pdao = new PlayerDaoImpl();
			for (Player p : players) {
				if (shareOfPlayers > 0)
					p.setTotalChips((int) (p.getChips() + p.getTotalChips() + Math
							.round(shareOfPlayers)));
				else if (game.getCurrentHand() != null) 
					p.setTotalChips(p.getChips() + p.getTotalChips() + game.getCurrentHand().getTotalBetAmount());
				else
					p.setTotalChips(p.getChips() + p.getTotalChips());
				p.setChips(0);
				p.setSittingOut(false);
				p.setFinishPosition(0);
				p.setGamePosition(0);
				p.setGameId(0);
				pdao.merge(p, null);
			}
			GameDaoImpl gameDao = new GameDaoImpl();
			game.setCurrentHand(null);
			game.setStarted(false);
			game.setPlayerInBTN(null);
			game.setPlayersRemaining(0);
			game.setPlayers(new HashSet<Player>());
			game = gameDao.merge(game, null);
		}

	}

	@Override
	public void leaveTheGame(Player player) {
		GameDaoImpl gdao = new GameDaoImpl();
		GameENT game = gdao.findById(player.getGameId(), null);
		PlayerDaoImpl pdao = new PlayerDaoImpl();
		for (Player p : game.getPlayers()) {
			if (p.getGamePosition() > player.getGamePosition()) {
				p.setGamePosition(p.getGamePosition() - 1);
				pdao.merge(p, null);
			}
		}
		player.setGameId(0);
		player.setTotalChips(player.getChips() + player.getTotalChips());
		player.setChips(0);
		player.setSittingOut(false);
		player.setGamePosition(0);
		player.setFinishPosition(0);
		pdao.merge(player, null);
	}
}
