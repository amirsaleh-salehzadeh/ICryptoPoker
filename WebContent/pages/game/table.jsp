<%@page import="game.poker.holdem.domain.Game"%>
<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<link rel="stylesheet" href="css/game/table.holdem.css" />
<script src="js/game/table.js"></script>
<%
	Game game = (Game) request.getAttribute("game");
%>
<div id="gamePlayScreen">
	<div class="ui-block-solo ui-grid-c constantBannersDiv">
		<div class="ui-block-a" onclick="window.location.replace('t_game.do')"
			style="cursor: pointer;">Back to Lobby</div>
		<div class="ui-block-b">
			<img alt="" src="images/game/money.png" width="33px" height="33px">220,000$
		</div>
		<div class="ui-block-c"><%=request.getParameter("playerName")%></div>
		<div class="ui-block-d"><%=game.getName()%></div>

	</div>


	<!-- MIDDLE PANEL  -->


	<div class="ui-block-solo ui-grid-b" id="mainBoardContainerDIV">

		<!-- LEFT GRID  -->

		<div class="ui-block-a" style="width: 20%; height: 100%;">
			<div class="ui-block-solo" style="height: 25%;"></div>
			<div class="ui-block-solo sitPlaceContainer" style="height: 25%;">Player</div>
			<div class="ui-block-solo sitPlaceContainer" style="height: 25%;">Player</div>
			<div class="ui-block-solo" style="height: 25%;"></div>
		</div>

		<!-- CENTER GRID  -->

		<div class="ui-block-b" style="width: 60%; height: 100%;">
			<div class="ui-block-solo ui-grid-b" style="height: 20%;">
				<div class="ui-block-a sitPlaceContainer"  style="height: 100% !important;">Player</div>
				<div class="ui-block-b sitPlaceContainer"  style="height: 100% !important;">Player</div>
				<div class="ui-block-c sitPlaceContainer"  style="height: 100% !important;">Player</div>
			</div>
			<div class="ui-block-solo" style="height: 60%;" id="mainTable">
				<div class="ui-block-solo" id="flopsContainer"></div>
			</div>
			<div class="ui-block-solo ui-grid-b" style="height: 20%;">
				<div class="ui-block-a sitPlaceContainer"
					style="height: 100% !important;">Player</div>
				<div class="ui-block-b sitPlaceContainer"
					style="height: 100% !important;">Player</div>
				<div class="ui-block-c sitPlaceContainer"
					style="height: 100% !important;">Player</div>
			</div>
		</div>

		<!-- RIGHT GRID  -->

		<div class="ui-block-c" style="width: 20%; height: 100%;">
			<div class="ui-block-solo" style="height: 25%;"></div>
			<div class="ui-block-solo sitPlaceContainer" style="height: 25%;">Player</div>
			<div class="ui-block-solo sitPlaceContainer" style="height: 25%;">Player</div>
			<div class="ui-block-solo" style="height: 25%;"></div>
		</div>
	</div>

	<!-- bottom -->


	<div class=" ui-block-solo ui-grid-c constantBannersDiv"
		id="buttonsContainerDIV">
		<div class="ui-block-a">
			<a href="#" data-role="button" data-mini="true" id="foldBTN">Fold</a>
		</div>
		<div class="ui-block-b">
			<a href="#" data-role="button" data-mini="true" id="allInBTN">All-In</a>
		</div>
		<div class="ui-block-c">
			<a href="#" data-role="button" data-mini="true" id="checkBTN">Check</a>
		</div>
		<div class="ui-block-d">
			<a href="#" data-role="button" data-mini="true" id="raiseBTN">Raise</a>
		</div>
	</div>
</div>