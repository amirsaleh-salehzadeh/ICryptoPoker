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
											pname = $("#playerNameDiv").html();

										var content = "<div class='sitPlaceThumbnail'>"
												+ "<img alt='' src='images/game/user.png' height='100%' /></div>"
												+ "<div class='playerNamePlace'>";
										content += pname
												+ "</div><div class='playerTotalChipsPlace'></div>";
										if (pname.length > 0)
											$(this).html(content);

									});
					fitElementsWithinScreen();
					generateACard("A", "clubs", 1);
					generateACard("2", "spades", 2);
					generateACard("5", "hearts", 3);
				});

function fitElementsWithinScreen() {
	var mainBoardHeight = $(window).height() - 88;// Bottom and
	$("#mainTable").css("height", $("mainTableParentDIV").height());
	$("#mainTable").css("width", $("mainTableParentDIV").width());
	$(".sitPlaceThumbnail").each(function() {
		$(this).width($("#userSitPlace").height() / 2);
		$(this).height($("#userSitPlace").height() / 2);
		$(this).trigger("create");
	});
	// $("#mainBoardContainerDIV").width();
}

function getOtherPlayersName() {
	if ($("#playerIDs").val().length == 0)
		return "";
	var name = $("#playerIDs").val().split(",");
	if (name.length > 1) {
		var res = name[0];
		$("#playerIDs").val($("#playerIDs").val().replace(res + ",", ""));
		return res;
	} else {
		$("#playerIDs").val("");
		return name[0];
	}
}

function generateACard(text, img, flopNo) {
	var color = "black";
	if (img == "clubs")
		img = "&clubs;";
	else if (img == "spades")
		img = "&spades;";
	else if (img == "diam") {
		img = "&diams;";
		color = "red"
	} else if (img == "hearts") {
		img = "&hearts;";
		color = "red";
	}

	var res = "<div class='card-small'><span class='card-text black'>" + text
			+ "</span><span class='card-img " + color + "'>" + img
			+ "</span></div>";
	$("#flop" + flopNo).html(res).trigger("create");
}
