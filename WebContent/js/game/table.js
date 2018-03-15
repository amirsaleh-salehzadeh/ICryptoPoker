$(window)
		.on(
				"load",
				function() {
					// var playerCounter = 0;
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
	var content = "<div class='ui-grid-a'><div class='ui-block-a'><div class='playerNamePlace'> "
			+ playerName
			+ "</div><div class='playerTotalChipsPlace'>$"
			+ chips
			+ "</div></div>"
			+ "<div class='ui-block-b'><div class='ui-grid-a playerCardsContainer' id='cards"
			+ playerName
			+ "'><div class='ui-block-a card1'>a</div><div class='ui-block-b card2'>b</div>"
			+ "</div></div><div class='pscontainer' id='pscontainer"
			+ playerName + "'></div>";
	return content;
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
	$(".playerCardsContainer").each(
			function() {
				$(this).width($("#userSitPlace").width() / 2);
				$(this).height($("#userSitPlace").height());
//				($(this).find(".card2")).find(".card-img").css("left",
//						$(this).find(".card2").css("left"));
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
