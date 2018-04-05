package services.websockets;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import common.game.poker.holdem.GameENT;
import tools.AMSException;

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.domain.Table;
import game.poker.holdem.service.GameServiceImpl;

@ServerEndpoint("/game/{guid}/{uid}")
public class TableWebsocket {
	public static Set<Table> games = Collections
			.synchronizedSet(new HashSet<Table>());

	@OnOpen
	public void handleOpen(Session user, @PathParam("uid") String uid,
			@PathParam("guid") long guid) {
		System.out.println("open");
		Table table = new Table();
		for (Table t : games) {
			if (t.getGame().getId() == guid) {
				table = t;
			}
		}
		if (table.getPlayerSessions().size() <= 0) {
			GameServiceImpl gservice = new GameServiceImpl();
			GameDaoImpl gdao = new GameDaoImpl();
			GameENT game = gdao.findById(guid, null);
			if (game.isStarted())
				gservice.closeTheGame(game, null);
		}
		table.setGameId(guid);
		table.addPlayer(uid, user);
		games.add(table);
		for (Table t : games) {
			if (t.getGame().getId() == guid) {
				t.sendToAll(uid);
			}
		}
		System.out.println(uid + " has joined");
	}

	@OnMessage
	public void handleMessage(String message, Session userSession,
			@PathParam("uid") String uid, @PathParam("guid") long guid) {
		for (Table table : games) {
			if (table.getGame().getId() == guid) {
				table.sendToAll(uid);
			}
		}
	}

	@OnClose
	public void handleClose(Session user, @PathParam("uid") String uid,
			@PathParam("guid") long guid) {
		System.out.println("close");
		for (Table table : games) {
			if (table.getGame().getId() == guid) {
				table.removePlayer(uid);
				System.out.println(uid + " has left");
				if (table.getPlayerSessions() == null
						|| table.getPlayerSessions().size() == 0) {
					GameENT g = table.getGame();
					// GameDaoImpl gdao = new GameDaoImpl();
					GameServiceImpl gservice = new GameServiceImpl();
					gservice.closeTheGame(g, null);
					// g.setCurrentHand(null);
					// g.setPlayerInBTN(null);
					// g.setPlayersRemaining(0);
					// g.setStarted(false);
					// gdao.merge(g, null);
					games.remove(table);
					System.out.println("game removed");
				}
			}
		}

	}

	@OnError
	public void handleError(Throwable t) {
		System.out.println("error");
		t.printStackTrace();
	}

}
