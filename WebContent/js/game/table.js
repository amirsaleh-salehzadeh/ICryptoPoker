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
	// $(".playerCardsContainer").each(function() {
	// if ($(this).find(".card1").html().length > 0) {
	// $(this).width($("#userSitPlace").height() * 1.4);
	// $(this).height($("#userSitPlace").height());
	// $(this).trigger("create");
	// }
	// });
	$(".card").each(
			function() {
				if ($(this).html().length > 0) {
					$(".playerCardsContainer").width(
							$("#userSitPlace").height() * 1.5);
					$(".playerCardsContainer").height(
							$("#userSitPlace").height());
					$(this).width($("#userSitPlace").height() * 0.7);
					$(this).height($("#userSitPlace").height());
					$(this).trigger("create");
				}
			});
}

function generateACard(cardVal, divID, cardNumber) {
	var res = "<img src='images/game/cards/" + cardVal
			+ ".png' height='100%' />";
	if (divID == "flopsContainer") {
		$("#flop" + cardNumber).html(res).trigger("create");
	} else {
		$("#" + divID).find(".card" + cardNumber).html(res).trigger("create");
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
			$(window).on("resize", function() {
				fitElementsWithinScreen();
			});
			$("#flopsContainer div").height(
					$("#flop1").width() + ($("#flop1").width() * 0.7));
			// resetplayerInfo();
			$("#sliderRaise").slider();
		});

// Toggles the display of the chat to block or none to be visible when the icon
// is clicked
function toggleChat() {
	if ($("#chatBoxContainer").css("display") == "block") {

		$("#chatBoxContainer").css("display", "none");
	} else
		$("#chatBoxContainer").css("display", "block");
}
function toggleFullScreen(elem) {
    // ## The below if statement seems to work better ## if ((document.fullScreenElement && document.fullScreenElement !== null) || (document.msfullscreenElement && document.msfullscreenElement !== null) || (!document.mozFullScreen && !document.webkitIsFullScreen)) {
    if ((document.fullScreenElement !== undefined && document.fullScreenElement === null) || (document.msFullscreenElement !== undefined && document.msFullscreenElement === null) || (document.mozFullScreen !== undefined && !document.mozFullScreen) || (document.webkitIsFullScreen !== undefined && !document.webkitIsFullScreen)) {
        if (elem.requestFullScreen) {
            elem.requestFullScreen();
        } else if (elem.mozRequestFullScreen) {
            elem.mozRequestFullScreen();
        } else if (elem.webkitRequestFullScreen) {
            elem.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
        } else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        }
    } else {
        if (document.cancelFullScreen) {
            document.cancelFullScreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        }
    }
}
function sitIn(){
	$("#popupSitIn").popup("open");
	
	
}