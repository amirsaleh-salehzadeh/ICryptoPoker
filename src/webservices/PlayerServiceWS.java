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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;

import common.game.poker.holdem.GameENT;
import services.websockets.TableWebsocket;
import tools.AMSException;

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.PlayerDaoImpl;
import game.poker.holdem.domain.GameStatus;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerStatus;
import game.poker.holdem.domain.Table;
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
	public Response fold(@QueryParam("gameId") long gameId,
			@QueryParam("playerId") String playerId) {
		gameService = new GameServiceImpl();
		playerActionService = new PlayerActionServiceImpl();
		GameDaoImpl gameDao = new GameDaoImpl();
		GameENT game = gameDao.findById(gameId, null);
		Player player = playerActionService.getPlayerById(playerId);
		HandEntity hand = playerActionService.fold(player, game, false);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			if (hand == null)
				json = mapper.writeValueAsString(Collections.singletonMap(
						"success", false));
			else
				json = mapper.writeValueAsString(Collections.singletonMap(
						"success", true));
			for (Table table : TableWebsocket.games) {
				if (table.getGame().getId() == gameId) {
					table.sendToAll("");
				}
			}
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
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
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
	public Response call(@QueryParam("gameId") long gameId,
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
			for (Table table : TableWebsocket.games) {
				if (table.getGame().getId() == gameId) {
					table.sendToAll("");
				}
			}
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
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
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
	public Response check(@QueryParam("gameId") long gameId,
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
			for (Table table : TableWebsocket.games) {
				if (table.getGame().getId() == gameId) {
					table.sendToAll("");
				}
			}
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
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
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
	public Response bet(@QueryParam("gameId") long gameId,
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
			for (Table table : TableWebsocket.games) {
				if (table.getGame().getId() == gameId) {
					table.sendToAll("");
				}
			}
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
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/LeaveGame")
	@Produces("application/json")
	public Response leaveGame(@QueryParam("playerId") String playerId) {
		// TODO Check if we can merge it with the close the game function
		gameService = new GameServiceImpl();
		playerActionService = new PlayerActionServiceImpl();
		Player player = playerActionService.getPlayerById(playerId);
		player.setGameId(0);
		player.setGamePosition(0);
		player.setSittingOut(false);
		player.setTotalChips(player.getChips() + player.getTotalChips());
		PlayerDaoImpl pdao = new PlayerDaoImpl();
		pdao.merge(player, null);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", false);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			for (Table table : TableWebsocket.games) {
				if (table.getGame().getId() == player.getGameId()) {
					table.sendToAll("");
				}
			}
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

		return Response.ok(json, MediaType.APPLICATION_JSON).build();
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
	public Response sitIn(@QueryParam("playerId") String playerId,
			@QueryParam("gameId") long gameId, @QueryParam("chips") int chips,
			@QueryParam("nickname") String nickname) {
		playerActionService = new PlayerActionServiceImpl();
		gameService = new GameServiceImpl();
		Player player = playerActionService.getPlayerById(playerId);
		/**
		 * Player is already in other game, should leave the other game first
		 */
		if (player.getGameId() != gameId && player.getGameId() != 0)
			return Response
					.serverError()
					.entity("Unauthorised Access to the Game. You are already in a different game")
					.build();
		// TODO Neil: check for sufficient fund here private boolean
		// checkSufficientFundToJoinAGame(Player p, Game g) if not ERROR then
		// sit
		/**
		 * Player decides to sit back in the game he/she sat out
		 */
		if (player.getGameId() != 0 && player.isSittingOut())
			try {
				player = playerActionService.sitIn(player);
			} catch (AMSException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
		else {
			player.setChips(chips);
			player.setTotalChips(player.getTotalChips() - player.getChips());
		}
		/**
		 * Player JOINs a game
		 */
		player.setName(nickname);
		GameDaoImpl gdo = new GameDaoImpl();
		gameService.addNewPlayerToGame(gdo.findById(gameId, null), player);
		Gson gson = new Gson();
		String json = "";
		json = gson.toJson(Collections.singletonMap("success", true));
		for (Table table : TableWebsocket.games) {
			if (table.getGame().getId() == gameId) {
				table.sendToAll("");
			}
		}
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
}
