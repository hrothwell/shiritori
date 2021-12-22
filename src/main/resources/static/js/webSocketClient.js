/**
 * 
 */
//TODO: should this client be reused and we just unsubscribe/resubscribe to things? rather than disconnecting the client every time they switch rooms?
var stompClient = null; //make this private somehow? Only accessible via our functions? 
var finalRoom = "";
var subscriptions = []; //all stomp subscriptions
$(document).ready(function(){
	//start in random room
	var roomId = Math.floor(Math.random() * 100).toString(); //testing with having multiple rooms (random num means every user is in their own room most likely)
	connect(roomId);
	
	//form is being submitted or something with button, disable it
	 $("form").on('submit', function (e) {
        e.preventDefault();
    });

	$("#send").on("click", function(){
		sendMessage();
	});
	
	$("#subscribe").click(function(){
		subscribeTo();
	});
});

function connect(room){
	if(stompClient){
		stompClient.disconnect();//disconnect client from server if it already exists
	}
	var socket = new SockJS('/gs-guide-websocket'); //thing defined in controller for SockJS
	stompClient = Stomp.over(socket); //will this auto disconnect from current socket? 
	
	//TODO add an "onError" and "onClose" for client
	stompClient.connect({}, function(frame){
		console.log("Connect frame: " + frame);
		subscribeTo(room);
	});
}

function subscribeTo(room){
	for(s of subscriptions){
		//currently unsubscribing to each room, but still lives in array we have
		//will probably allow them to subscribe to multiple at the same time in the future
		s.unsubscribe();
	}
	
	if(room){
		url = "/topic/" + room;
		finalRoom = room;
	}
	else{
		var userRoom = $("#room").val();
		url = "/topic/" + userRoom;
		finalRoom = userRoom;
	}
	
	var newSub = stompClient.subscribe(url, function(reply){
		handleMessage(JSON.parse(reply.body).userName + ": " + JSON.parse(reply.body).message);
	});
	
	subscriptions.push(newSub);
}
//TODO, allow passing in a div that we can "print" to. If it doesn't exist, make one first then append to it
function handleMessage(serverMessage){
	$("#messageBox").append(`<br/>${serverMessage}`);
}

function sendMessage(){
	var messageObject = {
		userName: $("#name").val(),
		message: $("#message").val()
	}
	//we need to prepend /app so it looks for our annotated server method? 
	stompClient.send("/app/" + finalRoom, {}, JSON.stringify(messageObject));
}
