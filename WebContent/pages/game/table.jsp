<%@page import="common.game.poker.holdem.GameENT"%>
<%@page import="game.poker.holdem.domain.Player"%>
<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width initial-scale=1.0 maximum-scale=1.0 user-scalable=0">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" href="css/jquery-mobile/jqm-demos.css">
<link rel="stylesheet" href="css/game/table.holdem.css" />
<link rel="stylesheet" href="css/game/table.timer.css" />
<link rel="stylesheet"
	href="css/game/table.raise.action.controllers.css" />
<!-- <link rel="stylesheet" href="css/icryptopokermaincss.css"> -->
<script src="js/jquery/jquery.min.js"></script>
<script src="js/jquery/jquery.mobile-1.4.5.min.js"></script>
<script src="js/game/table.js"></script>
<script src="js/game/game.js"></script>
<!-- <script src="js/game/vertical-slider.jquery.js"></script> -->
<script src="js/icryptopokermainscripts.js"></script>

<!-- <script src="js/jquery/jquery-ui.js"></script> -->
<%
	Player player = (Player) request.getAttribute("player");
	GameENT game = (GameENT) request.getAttribute("game");
%>
</head>
<body>
	<div data-role="page">
		<div id="gamePlayScreen" data-role="content">
			<input type="hidden" id="isStarted" value="<%=game.isStarted()%>">
			<input type="hidden" id="playerPot" value="<%=player.getChips()%>">
			<input type="hidden" id="gameID" value="<%=game.getId()%>"> <input
				type="hidden" id="boardID" value="<%=game.getId()%>"> <input
				type="hidden" id="handID" value=""> <input type="hidden"
				id="playerID" value="<%=request.getParameter("playerName")%>">
			<div class="ui-block-solo ui-grid-c constantBannersTop">
				<div class="ui-block-a" onclick="leaveTable()"
					style="cursor: pointer;">
					<img alt="" src="images/icons/undo.png" height="80%">&nbsp;
					Lobby
				</div>
				<div class="ui-block-b" id="playerChipsDiv">
					<img alt="" src="images/game/money.png" height="80%">&nbsp;&cent;&nbsp;<%=player.getTotalChips()%>
				</div>
				<div class="ui-block-c" id="playerNameDiv">
					<img alt="" src="images/game/user.png" height="80%">&nbsp;<%=request.getParameter("playerName")%></div>
				<div class="ui-block-d" style="max-height: 100%;">
					<!-- 					<div class="ui-block-solo"> -->
					<%-- 						<%=game.getName()%> --%>
					<!-- 					</div> -->
					<div class="ui-block-solo ui-grid-b"
						style="font-size: 10pt; white-space: nowrap;">
						<div class="ui-block-a">
							<img alt="" src="images/game/sb.png"
								style="height: auto; width: 17px;"> &cent;<%
 	out.write(game.getGameStructure().getCurrentBlindLevel().toString()
 			.split("_")[1]);
 %>
						</div>
						<div class="ui-block-b">
							<img alt="" src="images/game/bb.png"
								style="height: auto; width: 17px;"> &cent;<%
 	out.write(game.getGameStructure().getCurrentBlindLevel().toString()
 			.split("_")[2]);
 %>
						</div>
						<div class="ui-block-c">
							<%=game.getName()%>
						</div>
					</div>
				</div>
			</div>


			<!-- MIDDLE PANEL  -->


			<div class="ui-block-solo" id="mainBoardContainerDIV">

				<!-- TOP GRID  -->

				<div class="ui-grid-c ui-block-solo"
					style="width: 100%; height: 20%;">
					<div class="ui-block-a sitPlaceContainer topSideSits"
						style="width: 25%; height: 100%;">
						<div class='sitPlaceThumbnailEmpty'>Waiting</div>
					</div>
					<div class="ui-block-b sitPlaceContainer topSideSits"
						style="width: 25%; height: 100%;">
						<div class='sitPlaceThumbnailEmpty'>Waiting</div>
					</div>
					<div class="ui-block-c sitPlaceContainer topSideSits"
						style="width: 25%; height: 100%;">
						<div class='sitPlaceThumbnailEmpty'>Waiting</div>
					</div>
					<div class="ui-block-d sitPlaceContainer topSideSits"
						style="width: 25%; height: 100%;">
						<div class='sitPlaceThumbnailEmpty'>Waiting</div>
					</div>
				</div>


				<!-- MIDDLE GRID  -->


				<div class="ui-grid-b ui-block-solo"
					style="width: 100%; height: 55%;">

					<!-- MIDDLE-LEFT GRID  -->

					<div class="ui-block-a" style="width: 20%; height: 100%;">
						<div class="ui-block-solo sitPlaceContainer leftSideSits"
							style="height: 33% !important;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
						<div class="ui-block-solo sitPlaceContainer leftSideSits"
							style="height: 33% !important;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
						<div class="ui-block-solo sitPlaceContainer leftSideSits"
							style="height: 33% !important;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
					</div>


					<!-- MIDDLE-CENTER GRID  -->

					<div class="ui-block-b" style="width: 60%; height: 100%;"
						id="mainTable">
						<div class="ui-block-solo" id="handPotContainer"
							style="height: 20%; width: 100%;"></div>
						<div class="ui-block-solo ui-grid-b"
							style="height: 60%; width: 100%;">
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
						<div class="ui-block-solo" style="height: 20%; width: 100%;">
						</div>
					</div>

					<!-- MIDDLE-RIGHT GRID  -->

					<div class="ui-block-c" style="width: 20%; height: 100%;">
						<div class="ui-block-solo sitPlaceContainer rightSideSits"
							style="height: 33%;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
						<div class="ui-block-solo sitPlaceContainer rightSideSits"
							style="height: 33%;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
						<div class="ui-block-solo sitPlaceContainer rightSideSits"
							style="height: 33%;">
							<div class='sitPlaceThumbnailEmpty'></div>
						</div>
					</div>
				</div>

				<!--  BOTTOM GRID -->

				<div class="ui-block-solo ui-grid-b" style="height: 25%;">
					<div class="ui-block-a"
						style="width: 20%; height: 100% !important;">
						<div id="chatBoxContainer">This is the chat box</div>
					</div>
					<div class="ui-block-b sitPlaceContainer bottomSideSits"
						style="height: 100% !important; width: 40%;" id="userSitPlace"></div>
					<div class="ui-block-c"
						style="width: 40%; height: 100% !important;"
						id="raiseSliderContainer">
						<input class="actionButtons"type="range" name="sliderRaise" id="sliderRaise"
							data-show-value="true" data-popup-enabled="true">
						<!-- 							<input -->
						<!-- 							type="number" id="betAmount" value="0"> -->
					</div>
				</div>

			</div>


			<!-- bottom -->

			<div class="ui-block-solo ui-grid-d constantBannersBottom"
				id="buttonsContainerDIV" style="width: 100%; height: 14%;">
				<div class="ui-block-a" style="width: 40%">
					<div id="btnChat">
						<a href="" onclick="toggleChat();" ><img
							src="images/game/chatBox.png" style="width: 100%;" alt="Chat" /></a>
					</div>
					<div id="btnFullScreen">
					<a href="" onclick="toggleFullScreen(document.body);" ><img
							src="images/icons/FullScreen.png" style="width: 100%;" alt="Full Screen" /></a>
					
					</div>
				</div>
				<div class="ui-block-b" style="width: 15%">
					<a href="#" data-mini="true"
						class="actionButtons ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b"
						onclick="fold()" id="foldBTN">Fold</a>
				</div>
				<div class="ui-block-c" style="width: 15%">
					<a href="#" data-mini="true"
						class="actionButtons ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b"
						onclick="check()" id="checkBTN">Check</a>
				</div>
				<div class="ui-block-d" style="width: 15%">
					<a href="#" data-mini="true"
						class="actionButtons ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b"
						onclick="raise()" id="raiseBTN">Raise</a>
				</div>
				<div class="ui-block-e" style="width: 15%">
					<a href="#" data-mini="true" onclick="allIn()" id="allInBTN"
						class="actionButtons ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b">All-In</a>
				</div>
			</div>
		</div>
		
		
		
		
<!-- 		Sit In popup          -->

<div data-role="popup" id="popupSitIn" data-overlay-theme="b"
	data-theme="b" data-dismissible="false" style="max-width: 400px;">
	<form action="#" id="sitInGameForm" method="post">
		<div data-role="header" data-theme="a">
			<h1>Sit In</h1>
		</div>
		<div role="main" class="ui-content" data-overlay-theme="b"
			data-theme="b" data-dismissible="false">
			<div class="ui-block-solo" style="height: 25%;">
				<label for="buyIn">Entry Chips</label> <input type="number" data-mini="true"
					id="buyIn" value="0" placeholder="Chips">
			 
			</div>
		</div>
	</form>
	            <a href="#"
		class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b"
		data-rel="back">Leave Game</a>         <a href="#" id="sitInBTN"
		class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b">Join</a>
	    
</div>
<!-- 		Sit In popup          -->

	</div>
</body>
</html>