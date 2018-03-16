$(window).on("load", function() {
	// var playerCounter = 0;
	$(".jqm-header").css("display", "none");
	$(window).on("resize", fitElementsWithinScreen());
	fitElementsWithinScreen();
	getGameStatus();
});

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
	$(".timerCircle").width($("#userSitPlace").height() * 3 / 4);
	$(".timerCircle").height($("#userSitPlace").height() * 3 / 4);
	$(".timerText").width($("#userSitPlace").height() * 3 / 4);
	$(".timerText").height($("#userSitPlace").height() * 3 / 4);
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

	}
}
