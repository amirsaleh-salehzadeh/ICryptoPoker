<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" href="css/game/lobby.holdem.css" />
<script type="text/javascript">
$(document).ready(function(){
	var url = "/ICryptoPoker/REST/GetGameServiceWS/GetAllGames";
		$.ajax({
			url : url,
			cache : false,
			async : true,
			beforeSend : function() {
				ShowLoadingScreen();
			},
			success : function(data) {
				var tableRows = "";
				$.each(data, function(k, l) {
					tableRows += "<tr onclick='alert(" + l.id + ")'><td>" + l.name
							+ "</td><td>";
					var smallBig = l.gameStructure.currentBlindLevel.split("_");
					tableRows += smallBig[1] + " / " + smallBig[2] + "</td><td>"
							+ l.gameStructure.startingChips + "</td><td>"
							+ l.players.length + " / 10</td></tr>";
				});
				console.log($("#lobbyTableTBody").html());
				console.log(tableRows);
				$("#lobbyTableTBody").html(tableRows);
				$("#table-lobby").trigger("create");   

			},
			complete : function() {
				HideLoadingScreen();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				popErrorMessage("An error occured while removing the marker. "
						+ thrownError);
			}
		});
		$("#popupConfirmation").popup("close");
});
</script>
<div class="ui-grid-b">
	<div class="ui-block-a">
		<img alt="chips" title="Total Chips" src="images/game/user.png"
			width="33px" height="33px">&nbsp;<span id="name"
			class="spanTopBannerInfo">Amir Saleh</span>
	</div>
	<div class="ui-block-b">
		<img alt="chips" title="Total Chips" src="images/game/money.png"
			width="33px" height="33px">&nbsp;<span id="chips"
			class="spanTopBannerInfo">25,000</span>
	</div>
	<div class="ui-block-c">
		<a href="#" class="ui-btn">Create New Game</a>
	</div>
</div>
<div class="ui-block-solo">
	<table data-role="table" id="table-lobby" class="ui-responsive" data-mode="reflow">
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
