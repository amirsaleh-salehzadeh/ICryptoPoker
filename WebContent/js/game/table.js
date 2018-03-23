var webSocket;
var wsUri = "";
$(document).ready(
		function() {
//			$(".jqm-header").css("display", "none");
//			$(window).on("resize", fitElementsWithinScreen());
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
			// $("#flopsContainer div").width($("#flop1").width());
			$("#flopsContainer div").height(
					$("#flop1").width() + ($("#flop1").width() * 0.7));
		});

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
		$(this).width($("#userSitPlace").width() * 7 / 10);
		$(this).height($("#userSitPlace").height());
		$(this).trigger("create");
	});
	// $(".tableCards").height($("#userSitPlace").height());
	// $(".tableCards").width($("#userSitPlace").height() * 7 / 10);
	// $("#mainBoardContainerDIV").width();
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
	var res = "<div class='card-small'><span class='card-text " + color + "'>"
			+ text + "</span><span class='card-img " + color + "'>" + img
			+ "</span></div>";
	if (divID == "flopsContainer") {
		$("#flop" + cardNumber).html(res).trigger("create");
	} else {
		$("#" + divID).find(".card" + cardNumber).html(res).trigger("create");
		if (cardNumber == 2) {
			$("#" + divID).find(".card" + cardNumber).width(
					$("#userSitPlace").height() * 0.7)
					.trigger("create");
//			$("#flopsContainer div").height(
//					$("#flop1").width() + ($("#flop1").width() * 0.7));
		}
	}
}
