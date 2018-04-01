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
package webservices;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import common.game.poker.holdem.GameENT;
import tools.AMSException;

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.PlayerDaoImpl;
import game.poker.holdem.domain.GameStatus;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerStatus;
import game.poker.holdem.service.GameServiceImpl;
import game.poker.holdem.service.PlayerActionServiceImpl;
import game.poker.holdem.service.PlayerServiceManager;
import game.poker.holdem.service.PlayerServiceManagerImpl;
import game.poker.holdem.service.PokerHandServiceImpl;
import game.poker.holdem.util.GameUtil;
import game.poker.holdem.view.PlayerStatusObject;

/**
 * Controller class that will handle the front-end API interactions regarding
 * individual players involved with a game.
 * 
 * @author jacobhyphenated Copyright (c) 2013
 */

@Path("GetPlayerServiceWS")
public class PlayerServiceWS {

	private PlayerActionServiceImpl playerActionService;

	private PlayerServiceManagerImpl playerService;

	private GameServiceImpl gameService;

	/**
	 * Have a new player join a game.
	 * 
	 * @param gameId
	 *            unique ID for the game to be joined
	 * @param playerName
	 *            player name
	 * @return Map representing the JSON String of the new player's unique id.
	 *         The playerId will be used for that player's actions in future
	 *         requests. Example: {"playerId":xxx}
	 */

	@GET
	@Path("/JoinGame")
	@Produces("application/json")
	public String joinGame(long gameId, String playerName) {
		GameDaoImpl gameDao = new GameDaoImpl();
		GameENT game = gameDao.findById(gameId, null);
		Player player = new Player();
		player.setName(playerName);
		player = gameService.addNewPlayerToGame(game, player);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(Collections.singletonMap(
					"playerId", player.getId()));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * Player status in the current game and hand.
	 * 
	 * @param gameId
	 *            unique ID of the game the player is playing in
	 * @param playerId
	 *            unique ID of the player
	 * @return a Map represented as a JSON String with values for status (
	 *         {@link PlayerStatus}), chips left, current hole cards, amount bet
	 *         this betting round, and amount to call if there is a bet this
	 *         round. Example:
	 *         {"status":"ACTION_TO_CALL","chips":1000,"card1":"Xx"
	 *         ,"card2":"Xx", "amountBetRound":xx,"amountToCall":100}
	 */

	@GET
	@Path("/GetPlayerStatus")
	@Produces("application/json")
	public String getPlayerStatus(@QueryParam("gameId") long gameId,
			@QueryParam("playerId") String playerId) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		playerService = new PlayerServiceManagerImpl();
		try {
			json = mapper.writeValueAsString(playerService.buildPlayerStatus(
					gameId, playerId));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * Fold the hand.
	 * 
	 * @param gameId
	 *            Unique ID of the game being played
	 * @param playerId
	 *            Unique ID of the player taking the action
	 * @return Map with a single field for success, if the player successfully
	 *         folded or not. If fold is not a legal action, or the it is not
	 *         this players turn to act, success will be false. Example:
	 *         {"success":true}
	 */

	@GET
	@Path("/fold")
	@Produces("application/json")
	public String fold(@QueryParam("gameId") long gameId,
			@QueryParam("playerId") String playerId) {
		gameService = new GameServiceImpl();
		playerActionService = new PlayerActionServiceImpl();
		GameDaoImpl gameDao = new GameDaoImpl();
		GameENT game = gameDao.findById(gameId, null);
		Player player = playerActionService.getPlayerById(playerId);
		HandEntity hand = playerActionService.fold(player, game);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			if (hand == null)
				json = mapper.writeValueAsString(Collections.singletonMap(
						"success", false));
			else
				json = mapper.writeValueAsString(Collections.singletonMap(
						"success", true));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * Call a bet.
	 * 
	 * @param gameId
	 *            Unique ID of the game being played
	 * @param playerId
	 *            unique ID of the player calling the action
	 * @return Map represented as a JSON String with two fields, success and
	 *         chips. If calling is not a legal action, or it is not this
	 *         player's turn, success will be false. The Chips field represents
	 *         the current amount of chips the player has left after taking this
	 *         action. Example: {"success":true,"chips":xxx}
	 */

	@GET
	@Path("/call")
	@Produces("application/json")
	public String call(@QueryParam("gameId") long gameId,
			@QueryParam("playerId") String playerId) {
		gameService = new GameServiceImpl();
		GameDaoImpl gameDao = new GameDaoImpl();
		GameENT game = gameDao.findById(gameId, null);
		playerActionService = new PlayerActionServiceImpl();
		Player player = playerActionService.getPlayerById(playerId);
		HandEntity hand = playerActionService.call(player, game);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (hand == null)
			resultMap.put("success", false);
		else
			resultMap.put("success", true);
		resultMap.put("chips", player.getChips());
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(resultMap);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * Check the action in this hand.
	 * 
	 * @param gameId
	 *            Unique ID of the game being played
	 * @param playerId
	 *            Unique ID of the player making the action
	 * @return Map represented as a JSON String with a single field: success. If
	 *         checking is not a legal action, or it is not this player's turn,
	 *         success will be false. Example: {"success":false}
	 */

	@GET
	@Path("/check")
	@Produces("application/json")
	public String check(@QueryParam("gameId") long gameId,
			@QueryParam("playerId") String playerId) {
		gameService = new GameServiceImpl();
		GameDaoImpl gameDao = new GameDaoImpl();
		GameENT game = gameDao.findById(gameId, null);
		playerActionService = new PlayerActionServiceImpl();
		Player player = playerActionService.getPlayerById(playerId);
		HandEntity hand = playerActionService.check(player, game);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			if (hand == null)
				json = mapper.writeValueAsString(Collections.singletonMap(
						"success", false));
			else
				json = mapper.writeValueAsString(Collections.singletonMap(
						"success", true));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * Bet or Raise.
	 * 
	 * @param gameId
	 *            unique ID of the game being played
	 * @param playerId
	 *            Unique ID of the player taking this action
	 * @param betAmount
	 *            amount of the bet. This is the total amount, so if there was a
	 *            bet before, and this is the raise, this value represents the
	 *            amount bet for only this raise from the player. To put in
	 *            another way, this betAmount value is the amount is the amount
	 *            bet <em>in addition to</em> the amount it would take to call. <br />
	 *            For example: Player 1 bets 100. Player 2 raises to 300. Player
	 *            2 calls this method with the betAmount parameter of 200.
	 *            Player 1 re-raises to 900 total (100 + 200 to call + 600
	 *            more). The betAmount parameter is passed as 600.
	 * @return Map representing a JSON String with two values: success and
	 *         chips. If a bet is not a legal action, or if it is not this
	 *         player's turn, success will be false. The chips value represents
	 *         the amount of chips the player has after completing this action.
	 *         Example: {"success":true,"chips":xxx}
	 */

	@GET
	@Path("/bet")
	@Produces("application/json")
	public String bet(@QueryParam("gameId") long gameId,
			@QueryParam("playerId") String playerId,
			@QueryParam("betAmount") int betAmount) {
		gameService = new GameServiceImpl();
		playerActionService = new PlayerActionServiceImpl();
		GameDaoImpl gameDao = new GameDaoImpl();
		GameENT game = gameDao.findById(gameId, null);
		Player player = playerActionService.getPlayerById(playerId);
		HandEntity hand = playerActionService.bet(player, game, betAmount);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (hand == null)
			resultMap.put("success", false);
		else
			resultMap.put("success", true);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(resultMap);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/LeaveGame")
	@Produces("application/json")
	public String leaveGame(@QueryParam("playerId") String playerId) {
		gameService = new GameServiceImpl();
		playerActionService = new PlayerActionServiceImpl();
		GameDaoImpl gameDao = new GameDaoImpl();
		Player player = playerActionService.getPlayerById(playerId);
		GameENT game = gameDao.findById(player.getGameId(), null);
		player.setGameId(0);
		player.setGamePosition(0);
		player.setSittingOut(false);
		player.setTotalChips(player.getChips() + player.getTotalChips());
		PlayerDaoImpl pdao = new PlayerDaoImpl();
		pdao.merge(player, null);
//		HandEntity he = game.getCurrentHand();
//		for (Player p : game.getPlayers()) {
//			if(!p.equals(player)&& p.getGamePosition()<player.getGamePosition())
//		}
		// if (game.isStarted() && he != null) {
		//
		// }
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", false);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(resultMap);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// game = gdao.findById(game.getId(), null);
		// Player p = pdao.findById(uid, null);
		// p.setGameId(0);
		// p.setGamePosition(0);
		// if (players.size() > 1 || !game.isStarted()
		// || game.getCurrentHand() == null) {
		// p.setTotalChips(p.getChips() + p.getTotalChips());
		// } else {
		// p.setTotalChips(p.getChips() + p.getTotalChips()
		// + game.getCurrentHand().getTotalBetAmount());
		// }
		// if (players.size() <= 1 && game.getCurrentHand() != null &&
		// game.isStarted()) {
		// try {
		// handService.endHand(game);
		// } catch (AMSException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } else {
		// game.setPlayersRemaining(game.getPlayersRemaining() - 1);
		// }
		// p.setChips(0);
		// pdao.merge(p, null);
		// game = gdao.merge(game, null);
		// try {
		// players.get(uid).close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// players.remove(uid, players.get(uid));

		return json;
	}

	/**
	 * Sit back in the game after having sat out
	 * 
	 * @param playerId
	 *            Player being sat back in
	 * @return {"success":true} when the player is sat back in the game
	 */
	@GET
	@Path("/sitIn")
	@Produces("application/json")
	public String sitIn(@QueryParam("playerId") String playerId) {
		playerActionService = new PlayerActionServiceImpl();
		Player player = playerActionService.getPlayerById(playerId);
		playerActionService.sitIn(player);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(Collections.singletonMap(
					"success", true));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
