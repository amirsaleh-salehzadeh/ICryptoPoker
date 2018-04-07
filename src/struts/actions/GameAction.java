/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package struts.actions;

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.PlayerDaoImpl;
import game.poker.holdem.domain.Player;
import game.poker.holdem.service.GameServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.game.poker.holdem.GameENT;

public class GameAction extends Action {
	private String success = "";
	private String error = "";

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String reqCode = request.getParameter("reqCode");
		// try {
		// request.login("admin", "111");
		// System.out.println(request.getRemoteUser());
		// } catch (ServletException e) {
		// return new ActionRedirect("/login.html");
		// // e.printStackTrace();
		// }
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		if (reqCode == null || reqCode.equalsIgnoreCase("")) {
			Player player = playerDaoImpl.findById(
					request.getParameter("username"), null);
			request.setAttribute("player", player);
			reqCode = "goToLobby";
		}
		if (reqCode.equalsIgnoreCase("joinAGame")) {
			GameDaoImpl gamedao = new GameDaoImpl();
			long gameId = Long.parseLong(request.getParameter("gameId"));
			GameENT game = gamedao.findById(gameId, null);
			Player player = playerDaoImpl.findById(
					request.getParameter("playerName"), null);
			player.setName(request.getParameter("nickname"));
			player = playerDaoImpl.merge(player, null);
//			GameServiceImpl gameService = new GameServiceImpl();
//			player = gameService.addNewPlayerToGame(game, player);
			request.setAttribute("game", game);
			request.setAttribute("player", player);
		}
		// startNewHand
		return mapping.findForward(reqCode);
	}
}