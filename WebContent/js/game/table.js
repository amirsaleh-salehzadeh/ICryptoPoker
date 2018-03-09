$(window)
		.on(
				"load",
				function() {
					$(".jqm-header").css("display", "none");
					fitElementsWithinScreen();
					$(window).on("resize",fitElementsWithinScreen());
					$(".sitPlaceContainer")
							.each(
									function() {
										var content = "<div class='sitPlaceThumbnail'>"
												+ "<img alt='' src='images/game/user.png' height='100%' /></div>"
												+ "<div class='playerNamePlace'>SIT</div>"
												+ "<div class='playerTotalChipsPlace'>2,000,000$</div>";
										$(this).html(content);

									});
					$(".sitPlaceThumbnail").each(function() {
						$(this).width($("#userSitPlace").css("height"));
						$(this).height($("#userSitPlace").css("height"));
						$(this).trigger("create");
					});
					// $(".playerNamePlace").each(function() {
					// $(this).trigger("create");
					// });

				});

function fitElementsWithinScreen() {
	var mainBoardHeight = $(window).height() - 88;// Bottom and
	$("#mainTable").css("height", $("mainTableParentDIV").height());
	$("#mainTable").css("width", $("mainTableParentDIV").width());
	$(".sitPlaceThumbnail").each(function() {
		$(this).width($("#userSitPlace").css("height"));
		$(this).height($("#userSitPlace").css("height"));
		$(this).trigger("create");
	});
	// $("#mainBoardContainerDIV").width();
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

	var res = "<div class='card-small'><p class='card-text black'>" + text
			+ "</p>" + "<p class='card-img " + color + "'>" + img + "</p>"
			+ "</div>";
	$("#flop" + flopNo).html(res).trigger("create");
}
