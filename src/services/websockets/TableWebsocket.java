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

import game.poker.holdem.domain.Table;

@ServerEndpoint("/game/{guid}/{uid}")
public class TableWebsocket {
	static Set<Table> games = Collections.synchronizedSet(new HashSet<Table>());

	@OnOpen
	public void handleOpen(Session user, @PathParam("uid") String uid,
			@PathParam("guid") long guid) {
		Table table = new Table();
		for (Table t : games) {
			if (t.getGame() == guid) {
				table = t;
			}
		}
		table.setGame(guid);
		table.addPlayer(uid, user);
		games.add(table);
		System.out.println("player has joined");

	}

	@OnMessage
	public void handleMessage(String message, Session userSession,
			@PathParam("uid") String uid, @PathParam("guid") long guid) {
		for (Table table : games) {
			if (table.getGame() == guid) {
				table.sendToAll(uid, message);
			}
		}
	}

	@OnClose
	public void handleClose(Session user, @PathParam("uid") String uid,
			@PathParam("guid") long guid) {
		for (Table table : games) {
			if (table.getGame() == guid) {
				table.removePlayer(uid);
				if (table.getPlayers() == null
						|| table.getPlayers().size() == 0)
					games.remove(table);
			}
		}
		System.out.println("player has left");
	}

	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}

}
