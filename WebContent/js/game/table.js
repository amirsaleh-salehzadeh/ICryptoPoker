var webSocket;
var wsUri = "";
function onMessage(evt) {
	console.log("received over websockets: " + evt.data);
	updateGameInfo(JSON.parse(evt.data));
}

function onError(evt) {
	console.log('' + evt.data);
}

function onOpen() {
	console.log("Connected to " + wsUri);
}

function sendText(json) {
	console.log("sending text: " + json);
	webSocket.send(json);
}
function fitElementsWithinScreen() {
	$(".sitPlaceThumbnail").each(function() {
		$(this).width($("#userSitPlace").height() / 2);
		$(this).height($("#userSitPlace").height() / 2);
		$(this).trigger("create");
	});
	$(".sitPlaceThumbnailEmpty").each(function() {
		$(this).width($("#userSitPlace").height() / 2);
		$(this).height($("#userSitPlace").height() / 2);
		$(this).trigger("create");
	});
	$(".playerCardsContainer").each(function() {
		$(this).width($("#userSitPlace").height() * 1.4);
		$(this).height($("#userSitPlace").height());
		$(this).trigger("create");
	});
}

function generateACard(cardVal, divID, cardNumber) {
	var text, img;
	if (cardVal.charAt(0) == "T")
		text = "10";
	else
		text = cardVal.charAt(0);
	img = cardVal.charAt(1);
	// img = cardVal.split("_")[2].charAt(1).toLowerCase;
	var color = "black";
	if (img == "c")
		img = "&clubs;";
	else if (img == "s")
		img = "&spades;";
	else if (img == "d") {
		img = "&diams;";
		color = "red";
	} else if (img == "h") {
		img = "&hearts;";
		color = "red";
	}
	// var res = "<div class='card-small'><span class='card-text " + color +
	// "'>"
	// + text + "</span><span class='card-img " + color + "'>" + img
	// + "</span></div>";
	var res = "<img src='images/game/cards/" + cardVal
			+ ".png' height='100%' width='100%'/>";
	if (divID == "flopsContainer") {
		$("#flop" + cardNumber).html(res).trigger("create");
	} else {
		$("#" + divID).find(".card" + cardNumber).html(res).trigger("create");
		if (cardNumber == 2) {
			$("#" + divID).find(".card" + cardNumber).width(
					$("#userSitPlace").height() * 0.7).trigger("create");
			// $("#flopsContainer div").height(
			// $("#flop1").width() + ($("#flop1").width() * 0.7));
		}
	}
}

function resetPlayerInfo() {
	if ($(".playerInfo").length > 0)
		$(".playerInfo").each(function(i) {
			if (this.hasClass("winner")) {
				this.removeClass("winner");
			} else if (this.hasClass("loser")) {
				this.removeClass("loser");
			}
		});
	else
		return;
}

$(document).ready(
		function() {
			fitElementsWithinScreen();
			var wsUri = "ws://" + document.location.host
					+ "/ICryptoPoker/game/" + $("#gameID").val() + "/"
					+ $("#playerID").val();
			webSocket = new WebSocket(wsUri);
			webSocket.onmessage = function(evt) {
				onMessage(evt);
			};
			webSocket.onerror = function(evt) {
				onError(evt);
			};
			webSocket.onopen = function(evt) {
				onOpen(evt);
				$(".actionButtons").each(function() {
					$(this).addClass("ui-state-disabled");
				});
			};
			$("#flopsContainer div").height(
					$("#flop1").width() + ($("#flop1").width() * 0.7));
//			resetplayerInfo();
			$("#sliderRaise").slider();
		});


//Toggles the display of the chat to block or none to be visible when the icon is clicked
function toggleChat(){
	if($("#chatBoxContainer").css("display")=="block"){
		
		$("#chatBoxContainer").css("display","none");
	}else
		$("#chatBoxContainer").css("display","block");
}