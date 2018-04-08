var percentColors = [ {
	pct : 0.0,
	color : {
		r : 0xff,
		g : 0x00,
		b : 0
	}
}, {
	pct : 0.5,
	color : {
		r : 0xff,
		g : 0xff,
		b : 0
	}
}, {
	pct : 1.0,
	color : {
		r : 0x00,
		g : 0xff,
		b : 0
	}
} ];

var countDownTotal = 15000;
var timeLeft = 0;
var playerToActId;
var timer;

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
	$('.playerInfo').each(function() {
		if ($(this).hasClass("sitOut")) {
			$(this).removeClass("sitOut");
		}
	});
	if (data.gameStatus == "NOT_STARTED") {
		$("#handPotContainer").html('Game is not started');
		$("#sliderRaise").slider();
	} else
		$("#handPotContainer").html(
				'<img alt="" src="images/game/stack.png" height="100%"><span>&nbsp;$&nbsp;'
						+ data.pot + '&nbsp;</span>');
	if (data.gameStatus == "END_HAND") {
		return endHand(data);
	}
	$(".tableCards").each(function() {
		$(this).html('<img alt="" src="images/game/card.jpg">');
	});
	$(".actionButtons").each(function() {
		$(this).addClass("ui-state-disabled");
	});
	$("#raiseSliderContainer").addClass("ui-state-disabled");
	$("#handID").val(data.handId);
	if (data.cards != null)
		$(data.cards).each(function(k, l) {
			generateACard(l, "flopsContainer", k + 1);
		});

	$(data.players).each(function(k, l) {
		updatePlayerInfo(l);
	});
	if ($("#pscontainer" + data.POST_SB).length <= 0)
		$("#pscontainer" + data.POST_SB).html(
				'<img src="images/game/sb.png" height="100%" />');
	if ($("#pscontainer" + data.POST_BB).length <= 0)
		$("#pscontainer" + data.POST_BB).html(
				'<img src="images/game/bb.png" height="100%" />');
	if ($("#pscontainer" + data.DEALER).length <= 0) {
		// if the div already contain info
		if ($("#pscontainer" + data.DEALER).html().length > 1)
			$("#pscontainer" + data.DEALER).html(
					$("#pscontainer" + data.DEALER).html()
							+ '<img src="images/game/d.png" height="100%" />');
		// else
		else
			$("#pscontainer" + data.DEALER).html(
					'<img src="images/game/d.png" height="100%" />');
	}
	fitElementsWithinScreen();
}

function setTimer() {
	if (timeLeft == 0)
		timeLeft = countDownTotal - 1000;
	else
		timeLeft = timeLeft - 1000;
	var percentage = Math.round(timeLeft / countDownTotal * 100);
	$("#timer" + playerToActId).css("width", percentage + "%");
	$("#timer" + playerToActId).css("background-color",
			getColorForPercentage(percentage / 100));
	if (timeLeft == 0) {
		clearInterval(timer);
		if (!$("#checkBTN").hasClass("ui-state-disabled")) {
			if ($("#checkBTN").html() == "Check")
				check();
			else
				fold();
		}
	}
};

function getColorForPercentage(pct) {
	for ( var i = 1; i < percentColors.length - 1; i++) {
		if (pct < percentColors[i].pct) {
			break;
		}
	}
	var lower = percentColors[i - 1];
	var upper = percentColors[i];
	var range = upper.pct - lower.pct;
	var rangePct = (pct - lower.pct) / range;
	var pctLower = 1 - rangePct;
	var pctUpper = rangePct;
	var color = {
		r : Math.floor(lower.color.r * pctLower + upper.color.r * pctUpper),
		g : Math.floor(lower.color.g * pctLower + upper.color.g * pctUpper),
		b : Math.floor(lower.color.b * pctLower + upper.color.b * pctUpper)
	};
	return 'rgb(' + [ color.r, color.g, color.b ].join(',') + ')';
	// or output as hex if preferred
}

function updatePlayerInfo(data) {
	var playerId = data.id;
	addANewPlayerToTable(data);
	if (data.status != "NOT_STARTED" && data.status != "SEATING")
		dealCards2Players(data, playerId);
	if (data.status == "ACTION_TO_CHECK" || data.status == "ACTION_TO_CALL") {
		var atc = parseInt(data.amountToCall);
		if (data.status == "ACTION_TO_CALL") {
			$("#checkBTN").text("Call $" + atc);
			$("#checkBTN").attr("onclick", "call()").trigger("create");
		} else {
			$("#checkBTN").text("Check");
			$("#checkBTN").attr("onclick", "check()").trigger("create");
		}
		if (playerId == $("#playerID").val()) {
			$(".actionButtons").each(function() {
				$(this).removeClass("ui-state-disabled");
			});
			$("#raiseSliderContainer").removeClass("ui-state-disabled");
			if (atc > 0) {
				$("#sliderRaise").attr("min", atc * 2);
			} else {
				$("#sliderRaise").attr("min", parseInt(data.bigBlind));
			}
			$("#sliderRaise").attr("max", parseInt(data.chips)).slider(
					"refresh");
		}
		$("#sliderRaise").attr("value", $("#sliderRaise").attr("min")).slider(
				"refresh");
		playerToActId = playerId;
		countDownTotal = 15000;
		timeLeft = 0;
		clearInterval(timer);
		// timer = setInterval(setTimer, 1000);
	} else if (data.status == "LOST_HAND") {
		$('.playerInfo').each(function() {
			if ("playerInfo" + playerId == this.id) {
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
	} else if (data.status == "SIT_OUT" || data.status == "SIT_OUT_GAME"
			|| data.status == "SEATING") {
		$('.playerInfo').each(function() {
			if ("playerInfo" + playerId == this.id) {
				$(this).addClass("sitOut");
			}
		});
	}

}
function endHand() {
	console.log("GAME DONE");
	clearInterval(timer);
	var res = "<div class='ui-grid-a playerCardsContainer' id='cards"
	 + id
	 + "'><div class='ui-block-a card1 card'></div>" +
	 		"<div class='ui-block-b card2 card'></div></div><div class='handRankingRes'></div>";
	$(".sitPlaceContainer")
	.each(
			function() {
					if (id != $("#playerID").val()) {}
			});
	// sitIn();
	// sendText("");
	last hand div and presentation here...
	
}

function addANewPlayerToTable(data) {
	var id = data.id, name = data.name, chips = data.chips, amountBetRound = data.amountBetRound;
	var content = "";
	var betDivShow = false;
	if (amountBetRound != "0") {
		amountBetRound = "Raised $" + amountBetRound;
		betDivShow = true;
	} else
		amountBetRound = "";
	$(".sitPlaceContainer")
			.each(
					function() {
						if (content == "")
							if (id != $("#playerID").val()) {
								content = "<div class='ui-block-solo playerInfo' id='playerInfo"
										+ id
										+ "'>"
										+ "<div class='ui-block-solo w3-light-grey w3-round w3-tiny'>"
										+ "<div class='w3-container w3-round' style='width:100%; height:5px;' id='timer"
										+ id
										+ "'></div></div>"
										+ "<div class='ui-block-solo otherPlayersName'>"
										+ name
										+ "</div><div class='ui-block-solo ui-grid-a'>"
										+ "<div class='ui-block-a pscontainer' id='pscontainer"
										+ id
										+ "'></div>"
										+ "<div class='ui-block-b playerTotalChipsPlace'> $"
										+ chips
										+ "</div></div><div class='amountToCallcontainer' id='amountToCallcontainer"
										+ id
										+ "'>"
										+ amountBetRound
										+ "</div></div>";
							} else if (data.status != "SIT_OUT_GAME"
									&& data.status != "SIT_OUT") {
								if ($("#userSitPlace").find(
										".amountToCallcontainer").length <= 0) {
									content = "<div class='ui-grid-a'><div class='ui-block-a'><div class='ui-block-solo playerInfo' id='playerInfo"
											+ id
											+ "'>"
											+ "<div class='ui-block-solo w3-light-grey w3-round w3-tiny'>"
											+ "<div class='w3-container w3-round' style='width:100%; height:11px;' id='timer"
											+ id
											+ "'></div></div>"
											+ "<div class='ui-grid-a ui-block-solo'>"
											+ "<div class='ui-block-a pscontainer' id='pscontainer"
											+ id
											+ "'></div>"
											+ "<div class='ui-block-b playerTotalChipsPlace'> $"
											+ chips
											+ "</div></div>"
											+
											// + "<div class='ui-block-solo
											// amountToCallcontainer'
											// id='amountToCallcontainer"
											// + id
											// + "'>"
											// + amountBetRound
											// + "</div>" +
											"</div></div>"
											+ "<div class='ui-block-b ui-grid-a playerCardsContainer' id='cards"
											+ id
											+ "'><div class='ui-block-a card1 card'></div><div class='ui-block-b card2 card'></div>"
											+ "</div></div>";
									$("#userSitPlace").html(content);
								} else {
									$("#userSitPlace").find(
											".playerTotalChipsPlace").html(
											"$; " + chips);
								}
								return false;
							}
						// set the content for the first time
						if ($(this).children("div").hasClass(
								"sitPlaceThumbnailEmpty")
								&& $("#sitPlaceContainer" + id).length <= 0) {

							$(this).html(content);
							$(this).attr("id", "sitPlaceContainer" + id);
							return false;
						} else if ($("#sitPlaceContainer" + id).length > 0) {
							// set the content in the next rounds
							if (betDivShow) {
								$("#sitPlaceContainer" + id).find(
										".amountToCallcontainer").css(
										"display", "block");
								$("#sitPlaceContainer" + id).find(
										".amountToCallcontainer").html(
										amountBetRound).trigger("create");
							} else
								$("#sitPlaceContainer" + id).find(
										".amountToCallcontainer").css(
										"display", "none");
							$("#sitPlaceContainer" + id).find(
									".playerTotalChipsPlace")
									.html("$ " + chips);
							// $("#sitPlaceContainer" + id).html(content);
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
	clearInterval(timer);
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
	clearInterval(timer);
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
				sendText("Folded");
			else
				alert("fold failed");
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
	clearInterval(timer);
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
				sendText("");
			else
				alert("raise failed");
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
	clearInterval(timer);
}

function sitInTheGame() {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/sitIn?gameId="
			+ $("#gameID").val() + "&playerId=" + $("#playerID").val()
			+ "&chips=" + $("#sitInChips").val() + "&nickname="
			+ $("#nickname").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
			if (data.success == true)
				$("#popupSitIn").popup("close");
			else
				alert("raise failed");
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
	clearInterval(timer);
}

function allIn() {
	$("#sliderRaise").val($("#sliderRaise").attr("max")).slider("refresh");
	raise();
}

function removePlayer(name) {
	$("#sitPlaceContainer" + name).html(
			'<div class="sitPlaceThumbnailEmpty">SEAT</div>');
	$("#sitPlaceContainer" + name).attr('id', null);
}