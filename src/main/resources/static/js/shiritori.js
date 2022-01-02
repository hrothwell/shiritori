/**
 * 
 */
$(document).ready(function(){
	$("#createGameButton").click(function(){
		createGame();
	});
	$("#joinGameButton").click(function(){
		joinGame();
	});
	$("#findGamesButton").click(function(){
		findGames();
	});
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

function joinGame(joinGameButton){
	var joinGameName = ""; 
	var joinGamePassword = "";
	var userName = "";
	//if button object passed in, should have the info we need
	//TODO I would like to not have to do this this way as going forward could make it harder to manage.
	if(joinGameButton){
		//findGames page
		joinGameName = joinGameButton.value; //game name is button's value
		var textInput = $(`#${joinGameName}_findGamePassword`).val();
		joinGamePassword = textInput ? textInput.trim() : ""; //if the password exists, use it? 
	}
	else{
		//home page
		joinGameName = $("#createOrJoinGameName").val();
		joinGamePassword = $("#createOrJoinGamePassword").val();
		userName = $("#name").val();
	}
	
	if(joinGameName.trim() === ""){
		alert("name should not be empty");
		return;
	}
	
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

function findGames(){
	clearSTOMP();
	$.ajax({
		url: "/shiritori/findGames",
		method: "GET",
		success: function(data){
			$(document.body).html(data);
			console.log("done loading");
		},
		error: function(jqXHR){
			alert("Error finding games: " + jqXHR.responseText);
		}	
	}).done(function(data){
		console.log("In 'done' block now");
	});
}


//Must be called AFTER game is loaded
function setupShiritoriStompClient(){
	clearSTOMP();
	
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
	
	uiSetup();
	
}
//Remove everything. Usually when we are re-writing to dom we will want to do this
function clearSTOMP(){
	if(subscriptions){
		for(var [key, value] of subscriptions){
			value.unsubscribe(); //remove the room so they stop getting messages
		}
	}
	if(stompClient){
		stompClient.disconnect();
		stompClient = null;
	}
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
	$("#shiritoriGameMessages").append(`<span class="messageText">${timeStamp} ${builtMessage}</span><br/>`);
	var d = $("#shiritoriGameMessages");
	d.scrollTop(d.prop("scrollHeight"));
}

function sendShiritoriMessage(){
	
	var input = $("#shiritoriUserMessageBox");
	//this isn't really used on the server even though it is added to message, the url is used
	var shiritoriGameName = $("#gameName").html(); 
	
	var m = {
		userName: $("#userName").val(),
		message: input.val(),
		gameName: shiritoriGameName
	}
	//TODO is there really any way to prevent them from sending to a game they are not a part of? 
	stompClient.send(`/app/shiritori/${shiritoriGameName}`, {}, JSON.stringify(m));
	input.val("");
}

function uiSetup(){
	$(".generalMessages").resizable();
	//TODO make text also highlightable? 
	$(".generalMessages").draggable({cancel: ".messageText"});
}