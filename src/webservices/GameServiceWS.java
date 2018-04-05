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
import common.game.poker.holdem.GameENT;
import common.user.UserPassword;

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.HandDaoImpl;
import game.poker.holdem.domain.BlindLevel;
import game.poker.holdem.domain.CommonTournamentFormats;
import game.poker.holdem.domain.GameStatus;
import game.poker.holdem.domain.GameStructure;
import game.poker.holdem.domain.GameType;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.service.GameServiceImpl;
import game.poker.holdem.service.PokerHandServiceImpl;
import game.poker.holdem.util.GameUtil;
import hibernate.user.UserDAO;




@Path("GetGameServiceWS")
public class GameServiceWS {

	private GameServiceImpl gameService;
	private PokerHandServiceImpl handService;

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

	@POST
	@Path("/CreateGame")
	@Produces("application/json")
	public String createGame(@FormParam("gameName") String gameName,
			@FormParam("gameType") int gameType,
			@FormParam("blindLevel") String blindLevel) {
		// CommonTournamentFormats gameStructure,
		// http://localhost:8080/ICryptoPoker/REST/GetGameServiceWS/CreateGame?gameName=hshshs&description=hfhfhf&timeInMins=33&startingChips=400
		GameENT game = new GameENT();
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
