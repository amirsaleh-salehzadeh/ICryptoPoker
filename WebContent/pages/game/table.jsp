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
<script src="js/icryptopokermainscripts.js"></script>
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
			<div data-role="header" class="constantBannersTop">
				<div data-role="navbar" class="constantBannersTop" data-grid="c">
					<ul>
						<li><a href="#" id="toLobbyIcon" data-icon="custom"
							onclick="leaveTable()">Lobby</a></li>
						<li><a href="#" id="chipsIcon" data-icon="custom">&nbsp;&cent;&nbsp;<%=player.getTotalChips()%></a>
						</li>
						<li><a href="#" id="userIcon" data-icon="custom">&nbsp;<%=request.getParameter("playerName")%></a></li>
						<li><a href="#" id="gameIcon" data-icon="custom"> <%=game.getName()%>
						</a></li>
					</ul>
				</div>
			</div>


			<!-- MIDDLE PANEL  -->


			<div class="ui-block-solo" id="mainBoardContainerDIV" role="main">

				<!-- TOP GRID  -->

				<div class="ui-grid-c ui-block-solo"
					style="width: 100%; height: 20%;">
					<div class="ui-block-a sitPlaceContainer topSideSits"
						style="width: 25%; height: 100%;" data-sort='4'>
						<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
					</div>
					<div class="ui-block-b sitPlaceContainer topSideSits"
						style="width: 25%; height: 100%;" data-sort='5'>
						<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
					</div>
					<div class="ui-block-c sitPlaceContainer topSideSits"
						style="width: 25%; height: 100%;" data-sort='6'>
						<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
					</div>
					<div class="ui-block-d sitPlaceContainer topSideSits"
						style="width: 25%; height: 100%;" data-sort='7'>
						<div class='sitPlaceThumbnailEmpty'>&nbsp;</div>
					</div>
				</div>


				<!-- MIDDLE GRID  -->


				<div class="ui-grid-b ui-block-solo"
					style="width: 100%; height: 60%;">

					<!-- MIDDLE-LEFT GRID  -->

					<div class="ui-block-a" style="width: 20%; height: 100%;">
						<div class="ui-block-solo sitPlaceContainer leftSideSits"
							style="height: 33% !important;" data-sort='3'>
							<div class='sitPlaceThumbnailEmpty'></div>
						</div>
						<div class="ui-block-solo sitPlaceContainer leftSideSits"
							style="height: 33% !important;" data-sort='2'>
							<div class='sitPlaceThumbnailEmpty'></div>
						</div>
						<div class="ui-block-solo sitPlaceContainer leftSideSits"
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
						<div class="ui-block-solo sitPlaceContainer rightSideSits"
							style="height: 33%;" data-sort='8'>
							<div class='sitPlaceThumbnailEmpty' >&nbsp;</div>
						</div>
						<div class="ui-block-solo sitPlaceContainer rightSideSits"
							style="height: 33%;" data-sort='9'>
							<div class='sitPlaceThumbnailEmpty' >&nbsp;</div>
						</div>
						<div class="ui-block-solo sitPlaceContainer rightSideSits"
							style="height: 33%;">
							<!-- 							<div class='sitPlaceThumbnailEmpty'></div> -->
						</div>
					</div>
				</div>



				<!--  BOTTOM GRID -->



				<div class="ui-block-solo ui-grid-b" style="height: 20%;">
					<div class="ui-block-a"
						style="width: 20%; height: 100% !important;">
						<div id="chatBoxContainer">This is the chat box</div>
						<div class="ui-block-solo smallIcon">
							<img src="images/game/sb.png" width="20">&nbsp;&cent;<%
								out.write(game.getGameStructure().getCurrentBlindLevel().toString()
										.split("_")[1]);
							%>&nbsp;&nbsp;
						</div>
						<div class="ui-block-solo smallIcon">
							<img src="images/game/bb.png" width="20">&nbsp;&cent;<%
								out.write(game.getGameStructure().getCurrentBlindLevel().toString()
										.split("_")[2]);
							%>
						</div>
					</div>
					<div class="ui-block-b sitPlaceContainer bottomSideSits"
						style="height: 100% !important; width: 40%; cursor: pointer;" id="userSitPlace" onclick="sitInPopupOpen()">
						<div class='sitPlaceThumbnailEmpty' data-sort='9'>SEAT</div>
					</div>
					<div class="ui-block-c"
						style="width: 40%; height: 100% !important;"
						id="raiseSliderContainer">
						<input class="actionButtons" type="range" name="sliderRaise"
							id="sliderRaise" data-show-value="true" data-popup-enabled="true">
					</div>
				</div>

			</div>


			<!-- bottom -->

			<div data-role="footer" class="ui-grid-d constantBannersBottom"
				id="buttonsContainerDIV">
				<div class="ui-block-a utilBtns" style="width: 40%">
					<div class="ui-grid-a">
						<div id="btnChat" class="ui-block-a">
							<a href="" onclick="toggleChat();"><img
								src="images/game/chatBox.png" style="width: 100%;" alt="Chat" /></a>
						</div>
						<div id="btnFullScreen" class="ui-block-b">
							<a href="" onclick="toggleFullScreen(document.body);"><img
								src="images/icons/FullScreen.png" style="width: 100%;"
								alt="Full Screen" /></a>

						</div>
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
						<label for="buyIn">Entry Chips</label> <input type="number"
							data-mini="true" id="sitInChips" value="0" placeholder="Chips">

					</div>
					<div class="ui-block-solo" style="height: 25%;">
						<label for="buyIn">Nick Name</label> <input type="text"
							data-mini="true" id="nickname" value="<%=player.getName()%>"
							placeholder="Nick Name">

					</div>
				</div>
			</form>
			            <a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b"
				data-rel="back" data-mini="true">Leave Game</a>         <a href="#"
				id="sitInBTN"
				class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b"
				data-mini="true" onclick="sitInTheGame()">Join</a>     
		</div>
		<!-- 		Sit In popup          -->

	</div>
</body>
</html>