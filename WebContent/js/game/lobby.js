//$(document).ready(getAllGames());
$(window).load(getAllGames());
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
			var tableRows = "";
			$.each(data, function(k, l) {
				var smallBig = l.gameStructure.currentBlindLevel.split("_");
				tableRows += "<tr onclick='joinGame(" + l.id + ","+smallBig[1]  +"," + smallBig[2] +")'><td>"
						+ l.name + "</td><td>";
				
				tableRows += smallBig[1] + " / " + smallBig[2] + "</td><td>";
				tableRows += parseInt(smallBig[2]) * 40 + " / "
				+ parseInt(smallBig[2]) * 200  + "</td><td>"
						+ l.playersRemaining + " / 10</td></tr>";
			});
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
}

function joinGame(gameID, min, max) {
	if ($("#playerName").val() == "") {
		alert("player name????");
		return;
	}else{
	var playerChips = ($('#chips').html().split(";"))[1];
		// player does not have enough chips to enter the game
		if(playerChips< min){
			alert("not enough chips");
			
		}else
		if(playerChips<max){
//			players chips are less than the max
			$('#buyIn').attr("max", playerChips);
			$('#buyIn').attr("min", min);
			$( "#popupJoinGame" ).popup("open");
			$('#joingGameID').attr("value", gameID);
		}else{
//			player has enough chips for min and max
			$('#buyIn').attr("min", min);
			$('#buyIn').attr("max", max);
			$( "#popupJoinGame" ).popup("open");
			$('#joingGameID').attr("value", gameID);

			
		}
		//set the on click for the Join button.
		$('#joinGameBTN').click(function () {
			$( "#popupJoinGame" ).popup("close");
			buyInGame(gameID, $('#buyIn').val());
        });
		
	}

}


function buyInGame(gameID, chips){
//	takes the game ID and chips player bought in with and joins the game
	alert(chips);
	window.location.replace("t_game.do?reqCode=joinAGame&gameId=" + gameID
	+ "&playerName=" + $("#playerName").val()+ "&chips=" + chips);
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
					+ parseInt(smallBig[2]) * 200 +
			"</td><td>" + result.playersRemaining + " / 10</td></tr>";
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