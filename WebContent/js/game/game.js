function startTheGame() {
	if ($("#isStarted").val() == true)
		return;
	var url = "/ICryptoPoker/REST/GetGameServiceWS/StartHand?gameId="
			+ $("#gameID").val();
	$
			.ajax({
				url : url,
				cache : false,
				async : true,
				success : function(data) {
					$("#handPotContainer").html(
							'<img alt="" src="images/game/stack.png" height="100%"><span>&nbsp;'
									+ data.pot + '&nbsp;$</span>');
					$(data.players).each(function(k, l) {
						dealCards2Players(l);
					});
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("An error occured while removing the marker. "
							+ thrownError);
				}
			});
}

function dealCards2Players(l) {
	$('.playerCardsContainer').each(function(i, obj) {
		if (this.id != null && "cards" + l.player.id == this.id) {
			generateACard(l.card1S, this.id, 1);
			generateACard(l.card2S, this.id, 2);
		}
	});
}

function check() {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/check?gameId="
			+ $("#gameID").val() + "&playerId=" + $("#playerID").val();
	$
			.ajax({
				url : url,
				cache : false,
				async : true,
				success : function(data) {
					if (data.success == true)
						goToNexPlayer();
					else
						alert("check failed");
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("An error occured while removing the marker. "
							+ thrownError);
				}
			});
}

function fold() {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/fold?gameId="
			+ $("#gameID").val() + "&playerId=" + $("#playerID").val();
	$
			.ajax({
				url : url,
				cache : false,
				async : true,
				success : function(data) {
					if (data.success == true)
						goToNexPlayer();
					else
						alert("fold failed");
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("An error occured while removing the marker. "
							+ thrownError);
				}
			});
}

function raise() {
	var url = "/ICryptoPoker/REST/GetPlayerServiceWS/bet?gameId="
			+ $("#gameID").val() + "&playerId=" + $("#playerID").val();
	$
			.ajax({
				url : url,
				cache : false,
				async : true,
				success : function(data) {
					if (data.success == true)
						goToNexPlayer();
					else
						alert("fold failed");
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("An error occured while removing the marker. "
							+ thrownError);
				}
			});
}

function allIn() {

}

function leaveTable() {
	window.location.replace('t_game.do');
}

function goToNexPlayer() {
	alert("goToNexPlayer");
}