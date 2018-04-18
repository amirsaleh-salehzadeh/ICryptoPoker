var webSocket;
var wsUri = "";

function fitElementsWithinScreen() {
	$(".card")
			.each(
					function() {
						if ($(this).html().length > 0) {
							$(".playerCardsContainer").width(
									$("#userSitPlace").height() * 1.5);
							$(".playerCardsContainer").height(
									$("#userSitPlace").height());
							if (!$(this).hasClass("tableCards")) {
								$(this)
										.width(
												$(this).parent().parent()
														.height() * 0.7);
								$(this).height(
										$(this).parent().parent().height());
							} else {
								$(this)
										.width(
												$("#userSitPlace").height() * 0.7);
								$(this).height($("#userSitPlace").height());
							}
							$(this).trigger("create");
						}
					});
	var screen = $.mobile.getScreenHeight();
	var header = $(".ui-header").hasClass("ui-header-fixed") ? $(".ui-header")
			.outerHeight() - 1 : $(".ui-header").outerHeight();
	var footer = $(".ui-footer").hasClass("ui-footer-fixed") ? $(".ui-footer")
			.outerHeight() - 1 : $(".ui-footer").outerHeight();
	var contentCurrent = $(".ui-content").outerHeight()
			- $(".ui-content").height();
	var content = screen - header - footer - contentCurrent;
	$(".ui-content").height(content);
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
			// fitElementsWithinScreen();
			webSocket = new WebSocket(wsUri);
			webSocket.onmessage = function(evt) {
				onMessage(evt);
			};
			webSocket.onerror = function(evt) {
				onError(evt);
			};
			webSocket.onopen = function(evt) {
				onOpen(evt);
			};

			webSocket.onclose = function(evt) {
				onClose(evt);
			};

			window.onbeforeunload = function() {
				webSocket.onclose = function() {
				}; // disable onclose handler first
				webSocket.close();
			};
			$(window).on("resize", function() {
				fitElementsWithinScreen();
			});
			fitElementsWithinScreen();
		});

$(window).on("load", fitElementsWithinScreen);

function onMessage(evt) {
	console.log("received over websockets: " + evt.data);
	updateGameInfo(JSON.parse(evt.data));
	fitElementsWithinScreen();
}

function onClose(evt) {
	console.log("closing websockets: " + evt.data);
	webSocket.close();
	fitElementsWithinScreen();
}

function onError(evt) {
	console.log('error: ' + evt.data);
}

function onOpen() {
	console.log("Connected to " + wsUri);
}

function sendText(json) {
	console.log("sending text: " + json);
	webSocket.send(json);
}
// Toggles the display of the chat to block or none to be visible when the icon
// is clicked
function toggleChat() {
	if ($("#chatBoxContainer").css("display") == "block") {
		$("#chatBoxContainer").css("display", "none");
	} else
		$("#chatBoxContainer").css("display", "block");
}
function toggleFullScreen(elem) {
	if ((document.fullScreenElement !== undefined && document.fullScreenElement === null)
			|| (document.msFullscreenElement !== undefined && document.msFullscreenElement === null)
			|| (document.mozFullScreen !== undefined && !document.mozFullScreen)
			|| (document.webkitIsFullScreen !== undefined && !document.webkitIsFullScreen)) {
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

function sitInPopupOpen() {
	$("#popupSitIn").popup("open");
}