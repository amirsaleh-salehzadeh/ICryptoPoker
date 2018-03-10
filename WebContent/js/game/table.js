$(window)
		.on(
				"load",
				function() {
					$(".jqm-header").css("display", "none");
					$(window).on("resize", fitElementsWithinScreen());
					$(".sitPlaceContainer")
							.each(
									function() {
										var pname = "";
										if ($(this).attr("id") != "userSitPlace")
											pname = getOtherPlayersName();
										else
											pname = $("#playerNameDiv").html()
													+ ","
													+ $("#playerPot").val();

										var content = addAPlyerToTable(pname
												.split(",")[0], pname
												.split(",")[1]);
										if (pname.length <= 0)
											content = "<div class='sitPlaceThumbnailEmpty'>SIT</div>";
										$(this).html(content);
									});
					fitElementsWithinScreen();
					startTheGame();
				});
function addAPlyerToTable(playerName, chips) {
	var content = "<div class='sitPlaceThumbnail'>"
			+ "<img alt='' src='images/game/user.png' height='100%' /></div>"
			+ "<div class='playerNamePlace'> "
			+ playerName
			+ "</div><div class='playerTotalChipsPlace'>$"
			+ chips
			+ "</div>"
			+ "<div class='ui-grid-a playerCardsContainer' id='cards"
			+ playerName
			+ "'><div class='ui-block-a card1'>a</div><div class='ui-block-b card2'>b</div>"
			+ "</div>";
	return content;
}

function fitElementsWithinScreen() {
	var mainBoardHeight = $(window).height() - 88;// Bottom and
	$("#mainTable").css("height", $("mainTableParentDIV").height());
	$("#mainTable").css("width", $("mainTableParentDIV").width());
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
		$(this).width($(this).parent().parent().width() / 2);
		$(this).height($(this).parent().parent().height() / 2);
		$(this).trigger("create");
	});
	// $("#mainBoardContainerDIV").width();
}

function getOtherPlayersName() {
	if ($("#playerIDs").val().length == 0)
		return "";
	var name = $("#playerIDs").val().split(";");
	if (name.length > 1) {
		var res = name[0];
		$("#playerIDs").val($("#playerIDs").val().replace(res + ";", ""));
		return res;
	} else {
		$("#playerIDs").val("");
		return name[0];
	}
}

function generateACard(cardVal, flopNo) {
	var text, img;
	if (cardVal.charAt(0) == "T")
		text = "10";
	else
		text = cardVal.charAt(0);
	img = cardVal.charAt(1);
	var color = "black";
	if (img == "c")
		img = "&clubs;";
	else if (img == "spades")
		img = "&s;";
	else if (img == "diam") {
		img = "&d;";
		color = "red";
	} else if (img == "h") {
		img = "&hearts;";
		color = "red";
	}

	var res = "<div class='card-small'><span class='card-text black'>" + text
			+ "</span><span class='card-img " + color + "'>" + img
			+ "</span></div>";
	$("#flop" + flopNo).html(res).trigger("create");
}
