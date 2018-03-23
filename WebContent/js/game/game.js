function leaveTable() {
	var url = "/ICryptoPoker/REST/GetGameServiceWS/EndHand?handId="
			+ $("#handID").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
			if (data.success == true)
				window.location.replace('t_game.do');
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
}

function updateGameInfo(data) {
	$(".tableCards").each(function() {
		$(this).html('<img alt="" src="images/game/card.jpg">');
	});
	$(".sitPlaceContainer").each(function() {
		$(this).html("<div class='sitPlaceThumbnailEmpty'>Waiting</div>");
	});
	$("#handPotContainer")
			.html(
					'<img alt="" src="images/game/stack.png" height="100%"><span>&nbsp;&cent;&nbsp;'
							+ data.pot + '</span>');
	$(".actionButtons").each(function() {
		$(this).button();
		$(this).button('disable');
	});
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

	if (data.status == "WAITING" || data.status == "NOT_STARTED") {
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("W");
				$(this).addClass("waitingChip");
			}
		});
	} else if (data.status == "POST_SB") {
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("Small");
				$(this).addClass("smallBlindChip");
			}
		});
	} else if (data.status == "POST_BB") {
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("Big");
				$(this).addClass("bigBlindChip");
			}
		});
	} else if (data.status == "ACTION_TO_CHECK"
			|| data.status == "ACTION_TO_CALL") {
		if (data.status == "ACTION_TO_CALL") {
			$("#checkBTN").html("Call &cent;" + data.amountToCall);
			$("#checkBTN").attr("onclick", "call()");
		} else {
			$("#checkBTN").html("Check");
			$("#checkBTN").attr("onclick", "check()").trigger("create");
		}
		if (playerId == $("#playerID").val()) {
			$(".actionButtons").each(function() {
				$(this).button('enable');
			});
		}

		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("Dealer");
				$(this).addClass("dealerChip");
			}
		});
		playerToActId = playerId;
		countDownTotal = 15000;
		timeLeft = 0;
		// clearInterval(timer);
		// setInterval(SetTimer, 1000);
	} else if (data.status == "LOST_HAND") {
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("LOST");
				$(this).addClass("waitingChip");
			}
		});
	} else if (data.status == "WON_HAND") {
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("WIN");
				$(this).addClass("waitingChip");
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
	$(".sitPlaceContainer")
			.each(
					function() {
						if (content == "")
							if (id != $("#playerID").val()) {
								// username, chip and timer div START
								content = "<div class='ui-block-solo playerInfo'>"
										+ "<div class='ui-block-solo w3-light-grey w3-round w3-tiny'>"
										+ "<div class='w3-container w3-round w3-green' style='width:100%; height:7px;' id='timer"
										+ id
										+ "'></div></div>"
										+ "<div class='ui-block-solo playerTimer'></div>"
										+ "<div class='ui-grid-a'>"
										+ "<div class='ui-block-a pscontainer' id='pscontainer"
										+ id
										+ "'></div>"
										+ "<div class='ui-block-b'> "
										+ name
										+ "</div></div>"
										+ "<div class='ui-block-solo playerTotalChipsPlace'> &cent;"
										+ chips
										+ "</div>"
										// username, chip and timer div END
										// PLAYER STATUS AND ACTION CONTAINER
										// START
										+ "<div class='ui-block-solo amountToCallcontainer' id='amountToCallcontainer"
										+ id
										+ "'> &cent;"
										+ amountToCall
										+ "</div></div>"

										// PLAYER STATUS AND ACTION CONTAINER
										// END
										// CARD CONTAINER START
										+ "<div class='ui-grid-a playerCardsContainer' id='cards"
										+ id
										+ "'><div class='ui-block-a card1'></div><div class='ui-block-b card2'></div>"
										+ "</div></div>";
								// CARD CONTAINER END

							} else {
								content = "<div class='ui-block-a'><div class='ui-block-solo playerInfo'>"
										+ "<div class='ui-block-solo w3-light-grey w3-round w3-tiny'>"
										+ "<div class='w3-container w3-round w3-green' style='width:100%; height:7px;' id='timer"
										+ id
										+ "'></div></div>"
										+ "<div class='ui-block-solo playerTimer'></div>"
										+ "<div class='ui-grid-a'>"
										+ "<div class='ui-block-a pscontainer' id='pscontainer"
										+ id
										+ "'></div>"
										+ "<div class='ui-block-b'> "
										+ name
										+ "</div></div>"
										+ "<div class='ui-block-solo playerTotalChipsPlace'> &cent;"
										+ chips
										+ "</div>"
										// username, chip and timer div END
										// PLAYER STATUS AND ACTION CONTAINER
										// START
										+ "<div class='ui-block-solo amountToCallcontainer' id='amountToCallcontainer"
										+ id
										+ "'> &cent;"
										+ amountToCall
										+ "</div></div></div>"
										+ "<div class='ui-block-b'><div class='ui-grid-a playerCardsContainer' id='cards"
										+ id
										+ "'><div class='ui-block-a card1'></div><div class='ui-block-b card2'></div>"
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
				sendText("");
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
				sendText("");
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
				sendText("");
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
			+ "&betAmount=" + $("#betAmount").val();
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
