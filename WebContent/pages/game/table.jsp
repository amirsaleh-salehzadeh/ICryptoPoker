<%@page import="common.game.poker.holdem.GameENT"%>
<%@page import="game.poker.holdem.domain.Player"%>
<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile.icons.min.css">
<link rel="stylesheet" href="css/themes/default/theme-classic.css">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile.structure-1.4.5.min.css">
<script src="js/jquery/jquery.min.js"></script>
<script src="js/jquery/jquery.mobile-1.4.5.min.js"></script>
<script src="js/game/table.js"></script>
<script src="js/game/game.js"></script>
<link rel="stylesheet" href="css/game/table.holdem.css" />
<link rel="stylesheet" href="css/game/table.timer.css" />
<link rel="stylesheet" href="css/game/table.raise.action.controllers.css" />

<%
	Player player = (Player) request.getAttribute("player");
	GameENT game = (GameENT) request.getAttribute("game");
%>
</head>
<body>
	<div data-role="page">
		<div data-role="panel" id="panelChat" data-display="overlay">
			CHAT <a href="#tableFooter" data-mini="true"
				class="ui-btn ui-icon-delete ui-btn-icon-left ui-shadow ui-corner-all"
				data-rel="close">Close Panel</a>
		</div>
		<div data-role="header">
			<a href="#"
				class="ui-btn ui-mini ui-icon-back ui-btn-icon-left ui-shadow-icon ui-corner-all"
				onclick="leaveTable()" >Lobby</a>
			<h3>
				&nbsp;<%=request.getParameter("playerName")%></h3>
			<a href="#" class="ui-btn ui-icon-shop ui-btn-icon-left"
				>&nbsp;&cent;&nbsp;<%=player.getTotalChips()%></a>
		</div>
		<!-- MIDDLE PANEL  -->
		<div role="main" class="ui-content">
			<input type="hidden" id="isStarted" value="<%=game.isStarted()%>">
			<input type="hidden" id="playerPot" value="<%=player.getChips()%>">
			<input type="hidden" id="gameID" value="<%=game.getId()%>"> <input
				type="hidden" id="boardID" value="<%=game.getId()%>"> <input
				type="hidden" id="handID" value=""> <input type="hidden"
				id="playerID" value="<%=request.getParameter("playerName")%>">
			<!-- TOP GRID  -->
			<div class="ui-grid-d ui-block-solo"
				style="width: 100%; height: 20%;">
				<div class="ui-block-a sitPlaceContainer topSideSits"
					style="width: 20%; height: 100%;"></div>
				<div class="ui-block-b sitPlaceContainer"
					style="width: 20%; height: 100%;" data-sort='4'>
					<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
				</div>
				<div class="ui-block-c sitPlaceContainer"
					style="width: 20%; height: 100%;" data-sort='5'>
					<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
				</div>
				<div class="ui-block-d sitPlaceContainer"
					style="width: 20%; height: 100%;" data-sort='6'>
					<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
				</div>
				<div class="ui-block-e" style="width: 20%; height: 100%;"></div>
			</div>
			<!-- MIDDLE GRID  -->
			<div class="ui-grid-b ui-block-solo"
				style="width: 100%; height: 55%;">
				<!-- MIDDLE-LEFT GRID  -->
				<div class="ui-block-a" style="width: 20%; height: 100%;">
					<div class="ui-block-solo sitPlaceContainer"
						style="height: 33% !important;" data-sort='3'>
						<div class='sitPlaceThumbnailEmpty'></div>
					</div>
					<div class="ui-block-solo sitPlaceContainer"
						style="height: 33% !important;" data-sort='2'>
						<div class='sitPlaceThumbnailEmpty'></div>
					</div>
					<div class="ui-block-solo sitPlaceContainer"
						style="height: 33% !important;" data-sort='1'>
						<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
					</div>
				</div>
				<!-- MIDDLE-CENTER GRID  -->
				<div class="ui-block-b" style="width: 60%; height: 100%;"
					id="mainTable">
					<div class="ui-block-solo" id="handPotContainer"
						style="height: 20%; width: 100%;"></div>
					<div class="ui-block-solo ui-grid-b"
						style="height: 70%; width: 100%;">
						<div class="ui-block-a" style="height: 100%; width: 10%;"></div>
						<div class="ui-block-b ui-grid-d" id="flopsContainer"
							style="height: 100%; width: 80%;">
							<div class="ui-block-a tableCards card" id="flop1">
								<img alt="" src="images/game/card.jpg">
							</div>
							<div class="ui-block-b tableCards card" id="flop2">
								<img alt="" src="images/game/card.jpg">
							</div>
							<div class="ui-block-c tableCards card" id="flop3">
								<img alt="" src="images/game/card.jpg">
							</div>
							<div class="ui-block-d tableCards card" id="flop4">
								<img alt="" src="images/game/card.jpg">
							</div>
							<div class="ui-block-e tableCards card" id="flop5">
								<img alt="" src="images/game/card.jpg">
							</div>
						</div>
						<div class="ui-block-c" style="height: 100%; width: 10%;"></div>
					</div>
					<div class="ui-block-solo" style="height: 10%; width: 100%;">
					</div>
				</div>
				<!-- MIDDLE-RIGHT GRID  -->
				<div class="ui-block-c" style="width: 20%; height: 100%;">
					<div class="ui-block-solo sitPlaceContainer" style="height: 33%;"
						data-sort='7'>
						<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
					</div>
					<div class="ui-block-solo sitPlaceContainer" style="height: 33%;"
						data-sort='8'>
						<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
					</div>
					<div class="ui-block-solo sitPlaceContainer" style="height: 33%;"
						data-sort='9'>
						<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
					</div>
				</div>
			</div>
			<!--  BOTTOM GRID -->
			<div class="ui-block-solo ui-grid-b" style="height: 25%;">
				<div class="ui-block-a" style="width: 20%; height: 100% !important;">
					<div class="ui-block-solo ui-bar-a ui-corner-all"
						style="text-align: center;">
						<%=game.getName()%>
					</div>
					<div class="ui-block-solo ">
						Small <img src="images/game/sb.png" width="20">&nbsp;&cent;<%
						out.write(game.getGameStructure().getCurrentBlindLevel().toString()
								.split("_")[1]);
					%>&nbsp;&nbsp;
					</div>
					<div class="ui-block-solo">
						Big <img src="images/game/bb.png" width="20">&nbsp;&cent;<%
						out.write(game.getGameStructure().getCurrentBlindLevel().toString()
								.split("_")[2]);
					%>
					</div>
				</div>
				<div class="ui-block-b sitPlaceContainer "
					style="height: 100% !important; width: 40%;"
					id="userSitPlace">
					<div class='sitPlaceThumbnailEmpty ui-bar ui-corner-all ui-bar-a'
						onclick="sitInPopupOpen()">SEAT</div>
				</div>
				<div class="ui-block-c" style="width: 40%;"
					id="raiseSliderContainer">
					<input class="actionButtons ui-state-disabled" type="range"
						name="sliderRaise" id="sliderRaise" data-show-value="true"
						data-popup-enabled="true">
				</div>
			</div>
			<!-- 		Sit In popup          -->
			<div data-role="popup" id="popupSitIn" data-overlay-theme="b"
				data-dismissible="false">
				<form>
					<div data-role="header" data-theme="a">
						<h1>Sit In</h1>
					</div>
					<div role="main" class="ui-content" data-overlay-theme="b"
						data-dismissible="false">
						<div class="ui-block-solo" style="height: 25%;">
							<label for="buyIn">Entry Chips</label> <input type="number"
								data-mini="true" id="sitInChips" value="100" placeholder="Chips">
						</div>
						<div class="ui-block-solo" style="height: 25%;">
							<label for="buyIn">Nick Name</label> <input type="text"
								data-mini="true" id="nickname" value="<%=player.getId()%>"
								placeholder="Nick Name">
							<!-- 	TODO : CHECK NICKNAME : It should be at least 3 words-->
						</div>

					</div>
					            <a href="#"
						class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-a"
						data-rel="back" data-mini="true">Back</a>         <a href="#"
						id="sitInBTN"
						class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-a"
						data-mini="true" onclick="sitInTheGame()">Join</a>     
				</form>
			</div>
		</div>
		<!-- FOOTER -->
		<div data-role="footer" class="ui-grid-d" id="tableFooter">
			<div class="ui-block-a ui-grid-a" style="width: 32%">
				<div class="ui-block-a">
					<a href="#panelChat" data-mini="true"
						class="ui-btn ui-icon-comment ui-btn-icon-left ui-shadow ui-corner-all"
						onclick="toggleChat();">Chat</a>
				</div>
				<div class="ui-block-b">
					<a href="#" data-mini="true"
						class="ui-btn ui-corner-all ui-icon-plus ui-btn-icon-notext ui-shadow ui-btn-inline ui-btn-a"
						onclick="toggleFullScreen(document.body);">NOTEXT</a>

				</div>
			</div>
			<div class="ui-block-b" style="width: 17%">
				<a href="#" data-mini="true" data-role="button" id="foldBTN"
					class="actionButtons ui-btn ui-icon-minus ui-btn-icon-left ui-shadow ui-corner-all ui-state-disabled"
					onclick="fold()">Fold</a>
			</div>
			<div class="ui-block-c" style="width: 17%">
				<a href="#" data-mini="true" data-role="button" id="checkBTN"
					class="actionButtons ui-btn ui-icon-check ui-btn-icon-left ui-shadow ui-corner-all ui-state-disabled"
					onclick="check()">Check</a>
			</div>
			<div class="ui-block-d" style="width: 17%">
				<a href="#" data-mini="true" data-role="button" id="raiseBTN"
					class="actionButtons ui-btn ui-icon-carat-u ui-btn-icon-left ui-shadow ui-corner-all ui-state-disabled"
					onclick="raise()">Raise</a>
			</div>
			<div class="ui-block-e" data-role="button" style="width: 17%">
				<a href="#" data-mini="true" onclick="allIn()" id="allInBTN"
					class="actionButtons ui-btn ui-icon-action ui-btn-icon-left ui-shadow ui-corner-all ui-state-disabled">All-In</a>
			</div>
		</div>
	</div>
</body>
</html>