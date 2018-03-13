<%@page import="game.poker.holdem.domain.Player"%>
<%@page import="game.poker.holdem.domain.Game"%>
<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<link rel="stylesheet" href="css/game/table.holdem.css" />
<link rel="stylesheet" href="css/game/table.timer.css" />
<link rel="stylesheet" href="css/game/table.raise.slider.css" />
<script src="js/game/table.js"></script>
<script src="js/game/circle.js"></script>
<script src="js/game/game.js"></script>
<script src="js/jquery/jquery-ui.js"></script>
<%
	Player player = (Player) request.getAttribute("player");
	Game game = player.getGame();
	String playerIDs = "";
	for (Player p : game.getPlayers()) {
		if (!p.getId().equalsIgnoreCase(
				request.getParameter("playerName")))
			playerIDs += p.getId() + "," + p.getChips() + ";";
	}
	if (playerIDs.length() > 1)
		playerIDs = playerIDs.substring(0, playerIDs.length() - 1);
	// 	if (request.getParameter("reqCode").equalsIgnoreCase("joinAGame")
	// 			&& !game.isStarted() && game.getPlayers().size() == 2) {
	// 		game.setStarted(false);
	// 	}
%>
<script>
$(document).ready(function(){
	$('.slider-handle').draggable({
		containment:'parent',
		axis:'y',
		drag:function(e,ui){
			if(!this.par)
			{
				this.par = $(this).parent();
				this.parHeight = this.par.height();
				this.height = $(this).height();
				this.color = $.trim(this.par.attr('class').replace('colorful-slider',''));
			}
			
			var ratio = 1-(ui.position.top+this.height)/this.parHeight;
			
// 			resizeBar(this.color,ratio);
		}
	});
});
</script>
<input type="hidden" id="isStarted" value="<%=game.isStarted()%>">
<input type="hidden" id="playerIDs" value="<%=playerIDs%>">
<input type="hidden" id="playerPot" value="<%=player.getChips()%>">
<input type="hidden" id="gameID" value="<%=game.getId()%>">
<input type="hidden" id="boardID" value="<%=game.getId()%>">
<input type="hidden" id="playerID"
	value="<%=request.getParameter("playerName")%>">
<div id="gamePlayScreen">
	<div class="ui-block-solo ui-grid-c constantBannersDiv">
		<div class="ui-block-a" onclick="leaveTable()"
			style="cursor: pointer;">Back to Lobby</div>
		<div class="ui-block-b" id="playerChipsDiv">
			<img alt="" src="images/game/money.png" width="33px" height="33px"><%=player.getChips()%>
		</div>
		<div class="ui-block-c" id="playerNameDiv"><%=request.getParameter("playerName")%></div>
		<div class="ui-block-d"><%=game.getName()%>
			<br />
			<%
				String val = game.getGameStructure().getCurrentBlindLevel()
						.toString().split("_")[1]
						+ "/"
						+ game.getGameStructure().getCurrentBlindLevel().toString()
								.split("_")[2];
				out.write(val);
			%>
		</div>
	</div>


	<div class="ui-block-solo horizontalSpacer"></div>


	<!-- MIDDLE PANEL  -->


	<div class="ui-grid-b" id="middleBlockContainerDIV">

		<div class="ui-block-solo ui-block-a ui-grid-b"
			id="mainBoardContainerDIV">

			<!-- LEFT GRID  -->

			<div class="ui-block-a" style="width: 20%; height: 100%;">
				<div class="ui-block-solo" style="height: 25%;"></div>
				<div class="ui-block-solo sitPlaceContainer" style="height: 25%;">
				</div>
				<div class="ui-block-solo sitPlaceContainer" style="height: 25%;"></div>
				<div class="ui-block-solo" style="height: 25%;"></div>
			</div>

			<!-- CENTER GRID  -->

			<div class="ui-block-b" style="width: 60%; height: 100%;">
				<div class="ui-block-solo ui-grid-b" style="height: 20%;">
					<div class="ui-block-a sitPlaceContainer"
						style="height: 100% !important;"></div>
					<div class="ui-block-b sitPlaceContainer"
						style="height: 100% !important;"></div>
					<div class="ui-block-c sitPlaceContainer"
						style="height: 100% !important;"></div>
				</div>
				<div class="ui-block-solo" style="height: 60%;" id="mainTable">
					<div class="ui-block-solo" style="height: 20%; width: 100%;">
						<img alt="" src="images/game/stack.png" height="100%">roundBetAmount
					</div>
					<div class="ui-block-solo ui-grid-d"
						style="height: 60%; width: 100%;" id="flopsContainer">
						<div class="ui-block-a" id="flop1">
							<img alt="" src="images/game/card.jpg">
						</div>
						<div class="ui-block-b" id="flop2">
							<img alt="" src="images/game/card.jpg">
						</div>
						<div class="ui-block-c" id="flop3">
							<img alt="" src="images/game/card.jpg">
						</div>
						<div class="ui-block-d" id="flop4">
							<img alt="" src="images/game/card.jpg">
						</div>
						<div class="ui-block-e" id="flop5">
							<img alt="" src="images/game/card.jpg">
						</div>
					</div>
					<div class="ui-block-solo" style="height: 20%; width: 100%;">

					</div>
				</div>
				<div class="ui-block-solo ui-grid-b" style="height: 20%;">
					<div class="ui-block-a sitPlaceContainer"
						style="height: 100% !important;"></div>
					<div class="ui-block-b sitPlaceContainer"
						style="height: 100% !important;" id="userSitPlace"></div>
					<div class="ui-block-c sitPlaceContainer"
						style="height: 100% !important;"></div>
				</div>
			</div>

			<!-- RIGHT GRID  -->

			<div class="ui-block-c" style="width: 20%; height: 100%;">
				<div class="ui-block-solo" style="height: 25%;"></div>
				<div class="ui-block-solo sitPlaceContainer" style="height: 25%;"></div>
				<div class="ui-block-solo sitPlaceContainer" style="height: 25%;"></div>
				<div class="ui-block-solo" style="height: 25%;">
					<div id="circle-1" data-circle-dialWidth=10
						data-circle-skin="yellow">
						<div class="loader-bg">
							<div class="text"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="ui-block-b" id="rightSideToolBar">
			<div class="colorful-slider blue">
				<div class="slider-handle"></div>
			</div>
		</div>
	</div>
	<div class="ui-block-solo horizontalSpacer"></div>


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