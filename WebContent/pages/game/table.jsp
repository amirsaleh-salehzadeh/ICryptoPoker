<%@page import="game.poker.holdem.domain.Player"%>
<%@page import="game.poker.holdem.domain.Game"%>
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
<link rel="stylesheet" href="css/icryptopokermaincss.css">
<script src="http://code.jquery.com/jquery-1.6.4.min.js"></script>
<script src="http://code.jquery.com/mobile/1.0/jquery.mobile-1.0.min.js"></script>
<script src="js/game/table.js"></script>
<script src="js/game/game.js"></script>
<script src="js/game/vertical-slider.jquery.js"></script>
<script src="js/icryptopokermainscripts.js"></script>

<!-- <script src="js/jquery/jquery-ui.js"></script> -->
<%
	Player player = (Player) request.getAttribute("player");
	Game game = (Game) request.getAttribute("game");
%>
<script>
	// 	$(document).ready(
	// 			function() {
	// 				$('.slider-handle').draggable(
	// 						{
	// 							containment : 'parent',
	// 							axis : 'y',
	// 							drag : function(e, ui) {
	// 								if (!this.par) {
	// 									this.par = $(this).parent();
	// 									this.parHeight = this.par.height();
	// 									this.height = $(this).height();
	// 									this.color = $.trim(this.par.attr('class')
	// 											.replace('colorful-slider', ''));
	// 								}

	// 								var ratio = 1 - (ui.position.top + this.height)
	// 										/ this.parHeight;
	// 								console.log(ratio);
	// 							}
	// 						});
	// 			});
</script>
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
				<div class="ui-block-d"><%=game.getName()%>
					&nbsp;(<%
						String val = "&cent;"
								+ game.getGameStructure().getCurrentBlindLevel().toString()
										.split("_")[1]
								+ "/&cent;"
								+ game.getGameStructure().getCurrentBlindLevel().toString()
										.split("_")[2];
						out.write(val);
					%>)
				</div>
			</div>


			<!-- MIDDLE PANEL  -->


			<div class="ui-grid-b" id="middleBlockContainerDIV">

				<div class="ui-block-solo ui-block-a ui-grid-b"
					id="mainBoardContainerDIV">

					<!-- LEFT GRID  -->

					<div class="ui-block-a" style="width: 20%; height: 100%;">
						<div class="ui-block-solo" style="height: 25%;"></div>
						<div class="ui-block-solo sitPlaceContainer leftSideSits"
							style="height: 25%;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
						<div class="ui-block-solo sitPlaceContainer leftSideSits"
							style="height: 25%;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
						<div class="ui-block-solo"
							style="height: 25%; position: relative;"></div>
					</div>

					<!-- CENTER GRID  -->

					<div class="ui-block-b" style="width: 60%; height: 100%;">
						<div class="ui-block-solo ui-grid-b" style="height: 20%;">
							<div class="ui-block-a sitPlaceContainer topSideSits"
								style="height: 100% !important;">
								<div class='sitPlaceThumbnailEmpty'>Waiting</div>
							</div>
							<div class="ui-block-b sitPlaceContainer topSideSits"
								style="height: 100% !important;">
								<div class='sitPlaceThumbnailEmpty'>Waiting</div>
							</div>
							<div class="ui-block-c sitPlaceContainer topSideSits"
								style="height: 100% !important;">
								<div class='sitPlaceThumbnailEmpty'>Waiting</div>
							</div>
						</div>
						<div class="ui-block-solo" style="height: 60%;" id="mainTable">
							<div class="ui-block-solo" id="handPotContainer"
								style="height: 20%; width: 100%;"></div>
							<div class="ui-block-solo ui-grid-b"
								style="height: 60%; width: 100%;">
								<div class="ui-block-a" style="height: 100%; width: 20%;"></div>
								<div class="ui-block-b ui-grid-d" id="flopsContainer"
									style="height: 100%; width: 60%;">
									<div class="ui-block-a tableCards" id="flop1">
										<img alt="" src="images/game/card.jpg">
									</div>
									<div class="ui-block-b tableCards" id="flop2">
										<img alt="" src="images/game/card.jpg">
									</div>
									<div class="ui-block-c tableCards" id="flop3">
										<img alt="" src="images/game/card.jpg">
									</div>
									<div class="ui-block-d tableCards" id="flop4">
										<img alt="" src="images/game/card.jpg">
									</div>
									<div class="ui-block-e tableCards" id="flop5">
										<img alt="" src="images/game/card.jpg">
									</div>
								</div>
								<div class="ui-block-c" style="height: 100%; width: 20%;"></div>
							</div>
							<div class="ui-block-solo" style="height: 20%; width: 100%;">
							</div>
						</div>
						<div class="ui-block-solo ui-grid-b" style="height: 20%;">
							<div class="ui-block-a sitPlaceContainer bottomSideSits"
								style="height: 100% !important;">
								<div class='sitPlaceThumbnailEmpty'>Waiting</div>
							</div>
							<div class="ui-block-b sitPlaceContainer bottomSideSits"
								style="height: 100% !important;" id="userSitPlace"></div>
							<div class="ui-block-c sitPlaceContainer bottomSideSits"
								style="height: 100% !important;">
								<div class='sitPlaceThumbnailEmpty'>Waiting</div>
							</div>
						</div>
					</div>

					<!-- RIGHT GRID  -->

					<div class="ui-block-c" style="width: 20%; height: 100%;">
						<div class="ui-block-solo" style="height: 25%;">
							<a href="#" data-mini="true" onclick="allIn()"
								id="allInBTN" class="actionButtons ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b">All-In</a>
						</div>
						<div class="ui-block-solo sitPlaceContainer rightSideSits"
							style="height: 25%;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
						<div class="ui-block-solo sitPlaceContainer rightSideSits"
							style="height: 25%;">
							<div class='sitPlaceThumbnailEmpty'>Waiting</div>
						</div>
						<div class="ui-block-solo" style="height: 25%;">
							<input type="number" id="betAmount" value="0">
						</div>
					</div>
				</div>
				<div class="ui-block-b" id="rightSideToolBar">
					<!-- 			<div class="colorful-slider blue"> -->
					<!-- 				<div class="slider-handle"></div> -->
					<!-- 			</div> -->
					<!-- 			<input type="range" name="slider-mini" id="slider-mini" value="25" -->
					<!-- 				min="0" max="100" data-highlight="true" data-mini="true"> -->
					<input type="range" name="slider" id="slider-0" value="50" step="5"
						min="0" max="100" sliderOrientation="vertical" />

				</div>
			</div>


			<!-- bottom -->


			<div class=" ui-block-solo ui-grid-c constantBannersBottom"
				id="buttonsContainerDIV">
				<div class="ui-block-a" style="width: 40%"></div>
				<div class="ui-block-b" style="width: 15%">
					<a href="#" data-mini="true"
						class="actionButtons ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b" onclick="fold()"
						id="foldBTN">Fold</a>
				</div>
				<div class="ui-block-c" style="width: 15%">
					<a href="#" data-mini="true"
						class="actionButtons ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b" onclick="check()"
						id="checkBTN">Check</a>
				</div>
				<div class="ui-block-d" style="width: 30%">
					<a href="#" data-mini="true"
						class="actionButtons ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b" onclick="raise()"
						id="raiseBTN">Raise</a>
				</div>
			</div>
		</div>
	</div>

</body>
</html>