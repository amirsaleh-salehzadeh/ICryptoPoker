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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tools.AMSException;

import common.user.UserPassword;

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.HandDaoImpl;
import game.poker.holdem.domain.BlindLevel;
import game.poker.holdem.domain.CommonTournamentFormats;
import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.GameStatus;
import game.poker.holdem.domain.GameStructure;
import game.poker.holdem.domain.GameType;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.service.GameServiceImpl;
import game.poker.holdem.service.PokerHandServiceImpl;
import game.poker.holdem.util.GameUtil;
import hibernate.user.UserDAO;

/**
 * Controller class that will handle the API interactions with the front-end for
 * the GameController. The game controller is the device that handles the
 * community cards, setting up the game, dealing, etc. This will not be the
 * controller for specific player actions, but for actions that effect the game
 * at a higher level.
 * 
 * @author jacobhyphenated
 */
@Path("GetGameServiceWS")
public class GameServiceWS {

	private GameServiceImpl gameService;
	private PokerHandServiceImpl handService;

	/**
	 * Get a list of currently available game structures <br />
	 * <br />
	 * The standard URL Request to the path /structures with no parameters.
	 * 
	 * @return The response is a JSON array of {@link CommonTournamentFormats}
	 *         objects in JSON Object form. Each object will contain a "name"
	 *         that is the unique identifier for that format type.
	 */
	@GET
	@Path("/GetGameStructures")
	@Produces("application/json")
	public String getGameStructures() {
		List<CommonTournamentFormats> structures = Arrays
				.asList(CommonTournamentFormats.values());
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(structures);
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
	@Path("/GetAllGames")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllGames() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		GameDaoImpl gameDao = new GameDaoImpl();
		try {
			json = mapper.writeValueAsString(gameDao.getAllGames(null));
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
	 * Create a new game based on the parameters from the URL Request <br />
	 * <br />
	 * The standard URL Request to the path /create with two parameters, like:
	 * pokerserverurl
	 * .com/create?gameName=MyPokerGame&gameStructure=TWO_HR_SEVENPPL <br />
	 * <br />
	 * Use the Spring to leverage the Enum type conversions. Return JSON
	 * response with one value, gameId.
	 * 
	 * @param gameName
	 *            Name to identify this game
	 * @param gameStructure
	 *            Type of the game that will be played
	 * @return {"gameId":xxxx}. The Java Method returns the Map<String,Long>
	 *         which is converted by Spring to the JSON object.
	 */
	@POST
	@Path("/CreateGame")
	@Produces("application/json")
	public String createGame(@FormParam("gameName") String gameName,
			@FormParam("gameType") int gameType,
			@FormParam("blindLevel") String blindLevel) {
		// CommonTournamentFormats gameStructure,
		// http://localhost:8080/ICryptoPoker/REST/GetGameServiceWS/CreateGame?gameName=hshshs&description=hfhfhf&timeInMins=33&startingChips=400
		Game game = new Game();
		GameDaoImpl gameDao = new GameDaoImpl();
		game.setName(gameName);
		GameStructure gs = new GameStructure();
		if (gameType == 0) {
			game.setGameType(GameType.CASH);
			gs.setCurrentBlindLevel(BlindLevel.valueOf(blindLevel));
		} else {
			game.setGameType(GameType.TOURNAMENT);
			// Until Cash games are
			// supported
			// gs.setBlindLength(gameStructure.getTimeInMinutes());
			// gs.setBlindLevels(gameStructure.getBlindLevels());
			// gs.setStartingChips(gameStructure.getStartingChips());
		}
		game.setGameStructure(gs);
		game.setPlayersRemaining(0);
		game.setStarted(false);
		game = gameDao.save(game, null);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(game);
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
	 * Get the status of the game. List the status code as well as a list of all
	 * players in the game. <br />
	 * <br />
	 * The response will be a JSON Object containing the status, a list of
	 * player JSON objects, the big blind, the small blind, the pot size for the
	 * hand and the board cards (if a hand is in progress), and the number of
	 * milliseconds left for the current blind level.
	 * 
	 * @param gameId
	 *            unique identifier for the game
	 * @return JSON Object of the format:
	 *         {gameStatus:xxx,smallBlind:xx,bigBlind:xx,blindTime:xxx,pot:xxx,
	 *         players:[{name:xxx,chips:xxx,finishPosition:xxx,gamePosition:xxx,
	 *         sittingOut:false},...],cards:[Xx,Xx...]}
	 */
	@GET
	@Path("/GetGameStatus")
	@Produces("application/json")
	public String getGameStatus(@QueryParam("gameId") long gameId) {
		gameService = new GameServiceImpl();
		Game game = gameService.getGameById(gameId, true);
		GameStatus gs = GameUtil.getGameStatus(game);
		Collection<Player> players = game.getPlayers();

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("gameStatus", gs);
		results.put("players", players);
		if (game.getCurrentHand() != null)
			results.put("handId", game.getCurrentHand().getId());
		else
			results.put("handId", 0);
		// Before the game is started, there is no current blind level set.
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
					.getBoardCards());
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(results);
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
	 * Start the game. This should be called when the players have joined and
	 * everyone is ready to begin.
	 * 
	 * @param gameId
	 *            unique ID for the game that is to be started
	 * @return Map representing a JSON string with a single field for "success"
	 *         which is either true or false. example: {"success":true}
	 */

	@GET
	@Path("/StartGame")
	@Produces(MediaType.APPLICATION_JSON)
	public Response startGame(@QueryParam("gameId") long gameId) {
		gameService = new GameServiceImpl();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			Game game = gameService.getGameById(gameId, false);
			if (!game.isStarted()) {
				game = gameService.startGame(game);
			}
			json = mapper.writeValueAsString(game);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AMSException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Start a new hand. This method should be called at the start of the game,
	 * or when a hand is finished and a new hand needs to be dealt.
	 * 
	 * @param gameId
	 *            unique Id for the game with the hand to be dealt
	 * @return Map translated to a JSON string with a single field for handId of
	 *         the new hand. Example: {"handId":xxx}
	 */

	@GET
	@Path("/StartHand")
	@Produces("application/json")
	public String startHand(@QueryParam("gameId") long gameId) {
		gameService = new GameServiceImpl();
		handService = new PokerHandServiceImpl();
		Game game = gameService.getGameById(gameId, false);
		HandEntity hand = new HandEntity();
		HandDaoImpl hdao = new HandDaoImpl();
		if (game.getCurrentHand() != null && game.getCurrentHand().getId() > 0)
			hand = hdao.findById(game.getCurrentHand().getId(), null);
		else
			hand = handService.startNewHand(game);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(hand);
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
	 * Deal the flop for the hand. This should be called when preflop actions
	 * are complete and the the players are ready to deal the flop cards
	 * 
	 * @param handId
	 *            unique ID for the hand where the flop is being dealt
	 * @return A map represented as a JSON String for the three cards dealt on
	 *         the flop. The cards are denoted by the rank and the suit, with
	 *         <em>2-9,T,J,Q,K,A</em> as the rank and <em>c,s,d,h</em> as the
	 *         suit. For example: Ace of clubs is <em>Ac</em> and Nine of
	 *         Diamonds is <em>9d</em> <br />
	 * <br />
	 *         The json field values are card1, card2, card3. Example:
	 *         {"card1":"Xx","card2":"Xx","card3":"Xx"}
	 */

//	@GET
//	@Path("/Flop")
//	@Produces("application/json")
//	public String flop(@QueryParam("gameId") long gameId) {
//		Game game = gameService.getGameById(gameId, false);
////		hand = handService.flop(game);
//		Map<String, String> result = new HashMap<String, String>();
////		result.put("card1", hand.getBoard().getFlop1().toString());
////		result.put("card2", hand.getBoard().getFlop2().toString());
////		result.put("card3", hand.getBoard().getFlop3().toString());
//		ObjectMapper mapper = new ObjectMapper();
//		String json = "";
//		try {
//			json = mapper.writeValueAsString(result);
//		} catch (JsonGenerationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return json;
//	}

	/**
	 * Deal the turn for the hand. This should be called when the flop actions
	 * are complete and the players are ready for the turn card to be dealt.
	 * 
	 * @param handId
	 *            unique ID for the hand to receive the turn card.
	 * @return Map represented as a JSON String for the turn card, labeled as
	 *         card4. Example: {"card4":"Xx"}
	 */

//	@GET
//	@Path("/Turn")
//	@Produces("application/json")
//	public String turn(@QueryParam("handId") long handId) {
//		HandEntity hand = handService.getHandById(handId);
////		hand = handService.turn(hand);
//		ObjectMapper mapper = new ObjectMapper();
//		String json = "";
//		try {
//			json = mapper.writeValueAsString(Collections.singletonMap("card4",
//					hand.getBoard().getTurn().toString()));
//		} catch (JsonGenerationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return json;
//	}

	/**
	 * Deal the river card for the hand. This should be called when the turn
	 * action is complete and the players are ready for the river card to be
	 * dealt.
	 * 
	 * @param handId
	 *            Unique ID for the hand to receive the river card
	 * @return Map represented as a JSON String for the river card, labeled as
	 *         card5. Example: {"card5":"Xx"}
	 */

//	@GET
//	@Path("/River")
//	@Produces("application/json")
//	public String river(@QueryParam("handId") long handId) {
//		HandEntity hand = handService.getHandById(handId);
////		hand = handService.river(hand);
//		ObjectMapper mapper = new ObjectMapper();
//		String json = "";
//		try {
//			json = mapper.writeValueAsString(Collections.singletonMap("card5",
//					hand.getBoard().getRiver().toString()));
//		} catch (JsonGenerationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return json;
//	}

	/**
	 * End the hand. This completes all actions that can be done on the hand.
	 * The winners are determined and the chips given to the appropriate
	 * players. This will detach the hand from the game, and no more actions may
	 * be taken on this hand.
	 * 
	 * @param handId
	 *            Unique ID for the hand to be ended
	 * @return Map represented as a JSON String determining if the action was
	 *         successful. Example: {"success":true}
	 */
//	@GET
//	@Path("/EndHand")
//	@Produces("application/json")
//	public Response endHand(@QueryParam("handId") long handId) {
//		handService = new PokerHandServiceImpl();
//		HandEntity hand = handService.getHandById(handId);
//		try {
//			handService.endHand(hand);
//		} catch (AMSException e1) {
//			return Response.serverError().entity(e1.getMessage()).build();
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		String json = "";
//		try {
//			json = mapper.writeValueAsString(Collections.singletonMap(
//					"success", true));
//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return Response.ok(json, MediaType.APPLICATION_JSON).build();
//	}

	/**
	 * Puts the current player to act in a sit-out state. Action will move to
	 * the next player. This action requires the handId parameter, so it should
	 * only be exposed to the Game Controller.
	 * 
	 * @param handId
	 *            Current hand where the current player to act will be sat out.
	 * @return {"success":true} if the player was sat out. Error otherwise.
	 */

	@GET
	@Path("/SitOutCurrentPlayer")
	@Produces("application/json")
	public String sitOutCurrentPlayer(@QueryParam("handId") long handId) {
		HandEntity hand = handService.getHandById(handId);
		boolean result = handService.sitOutCurrentPlayer(hand);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(Collections.singletonMap(
					"success", result));
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
	 * Sometimes it is nice to know that everything is working
	 * 
	 * @return {"success":true}
	 */

	@GET
	@Path("/PingServer")
	@Produces("application/json")
	public String pingServer() {
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
