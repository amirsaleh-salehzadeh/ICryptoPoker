function startTheGame() {
//	if ($("#isStarted").val() == true)
		return;
	var url = "/ICryptoPoker/REST/GetGameServiceWS/StartHand?gameId="
			+ $("#gameID").val();
	$.ajax({
		url : url,
		cache : false,
		async : true,
		success : function(data) {
//			if(data.success==true)
//				startHand();
			alert(data);

		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("An error occured while removing the marker. "
					+ thrownError);
		}
	});
}