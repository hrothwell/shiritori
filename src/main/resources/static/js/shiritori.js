/**
 * 
 */
$(document).ready(function(){
	$("#createGameButton").click(function(){
		createGame();
	});
	$("#joinGameButton").click(function(){
		joinGame();
	})
});

function createGame(){
	
	var createGameName = $("#createOrJoinGameName").val();
	var createGamePassword = $("#createOrJoinGamePassword").val().trim();
	
	//TODO move this call somewhere else and make it more reusable? we use it twice in this file and it is essentially the same. 
	//Could at least make the success method shared.
	//TODO make an "error" method that lets them know there was some issue
	$.ajax({
		url: "/shiritori/createGame",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify({
			gameName: createGameName,
			password: createGamePassword,
			userName: $("#name").val().trim()
		}),
		traditional: true,
		success: function(data){
			console.log("Success: " + data);
			$(document.body).html(data);
			console.log("done loading");
		},
		error: function(jqXHR){
			alert("Error creating game: " + jqXHR.responseText);
		}
			
	}).done(function(data){
		console.log("In 'done' block now");
		setupShiritoriStompClient();
	});
}

function joinGame(){
	var joinGameName = $("#createOrJoinGameName").val();
	var joinGamePassword = $("#createOrJoinGamePassword").val();
	var userName = $("#name").val();
	var baseUrl = window.location.origin; //todo test what this returns
	var joinUrl = new URL(`/shiritori/joinGame/${joinGameName}`, baseUrl);
	
	if(joinGamePassword.trim() !== ""){
		joinUrl.searchParams.append("password", joinGamePassword.trim());
	}
	if(userName.trim() !== ""){
		joinUrl.searchParams.append("userName", userName.trim());
	}
	
	$.ajax({
		url: joinUrl.toString(),
		method: "GET",
		success: function(data){
			$(document.body).html(data);
			console.log("done loading");
		},
		error: function(jqXHR){
			alert("Error joining game: " + jqXHR.responseText);
		}
			
	}).done(function(data){
		console.log("In 'done' block now");
		setupShiritoriStompClient();
	});
}

//Must be called AFTER game is loaded
function setupShiritoriStompClient(){
	//"subscriptions" and "stompClient" are setup in webSocketClient.js
	if(subscriptions){
		for(var [key, value] of subscriptions){
			value.unsubscribe(); //remove the room so they stop getting messages
		}
	}
	if(stompClient){
		stompClient.disconnect();
		stompClient = null;
	}
	
	var gameName = $("#gameName").html();
	
	var socket = new SockJS('/main-websocket'); 
	stompClient = Stomp.over(socket); 
	
	stompClient.connect({}, function(frame){
		console.log("Shiritori connect frame: " + frame);
		subToShiritoriGame(gameName);
		},
		//error handler function
		function(frame){alert("Error connecting: " + frame);}
	);
	
	$("#shiritoriUserMessageBox").keypress(function(key){
			if(key.which === 13){
				sendShiritoriMessage();
			}
	});
	
}

function subToShiritoriGame(gameName){
	var shiritoriUrl = "/topic/shiritori/" + gameName;
	var shiritoriSubscription = stompClient.subscribe(shiritoriUrl, function(reply){
		handleShiritoriMessage(JSON.parse(reply.body));
	});
}


function handleShiritoriMessage(messageObject){
	var builtMessage = messageObject.userName + ": " + messageObject.message;
	var timeStamp = new Date().toTimeString().split(" ")[0];
	$("#shiritoriUserMessageBox").before(`<span>${timeStamp} - ${builtMessage}</span><br/>`);
}

function sendShiritoriMessage(){
	
	var input = $("#shiritoriUserMessageBox");
	var shiritoriGameName = $("#gameName").html();
	
	var m = {
		userName: $("#userName").html(),
		message: input.val(),
		gameName: shiritoriGameName
	}
	stompClient.send(`/app/shiritori/${shiritoriGameName}`, {}, JSON.stringify(m));
	input.val("");
}