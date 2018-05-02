<%@page import="game.poker.holdem.domain.Player"%>
<%@page import="common.user.UserENT"%>
<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" href="css/game/lobby.holdem.css" />
<script src="js/game/poker/holdem/lobby.js"></script>
<%
	Player p = (Player) request.getAttribute("player");
%>

<!-- 	POPUP DIV CREATE NEW -->

<input type="hidden" value="<%=p.getId()%>" id="playerName">
<div data-role="popup" id="popupCreateNewGame" data-dismissible="false"
	style="max-width: 400px;">
	<form action="#" id="createNewGameForm" method="post">
		<div data-role="header" data-theme="a">
			<h3>Create New GAME</h3>
		</div>
		<div role="main" class="ui-content" data-overlay-theme="a"
			data-theme="a" data-dismissible="false">
			<div class="ui-block-solo ui-field-contain">
				<label for="gameType">Game Type</label> <select id="gameType"
					name="gameType" data-mini="true" data-inline="true">
					<option value="0" selected="selected">Cash</option>
					<option value="1">Tournament</option>
				</select>   
			</div>
			<div class="ui-block-solo">
				<label for="name">Game Name</label> <input type="text"
					name="gameName" placeholder="Game Name" data-mini="true">
			</div>
			<div class="ui-block-solo ui-field-contain">
				<label for="select-SB-CreateNew">Small / Blind</label> <select
					name="blindLevel" id="select-SB-CreateNew" data-mini="true"
					data-inline="true">
					<%
						for (BlindLevel cur : BlindLevel.values()) {
							String val = cur.toString().split("_")[1] + "/"
									+ cur.toString().split("_")[2];
					%>
					<option value="<%=cur.toString()%>"><%=val%></option>
					<%
						}
					%>
				</select>
			</div>
			<div class="ui-grid-a ui-block-solo">
				<div class="ui-block-a">
					<a href="#" class="ui-btn ui-corner-all ui-shadow ui-mini ui-btn-a"
						data-rel="back">Cancel</a>    
				</div>
				<div class="ui-block-b">
					<a href="#" class="ui-btn ui-corner-all ui-shadow ui-mini ui-btn-a"
						data-rel="back" onclick="createNewGame()">Save</a>     
				</div>

			</div>
		</div>
	</form>
</div>


<!-- GAME LIST GRID -->



<div class="tableContainer ui-block-solo">
	<table data-role="table" id="table-lobby" class="ui-responsive"
		data-mode="reflow">
		<thead>
			<tr>
				<th data-priority="2">Table</th>
				<th>Stake</th>
				<th data-priority="3">Min/Max BuyIn</th>
				<th data-priority="1">Players</th>
			</tr>
		</thead>
		<tbody>
			<logic:iterate id="gameListIteration" indexId="rowId" name="games"
				type="common.game.poker.holdem.GameENT">
				<%
					String[] smallBig = gameListIteration.getGameStructure()
								.getCurrentBlindLevel().name().split("_");
				%>
				<tr
					onclick="<%="joinGame(" + gameListIteration.getId() + ","
						+ smallBig[1] + "," + smallBig[2] + ")"%>">
					<td data-priority="2"><%=gameListIteration.getName()%></td>
					<td><%=smallBig[1] + " / " + smallBig[2]%></td>
					<td data-priority="3"><%=Integer.parseInt(smallBig[2]) * 40 + " / "
						+ Integer.parseInt(smallBig[2]) * 200%></td>
					<td data-priority="1"><%=gameListIteration.getPlayersRemaining() + "/10"%></td>
				</tr>
			</logic:iterate>
		</tbody>
	</table>
</div>