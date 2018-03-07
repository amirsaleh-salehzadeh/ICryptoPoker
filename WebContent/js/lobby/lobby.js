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
			var tableRows = "";
			$.each(data, function(k, l) {
				tableRows += "<tr onclick='alert(" + l.id + ")'><td>" + l.name
						+ "</td><td>";
				var smallBig = l.gameStructure.currentBlindLevel.split("_");
				tableRows += smallBig[1] + " / " + smallBig[2] + "</td><td>"
						+ l.gameStructure.startingChips + "</td><td>"
						+ l.players.length + " / 10</td></tr>";
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

function createNewGame() {
	$.ajax({
		url : '/ICryptoPoker/REST/GetGameServiceWS/CreateGame',
		type : "POST", 
		dataType : 'json', 
		data : $("#createNewGameForm").serialize(),
		success : function(result) {
			alert("hi");
		},
		error : function(xhr, resp, text) {
			console.log(xhr, resp, text);
		}
	});
	// alert(JSON.stringify($('#createNewGameForm').serializeObject()));
}