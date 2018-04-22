<%@page import="game.poker.holdem.domain.Player"%>
<%@page import="common.user.UserENT"%>
<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" href="css/game/lobby.holdem.css" />
<script src="js/game/lobby.js"></script>
<%
	Player p = (Player) request.getAttribute("player");
%>
<!-- <div class="ui-grid-b ui-block-solo ui-body-a ui-corner-all" style="position: fixed; right: 0; left: 0;"> -->
<!-- 	<div class="ui-block-a ui-bar-a"> -->
<!-- 		<img alt="chips" title="Total Chips" src="images/game/user.png" -->
<!-- 			width="33px" height="33px">&nbsp;<span id="name" -->
<%-- 			class="spanTopBannerInfo"><%=p.getId()%><input type="hidden" --%>
<%-- 			value="<%=p.getId()%>" id="playerName" placeholder="Player Name"></span> --%>
<!-- 	</div> -->


<!-- 	USER'S TOTAL CHIPS -->


<!-- 	<div class="ui-block-b ui-bar-a ui-corner-all"> -->
<!-- 		<img alt="chips" title="Total Chips" src="images/game/money.png" -->
<!-- 			width="33px" height="33px">&nbsp;<span id="chips" -->
<%-- 			class="spanTopBannerInfo">&nbsp;$<%=p.getTotalChips()%></span> --%>
<!-- 	</div> -->


<!-- 	CREATE NEW GAME BUTTON AND POPUP -->

<!-- 	<div class="ui-block-c ui-bar-a ui-corner-all"> -->


<!-- 	CREATE NEW BUTTON -->


<!-- 		<a href="#popupCreateNewGame" data-rel="popup" -->
<!-- 			data-position-to="window" data-transition="pop" data-role="button" -->
<!-- 			role="button" -->
<!-- 			class="ui-btn ui-mini ui-icon-plus ui-btn-icon-left ui-shadow-icon ui-corner-all">New -->
<!-- 			Game</a> -->


<!-- 	POPUP DIV CREATE NEW -->


<div data-role="popup" id="popupCreateNewGame" data-dismissible="false"
	style="max-width: 400px;">
	<form action="#" id="createNewGameForm" method="post">
		<div data-role="header" data-theme="a">
			<h1>Create New GAME</h1>
		</div>
		<div role="main" class="ui-content" data-overlay-theme="a"
			data-theme="a" data-dismissible="false">
			<div class="ui-field-contain">
				<label for="gameType">Game Type</label> <select id="gameType"
					name="gameType" data-mini="true" data-inline="true">
					<option value="0" selected="selected">Cash</option>
					<option value="1">Tournament</option>
				</select>   
			</div>
			<div class="ui-field-contain">
				<label for="name">Game Name</label> <input type="text"
					name="gameName" placeholder="Game Name" data-mini="true">
			</div>
			<div class="ui-field-contain">
				<label for="select-SB-CreateNew">Small / Blind</label> <select
					name="blindLevel" id="select-SB-CreateNew" data-mini="true"
					data-inline="true">
					<%
						for (BlindLevel cur : BlindLevel.values()) {
							String val = cur.toString().split("_")[1] + "/"
									+ cur.toString().split("_")[2];
					%>
					<option value="<%=cur.toString()%>">
						<%=val%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
	</form>
	            <a href="#"
		class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-a"
		data-rel="back">Cancel</a>         <a href="#"
		class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-a"
		data-rel="back" onclick="createNewGame()">Save</a>     
</div>
<!-- 	</div> -->
<!-- </div> -->


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
		<tbody id="lobbyTableTBody"></tbody>
	</table>
</div>


<!-- 	POPUP DIV JOIN GAME -->


<div data-role="popup" id="popupJoinGame" data-overlay-theme="b"
	data-theme="b" data-dismissible="false" style="max-width: 400px;">
	<form action="#" id="createNewGameForm" method="post">
		<input type="hidden" name="joinGameID" id="joinGameID" value="1">
		<div data-role="header" data-theme="a">
			<h1>Join Game</h1>
		</div>
		<div role="main" class="ui-content" data-overlay-theme="b"
			data-theme="b" data-dismissible="false">
			<div class="ui-block-solo" style="height: 25%;">
				<label for="buyIn">Entry Chips</label> <input type="number"
					data-mini="true" id="buyIn" value="0" placeholder="Chips">
				<label for="buyIn">Nick Name</label> <input type="text"
					data-mini="true" id="nickname" placeholder="Nickname">
			</div>
		</div>
	</form>
	            <a href="#"
		class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b"
		data-rel="back">Cancel</a>         <a href="#" id="joinGameBTN"
		class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b">Join</a>
	    
</div>
