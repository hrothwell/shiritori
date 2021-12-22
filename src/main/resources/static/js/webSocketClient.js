/**
 * 
 */
var stompClient = null; //make this private somehow? Only accessible via our functions? 
var finalRoom = "";
$(document).ready(function(){
	//start in random room
	var roomId = Math.random().toString(); //testing with having multiple rooms (random num means every user is in their own room most likely)
	connect(roomId);
	
	//form is being submitted or something with button, disable it
	 $("form").on('submit', function (e) {
        e.preventDefault();
    });

	$("#send").on("click", function(){
		sendMessage();
	});
	
	$("#connect").click(function(){
		connect();
	});
});

function connect(room){
	//TODO : Is there a potential memory leak when a user changes rooms but does so in a way to bypass our stompClient.disconnect() call in js? 
	if(stompClient){
		stompClient.disconnect();//disconnect from any prior connections
	}
	var socket = new SockJS('/gs-guide-websocket'); //thing defined in controller for SockJS
	stompClient = Stomp.over(socket); //will this auto disconnect from current socket? 
	if(room){
		url = "/topic/" + room;
		finalRoom = room;
	}
	else{
		var userRoom = $("#room").val();
		url = "/topic/" + userRoom;
		finalRoom = userRoom;
	}
	
	stompClient.connect({}, function(frame){
		stompClient.subscribe(url, function(reply){
			handleMessage(JSON.parse(reply.body).userName + ": " + JSON.parse(reply.body).message);
		});
		console.log("Connected to webSocket?? " + frame);
	});
}

function handleMessage(serverMessage){
	$("#messageBox").append(`<br/>${serverMessage }`);
}

function sendMessage(){
	var messageObject = {
		userName: $("#name").val(),
		message: $("#message").val()
	}
	//we need to prepend /app so it looks for our annotated server method? 
	stompClient.send("/app/" + finalRoom, {}, JSON.stringify(messageObject));
}
//TODO any disconnecting need to happen? see above TODO for memory leak stuff 
