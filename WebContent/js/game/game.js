function startTheGame() {
	if ($("#isStarted").val() == true)
		return;
	var url = "/ICryptoPoker/REST/GetGameServiceWS/StartGame?gameId="
			+ $("#gameID").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
			if (data.started) {
				$("#isStarted").val(data.isStarted);
				// $("#playerID").val(data.playerInBTN.id);
				startAHand();
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
}

function startAHand() {
	var url = "/ICryptoPoker/REST/GetGameServiceWS/StartHand?gameId="
			+ $("#gameID").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
			$("#handPotContainer").html(
					'<img alt="" src="images/game/stack.png" height="100%"><span>&nbsp;$&nbsp;'
							+ data.pot + '</span>');
			$(data.players).each(function(k, l) {
				dealCards2Players(l, l.id);
			});
			// getGameStatus();
			sendText("");
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
}

function dealCards2Players(l, id) {
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

function getPlayerStatus(playerId, playerName) {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/GetPlayerStatus?gameId="
			+ $("#gameID").val() + "&playerId=" + playerId;
	$
			.ajax({
				url : url,
				cache : false,
				async : true,
				success : function(data) {
					if ((data.status == "ACTION_TO_CALL" || data.status == "ACTION_TO_CHECK")
							&& playerId == $("#playerID").val())
						$(".actionButtons").each(function() {
							$(this).button();
							$(this).button('enable');
						});
					addAPlyerToTable(playerId, playerName, parseInt(data.chips)
							- parseInt(data.amountBetRound), data.amountToCall);
					if (data.status != "NOT_STARTED"
							&& data.status != "SEATING")
						dealCards2Players(data, playerId);

					if (data.status == "WAITING"
							|| data.status == "NOT_STARTED") {
						$('.pscontainer').each(function() {
							if ("pscontainer" + playerId == this.id) {
								$(this).html("W");
								$(this).addClass("waitingChip");
							}
						});
					} else if (data.status == "POST_SB") {
						$('.pscontainer').each(function() {
							if ("pscontainer" + playerId == this.id) {
								$(this).html("S");
								$(this).addClass("smallBlindChip");
							}
						});
					} else if (data.status == "POST_BB") {
						$('.pscontainer').each(function() {
							if ("pscontainer" + playerId == this.id) {
								$(this).html("B");
								$(this).addClass("bigBlindChip");
							}
						});
					} else if (data.status == "ACTION_TO_CALL") {
						// $("#playerID").val(playerId);
						$("#checkBTN").html("Call");
						$("#checkBTN").attr("onclick", "call()");
						$(".actionButtons").each(function() {
							$(this).button('enable');
						});
						$('.pscontainer').each(function() {
							if ("pscontainer" + playerId == this.id) {
								$(this).html("D");
								$(this).addClass("dealerChip");
							}
						});
					} else if (data.status == "ACTION_TO_CHECK") {
						// $("#playerID").val(playerId);
						$("#checkBTN").html("Check");
						$("#checkBTN").attr("onclick", "check()").trigger(
								"create");
						$('.pscontainer').each(function() {
							if ("pscontainer" + playerId == this.id) {
								$(this).html("D");
								$(this).addClass("dealerChip");
							}
						});
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert(xhr.responseText);
				}
			});
}

function getGameStatus() {
	var url = "/ICryptoPoker/REST/GetGameServiceWS/GetGameStatus?gameId="
			+ $("#gameID").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
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
				if (data.players.length >= 2) {
					startTheGame();
					return true;
				}
			} else
				$("#handPotContainer").html(
						'<img alt="" src="images/game/stack.png" height="100%"><span>&nbsp;'
								+ data.pot + '&nbsp;$</span>');
			if (data.cards != null)
				$(data.cards).each(function(k, l) {
					generateACard(l, "flopContainer", k + 1);
				});
			$(data.players).each(function(k, l) {
				updatePlayerStatus(l.id, l.name);
			});
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.responseText);
		}
	});
}

function updateGameInfo(data) {
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
		if (data.players.length >= 2) {
			startTheGame();
			return true;
		}
	} else
		$("#handPotContainer").html(
				'<img alt="" src="images/game/stack.png" height="100%"><span>&nbsp;'
						+ data.pot + '&nbsp;$</span>');
	if (data.cards != null)
		$(data.cards).each(function(k, l) {
			generateACard(l, "flopContainer", k + 1);
		});

	$(data.players).each(function(k, l) {
		updatePlayerInfo(l.id, l.name);
	});
}

function updatePlayerInfo(playerId, playerName, data) {
	var playerId = data.id;
	var playerName = data.name;
	if ((data.status == "ACTION_TO_CALL" || data.status == "ACTION_TO_CHECK")
			&& playerId == $("#playerID").val())
		$(".actionButtons").each(function() {
			$(this).button();
			$(this).button('enable');
		});
	addAPlyerToTable(playerId, playerName, parseInt(data.chips)
			- parseInt(data.amountBetRound), data.amountToCall);
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
				$(this).html("S");
				$(this).addClass("smallBlindChip");
			}
		});
	} else if (data.status == "POST_BB") {
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("B");
				$(this).addClass("bigBlindChip");
			}
		});
	} else if (data.status == "ACTION_TO_CALL") {
		// $("#playerID").val(playerId);
		$("#checkBTN").html("Call");
		$("#checkBTN").attr("onclick", "call()");
		$(".actionButtons").each(function() {
			$(this).button('enable');
		});
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("D");
				$(this).addClass("dealerChip");
			}
		});
	} else if (data.status == "ACTION_TO_CHECK") {
		// $("#playerID").val(playerId);
		$("#checkBTN").html("Check");
		$("#checkBTN").attr("onclick", "check()").trigger("create");
		$('.pscontainer').each(function() {
			if ("pscontainer" + playerId == this.id) {
				$(this).html("D");
				$(this).addClass("dealerChip");
			}
		});
	}
}

function addAPlyerToTable(id, name, chips, amountToCall) {
	$(".sitPlaceContainer")
			.each(
					function() {
						var content = "";
						if (id != $("#playerID").val()) {
							// username, chip and timer div START
							content = "<div class='ui-block-solo playerInfo'>"
									+ "<div class='ui-block-solo w3-light-grey w3-round w3-tiny'>"
									+ "<div class='w3-container w3-round w3-green' style='width:50%; height:7px;'></div></div>"
									+"<div class='ui-block-solo playerInfoContent playerTimer'></div>"
									+ "<div class='ui-grid-a playerInfoContent'>" 
									+"<div class='ui-block-a playerInfoContent pscontainer' id='pscontainer"
									+ id
									+ "'></div>"
									+ "<div class='ui-block-b playerInfoContent playerNamePlace'> "
									+ name
									+ "</div></div>"
									+"<div class='ui-block-solo playerInfoContent playerTotalChipsPlace'>$"
									+ chips
									+ "</div>"
									//username, chip and timer div END
									//PLAYER STATUS AND ACTION CONTAINER START
									+ "<div class='ui-block-solo playerInfoContent amountToCallcontainer' id='amountToCallcontainer"
									+ id + "'> $" + amountToCall
									+ "</div></div>"
									
							//PLAYER STATUS AND ACTION CONTAINER END
									//CARD CONTAINER START
									+ "<div class='ui-grid-a playerCardsContainer' id='cards"
									+ id
									+ "'><div class='ui-block-a card1'></div><div class='ui-block-b card2'></div>"
									+ "</div></div>";
									//CARD CONTAINER END
							
						} else {
							content = "<div class='ui-block-a'><div class='playerTotalChipsPlace'>$"
									+ chips
									+ "</div></div>"
									+ "<div class='ui-block-b'><div class='ui-grid-a playerCardsContainer' id='cards"
									+ id
									+ "'><div class='ui-block-a card1'></div><div class='ui-block-b card2'></div>"
									+ "</div></div><div class='ui-block-c' style='position: relative'><div class='pscontainer' id='pscontainer"
									+ id
									+ "'></div><div class='amountToCallcontainer' id='amountToCallcontainer"
									+ id
									+ "'> $"
									+ amountToCall
									+ "</div></div>";
							$("#userSitPlace").html(content);
							return false;
						}
						if ($(this).children("div").hasClass(
								"sitPlaceThumbnailEmpty")) {
							$(this).html(content);
							$(this).addClass("ui-grid-b");
							return false;
						}
					});
}
