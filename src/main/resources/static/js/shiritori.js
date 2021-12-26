/**
 * 
 */

function createGame(){
	//TODO: unsubscribe their stomp client? 
	//get data from page
	var createGameName = $("#createGameName").val();
	var createGamePassword = $("#createGamePassword").val();
	
	$.ajax({
		url: "/shiritori/createGame",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify({
			gameName: createGameName,
			password: createGamePassword
		}),
		traditional: true,
		success: function(data){
			console.log("Success: " + data);
			$(document.body).html(data);
			console.log("done loading");
		}
			
	});
}

$(document).ready(function(){
	$("#createGameButton").click(function(){
		createGame();
	});
});