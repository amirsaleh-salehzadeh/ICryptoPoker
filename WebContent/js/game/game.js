function leaveTable() {
	// var url = "/ICryptoPoker/REST/GetGameServiceWS/EndHand?handId="
	// + $("#handID").val();
	// $.ajax({
	// url : url,
	// cache : false,
	// async : true,
	// success : function(data) {
	// if (data.success == true)
	window.location.replace('t_game.do');
	// },
	// error : function(xhr, ajaxOptions, thrownError) {
	// alert(xhr.responseText);
	// }
	// });
}

function updateGameInfo(data) {
	$(".tableCards").each(function() {
		$(this).html('<img alt="" src="images/game/card.jpg">');
	});
	$(".sitPlaceContainer").each(
			function() {
				$(this).html(
						"<div class='sitPlaceThumbnailEmpty'>Waiting</div>")
						.trigger("create");
			});
	$("#handPotContainer")
			.html(
					'<img alt="" src="images/game/stack.png" height="100%"><span>&nbsp;&cent;&nbsp;'
							+ data.pot + '</span>');
	$(".actionButtons").each(function() {
		$(this).addClass("ui-state-disabled");
	});
	$("#raiseSliderContainer").addClass("ui-state-disabled");
	$("#handID").val(data.handId);
	$('.pscontainer').each(function() {
		$(this).attr("class", "pscontainer");
	});
	if (data.gameStatus == "NOT_STARTED") {
		$("#handPotContainer").html('Game is not started');
	} else
		$("#handPotContainer")
				.html(
						'<img alt="" src="images/game/stack.png" height="100%"><span>&nbsp;&cent;&nbsp;'
								+ data.pot + '&nbsp;</span>');
	if (data.gameStatus == "END_HAND") {
		endHand();
	}
	if (data.cards != null)
		$(data.cards).each(function(k, l) {
			generateACard(l, "flopsContainer", k + 1);
		});

	$(data.players).each(function(k, l) {
		updatePlayerInfo(l);
	});
	if ($("#pscontainer" + data.POST_SB).length > 0)
		$("#pscontainer" + data.POST_SB).html(
				'<img src="images/game/sb.png" height="100%" />');
	if ($("#pscontainer" + data.POST_BB).length > 0)
		$("#pscontainer" + data.POST_BB).html(
				'<img src="images/game/bb.png" height="100%" />');
	if ($("#pscontainer" + data.DEALER).length > 0) {
		if ($("#pscontainer" + data.DEALER).html().length > 1)
			$("#pscontainer" + data.DEALER).html(
					$("#pscontainer" + data.DEALER).html()
							+ '<img src="images/game/d.png" height="100%" />');
		else
			$("#pscontainer" + data.DEALER).html(
					'<img src="images/game/d.png" height="100%" />');
	}
	// $("#sliderRaise").attr("min", data.bigBlind * 2); //
	// $("#sliderRaise").attr("max", (data.players[0]).chips); // Sets min, max
	// and
	// // value for bet
	// // slider
	// // (sliderRaise) in
	// // table.jsp
	// $("#sliderRaise").attr("value", data.bigBlind * 2); //
	fitElementsWithinScreen();
}
var playerToActId;
var timer;
function SetTimer() {
	if (timeLeft == 0)
		timeLeft = countDownTotal - 1000;
	else
		timeLeft = timeLeft - 1000;
	$("#timer" + playerToActId).css("width",
			Math.round(timeLeft / countDownTotal * 100) + "%");
	if (timeLeft == 0) {
		clearInterval(timer);
		if (!$("#checkBTN").hasClass("ui-state-disabled")) {
			if ($("#checkBTN").html() == "Check")
				check();
			else
				fold();
		}
	}
}

var countDownTotal = 15000;
var timeLeft = 0;
function updatePlayerInfo(data) {
	var playerId = data.id;
	var playerName = data.name;

	addANewPlayerToTable(playerId, playerName, parseInt(data.chips),
			data.amountToCall);
	if (data.status != "NOT_STARTED" && data.status != "SEATING")
		dealCards2Players(data, playerId);
	//
	// if (data.status == "WAITING" || data.status == "NOT_STARTED") {
	// $('.pscontainer').each(function() {
	// if ("pscontainer" + playerId == this.id) {
	// $(this).html("W");
	// $(this).addClass("waitingChip");
	// }
	// });
	// } else
	if (data.status == "ACTION_TO_CHECK" || data.status == "ACTION_TO_CALL") {
		var atc = parseInt(data.amountToCall);
		if (data.status == "ACTION_TO_CALL") {
			$("#checkBTN").html("Call &cent;" + atc);
			$("#checkBTN").attr("onclick", "call()");
		} else {
			$("#checkBTN").html("Check");
			$("#checkBTN").attr("onclick", "check()").trigger("create");
		}
		if (playerId == $("#playerID").val()) {
			$(".actionButtons").each(function() {
				$(this).removeClass("ui-state-disabled");
			});
			$("#raiseSliderContainer").removeClass("ui-state-disabled");
			if (atc > 0) {
				$("#sliderRaise").attr("min", atc * 2);
				$("#sliderRaise").attr("value", atc);
			} else {
				$("#sliderRaise").attr("min", parseInt(data.bigBlind));
				$("#sliderRaise").attr("value", parseInt(data.bigBlind) * 2);
			}
			$("#sliderRaise").attr("max", parseInt(data.chips)).slider(
					"refresh");
		}

		playerToActId = playerId;
		countDownTotal = 15000;
		timeLeft = 0;
		// clearInterval(timer);
		// setInterval(SetTimer, 1000);
	} else if (data.status == "LOST_HAND") {
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("LOST");
				$(this).addClass("loser");
				$("#amountToCallcontainer" + playerId).html(data.handRank);
			}
		});

	} else if (data.status == "WON_HAND") {
		$('.playerInfo').each(function() {
			if ("playerInfo" + playerId == this.id) {
				$(this).addClass("winner");
				$("#amountToCallcontainer" + playerId).html(data.handRank);
			}
		});
	}
}
function endHand() {
	console.log("GAME DONE");
	// sendText("");
}

function addANewPlayerToTable(id, name, chips, amountToCall) {
	var content = "";
	if (amountToCall != "0")
		amountToCall = "&cent; " + amountToCall;
	else
		amountToCall = "";
	$(".sitPlaceContainer")
			.each(
					function() {
						if (content == "")
							if (id != $("#playerID").val()) {
								content = "<div class='ui-block-solo playerInfo' id='playerInfo"
										+ id
										+ "'>"
										+ "<div class='ui-block-solo w3-light-grey w3-round w3-tiny'>"
										+ "<div class='w3-container w3-round w3-green' style='width:100%; height:5px;' id='timer"
										+ id
										+ "'></div></div>"
										+ "<div class='ui-grid-a ui-block-solo'>"
										+ "<div class='ui-block-a playerTotalChipsPlace'> &cent;"
										+ chips
										+ "</div>"
										+ "<div class='ui-block-b'> "
										+ name
										+ "</div></div>"
										+ "<div class='ui-grid-a ui-block-solo'>"
										+ "<div class='ui-block-a pscontainer' id='pscontainer"
										+ id
										+ "'></div>"
										+ "<div class='ui-block-b amountToCallcontainer' id='amountToCallcontainer"
										+ id
										+ "'>"
										+ amountToCall
										+ "</div></div>"
										+ "<div class='ui-grid-a playerCardsContainer' id='cards"
										+ id
										+ "'><div class='ui-block-a card1 card'></div><div class='ui-block-b card2 card'></div>"
										+ "</div></div>";
							} else {
								content = "<div class='ui-block-a'><div class='ui-block-solo playerInfo' id='playerInfo"
										+ id
										+ "'>"
										+ "<div class='ui-block-solo w3-light-grey w3-round w3-tiny'>"
										+ "<div class='w3-container w3-round w3-green' style='width:100%; height:11px;' id='timer"
										+ id
										+ "'></div></div>"
										+ "<div class='ui-grid-a'>"
										+ "<div class='ui-block-a pscontainer' id='pscontainer"
										+ id
										+ "'></div>"
										+ "<div class='ui-block-b playerTotalChipsPlace'> &cent; "
										+ chips
										+ "</div></div>"
										+ "<div class='ui-block-solo amountToCallcontainer' id='amountToCallcontainer"
										+ id
										+ "'>"
										+ amountToCall
										+ "</div></div></div>"
										+ "<div class='ui-block-b'><div class='ui-grid-a playerCardsContainer' id='cards"
										+ id
										+ "'><div class='ui-block-a card1 card'></div><div class='ui-block-b card2 card'></div>"
										+ "</div></div>";
								$("#userSitPlace").html(content);
								return false;
							}
						if ($(this).children("div").hasClass(
								"sitPlaceThumbnailEmpty")
								&& $("#sitPlaceContainer" + id).length <= 0) {
							$(this).html(content);
							$(this).attr("id", "sitPlaceContainer" + id);
							return false;
						} else if ($("#sitPlaceContainer" + id).length > 0) {
							$("#sitPlaceContainer" + id).html(content);
							return false;
						} else
							return true;
					});

}

function dealCards2Players(l, id) {
	if (l.card1 == "" || l.card2 == "")
		return;
	$('.playerCardsContainer').each(function() {
		if (this.id != null && "cards" + id == this.id) {
			generateACard(l.card1, this.id, 1);
			generateACard(l.card2, this.id, 2);
		}
	});
}

function check() {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/check?gameId="
			+ $("#gameID").val() + "&playerId=" + $("#playerID").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
			if (data.success == true)
				// getGameStatus();
				sendText("Checked");
			else
				alert("check failed");
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
}

function call() {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/call?gameId="
			+ $("#gameID").val() + "&playerId=" + $("#playerID").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
			if (data.success == true)
				// getGameStatus();
				sendText("Called");
			else
				alert("check failed");
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
}

function fold() {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/fold?gameId="
			+ $("#gameID").val() + "&playerId=" + $("#playerID").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
			if (data.success == true)
				// getGameStatus();
				sendText("Folded");
			else
				alert("fold failed");
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
}

function raise() {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/bet?gameId="
			+ $("#gameID").val() + "&playerId=" + $("#playerID").val()
			+ "&betAmount=" + $("#sliderRaise").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
			if (data.success == true)
				// getGameStatus();
				sendText("");
			else
				alert("fold failed");
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
}

function allIn() {

}
