<%@page import="game.poker.holdem.domain.Player"%>
<%@page import="game.poker.holdem.domain.Game"%>
<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<<<<<<< HEAD

=======
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
>>>>>>> origin/AmirV1
<link rel="stylesheet" href="css/game/table.holdem.css" />
<link rel="stylesheet" href="css/game/table.timer.css" />
<link rel="stylesheet"
	href="css/game/table.raise.action.controllers.css" />
<<<<<<< HEAD
<script src="js/game/table.js"></script>
<script src="js/game/game.js"></script>
<script src="js/jquery/jquery-ui.js"></script>
=======
<!-- <link rel="stylesheet" href="css/icryptopokermaincss.css"> -->
<script src="js/jquery/jquery.min.js"></script>
<script src="js/jquery/jquery.mobile-1.4.5.min.js"></script>
<script src="js/game/table.js"></script>
<script src="js/game/game.js"></script>
<!-- <script src="js/game/vertical-slider.jquery.js"></script> -->
<script src="js/icryptopokermainscripts.js"></script>

<!-- <script src="js/jquery/jquery-ui.js"></script> -->
>>>>>>> origin/AmirV1
<%
	Player player = (Player) request.getAttribute("player");
	Game game = (Game) request.getAttribute("game");
%>
<<<<<<< HEAD
<script>
	$(document).ready(
			function() {
				$('.slider-handle').draggable(
						{
							containment : 'parent',
							axis : 'y',
							drag : function(e, ui) {
								if (!this.par) {
									this.par = $(this).parent();
									this.parHeight = this.par.height();
									this.height = $(this).height();
									this.color = $.trim(this.par.attr('class')
											.replace('colorful-slider', ''));
								}

								var ratio = 1 - (ui.position.top + this.height)
										/ this.parHeight;
								console.log(ratio);
							}
						});
			});
</script>
<input type="hidden" id="isStarted" value="<%=game.isStarted()%>">
<input type="hidden" id="playerPot" value="<%=player.getChips()%>">
<input type="hidden" id="gameID" value="<%=game.getId()%>">
<input type="hidden" id="boardID" value="<%=game.getId()%>">
<input type="hidden" id="handID" value="">
<input type="hidden" id="playerID"
	value="<%=request.getParameter("playerName")%>">
<div id="gamePlayScreen">
	<div class="ui-block-solo ui-grid-c constantBannersTop">
		<div class="ui-block-a" onclick="leaveTable()"
			style="cursor: pointer;">
			<img alt="" src="images/icons/undo.png" height="80%">&nbsp;
			Lobby
		</div>
		<div class="ui-block-b" id="playerChipsDiv">
			<img alt="" src="images/game/money.png" height="80%">&nbsp;$&nbsp;<%=player.getTotalChips()%>

		</div>
		<div class="ui-block-c" id="playerNameDiv">
			<img alt="" src="images/game/user.png" height="80%">&nbsp;<%=request.getParameter("playerName")%></div>
		<div class="ui-block-d"><%=game.getName()%>
			&nbsp;(<%
				String val = "$"
						+ game.getGameStructure().getCurrentBlindLevel().toString()
								.split("_")[1]
						+ "/$"
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
				<div class="ui-block-solo" style="height: 25%; position: relative;">
				</div>
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
=======
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
					style="width: 100%; height: 60%;">

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
						<!-- 						<div class="ui-block-solo sitPlaceContainer rightSideSits" -->
						<!-- 							style="height: 25%;"> -->
						<!-- 							<div class='sitPlaceThumbnailEmpty'>Waiting</div> -->
						<!-- 						</div> -->
					</div>
				</div>

				<!--  BOTTOM GRID -->

				<div class="ui-block-solo ui-grid-b" style="height: 20%;">
					<div class="ui-block-a"
						style="width: 20%; height: 100% !important;"></div>
					<div class="ui-block-b sitPlaceContainer bottomSideSits"
						style="height: 100% !important; width: 40%;" id="userSitPlace"></div>
					<div class="ui-block-c"
						style="width: 40%; height: 100% !important;"
						id="raiseSliderContainer">
						<input type="range" name="sliderRaise" id="sliderRaise"
							data-show-value="true" data-popup-enabled="true"> 
<!-- 							<input -->
<!-- 							type="number" id="betAmount" value="0"> -->
>>>>>>> origin/AmirV1
					</div>
				</div>
			</div>

<<<<<<< HEAD
			<!-- RIGHT GRID  -->

			<div class="ui-block-c" style="width: 20%; height: 100%;">
				<div class="ui-block-solo" style="height: 25%;">
					<a href="#" data-role="button" data-mini="true" onclick="allIn()"
						id="allInBTN" class="actionButtons">All-In</a>
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
				<input type="number" id="betAmount"
				value="0">
				</div>
			</div>
		</div>
		<div class="ui-block-b" id="rightSideToolBar">
			<div class="colorful-slider blue">
				<div class="slider-handle"></div>
			</div>
		</div>
	</div>


	<!-- bottom -->


	<div class=" ui-block-solo ui-grid-c constantBannersBottom"
		id="buttonsContainerDIV">
		<div class="ui-block-a" style="width: 40%"></div>
		<div class="ui-block-b" style="width: 15%">
			<a href="#" data-role="button" data-mini="true" class="actionButtons" onclick="fold()"
				id="foldBTN">Fold</a>
		</div>
		<div class="ui-block-c" style="width: 15%">
			<a href="#" data-role="button" data-mini="true" class="actionButtons" onclick="check()"
				id="checkBTN">Check</a>
		</div>
		<div class="ui-block-d" style="width: 30%">
			<a href="#" data-role="button" data-mini="true" class="actionButtons" onclick="raise()"
				id="raiseBTN">Raise</a> 
		</div>
	</div>
</div>
=======
			<!-- bottom -->

			<div class="ui-block-solo ui-grid-d constantBannersBottom"
				id="buttonsContainerDIV" style="width: 100%; height: 14%;">
				<div class="ui-block-a" style="width: 40%"></div>
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
	</div>
</body>
</html>
>>>>>>> origin/AmirV1
