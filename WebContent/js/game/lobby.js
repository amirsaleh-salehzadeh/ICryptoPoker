$(document).ready(getAllGames());

$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

function getAllGames() {
	var url = "/ICryptoPoker/REST/GetGameServiceWS/GetAllGames";
	$.ajax({
		url : url,
		cache : false,
		async : true,
		beforeSend : function() {
			ShowLoadingScreen();
		},
		success : function(data) {
			alert(data);
		},
		complete : function() {
			HideLoadingScreen();
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("An error occured while removing the marker. "
					+ thrownError);
		}
	});
}

function joinGame(gameID) {
	if ($("#playerName").val() == "") {
		alert("player name????");
		return;
	}
	window.location.replace("t_game.do?reqCode=joinAGame&gameId=" + gameID
			+ "&playerName=" + $("#playerName").val());
}

function createNewGame() {
	$.ajax({
		url : '/ICryptoPoker/REST/GetGameServiceWS/CreateGame',
		type : "POST",
		dataType : 'json',
		data : $("#createNewGameForm").serialize(),
		// data : JSON.stringify($('#createNewGameForm').serializeObject()),
		contentType : "application/json",
		success : function(result) {
			var tableRows = "<tr onclick='alert(" + result.id + ")'><td>"
					+ result.name + "</td><td>";
			var smallBig = result.gameStructure.currentBlindLevel.split("_");
			tableRows += smallBig[1] + " / " + smallBig[2] + "</td><td>";
			tableRows += parseInt(smallBig[2]) * 40 + "/"
					+ parseInt(smallBig[2]) * 200 + "</td><td>"
					+ result.players.length + " / 10</td></tr>";
			$("#lobbyTableTBody")
					.html(tableRows + $("#lobbyTableTBody").html());
			$("#table-lobby").trigger("create");
		},
		error : function(xhr, resp, text) {
			console.log(xhr, resp, text);
		}
	});
	// alert(JSON.stringify($('#createNewGameForm').serializeObject()));
}