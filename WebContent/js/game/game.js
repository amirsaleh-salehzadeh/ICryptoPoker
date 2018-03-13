function startTheGame() {
	if ($("#isStarted").val() == true)
		return;
	var url = "/ICryptoPoker/REST/GetGameServiceWS/StartHand?gameId="
			+ $("#gameID").val();
	$
			.ajax({
				url : url,
				cache : false,
				async : true,
				success : function(data) {
					// if(data.success==true)
					// startHand();

					// for now the data object which contains the players, and
					// the cards in hteir hands and etc....
					// we should present each player's card in the specific a
					// and b blocks next to them
					// make a loop on all divs with class .playerCardsContainer
					// in the loop if $(this).attr(id) == "cards"+playerUsername
					// then show the cards in
					// $(this).children div >>> u can use this function to
					// generate the card generateACard but
					// u ok with it?
					$(data.players).each(function(k, l) {
						$('.playerCardsContainer').each(function(i, obj) {
							if (this.id != null && "cards"+l.player.id == this.id) {
								generateACard(l.card1S, this.id, 1);
								generateACard(l.card2S, this.id, 2);
							}
						});
					});
					console.log("hi");
					// $(data.players).each(function(k, l) {
					//
					// });
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("An error occured while removing the marker. "
							+ thrownError);
				}
			});
}