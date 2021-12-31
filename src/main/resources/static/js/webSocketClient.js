/** TODO rename file to be more specific to the chat boxes
 * 
 */
//TODO: should this client be reused and we just unsubscribe/resubscribe to things? rather than disconnecting the client every time they switch rooms?
var stompClient = null; //make this private somehow? Only accessible via our functions? 
var subscriptions = new Map(); //all stomp subscriptions key=topic endpoint value=subscription id
var MD5 = new Hashes.MD5();
$(document).ready(function(){
	//start in random room
	var roomId = "lobby"; //drop everyone here to start
	connect(roomId);
	
	//form is being submitted or something with button, disable it
	 $("form").on('submit', function (e) {
        e.preventDefault();
    });

	$("#subscribe").click(function(){
		subscribeTo();
	});
});

function connect(room){
	if(stompClient){
		stompClient.disconnect();//disconnect client from server if it already exists
	}
	var socket = new SockJS('/main-websocket'); //thing defined in controller for SockJS
	stompClient = Stomp.over(socket); //will this auto disconnect from current socket? 
	
	//TODO add an "onError" and "onClose" for client
	stompClient.connect({}, function(frame){
		console.log("Connect frame: " + frame);
		subscribeTo(room);
	},
	function(frame){
		alert("Error: " + frame);
	});
}

function subscribeTo(room){
	if(room){
		url = "/topic/" + room;
	}
	else{
		var userRoom = $("#room").val();
		url = "/topic/" + userRoom;
		room = userRoom;
	}
	
	if(!room.match(/^[a-zA-Z0-9]+$/)){
		alert("Please only use alphanumerics, or you will end up nowhere");
		return;
	}
	roomMD5 = MD5.hex(room);
	
	if(subscriptions.has(room)){
		alert("You are already in that room");
		return;
	}
	
	var userMessageRoomBox = `${roomMD5}UserMessage`;
	var serverMessageBox = `${roomMD5}serverMessages`;
	
	//TODO Something here can error out when they send bad room name, server sends error but div is still made 
	var newSub = stompClient.subscribe(url, function(reply){
		handleMessage(JSON.parse(reply.body).userName + ": " + JSON.parse(reply.body).message, serverMessageBox);
	});
	
	if(!document.getElementById(roomMD5)){
		//Manually build the input for the new room
		$("#messageBox").append(`<div id=${roomMD5} class="generalMessages">
			<div>
				<b>${room}</b>
				<button id="${roomMD5}UnsubscribeButton">Exit</button>
			</div>
			<br/>
			<div id="${serverMessageBox}" class="serverMessageBox"></div>
			<div class="userMessageBox">
				<input id="${userMessageRoomBox}" type="text" class="messageText" placeholder="..."/>
			</div>`);
		//When user hits enter, send message
		$(`#${userMessageRoomBox}`).keypress(function(key){
			if(key.which === 13){
				sendMessage(this, room); //send message to the room
			}
			
		});
		$(`#${roomMD5}UnsubscribeButton`).click(function(){
			unsubscribeFrom(room);
		});
	}
	else{
		$(`#${userMessageRoomBox}`).append(`<br/><b>Reconnected to ${room}<b/>`);
	}
	subscriptions.set(room, newSub);
	uiSetup();//todo move this somewhere else? lives in shiritori.js
}

function unsubscribeFrom(room){
	var roomMD5 = MD5.hex(room);
	//disconnect from that room and also delete the entry in the map
	if(subscriptions.has(room)){
		subscriptions.get(room).unsubscribe();
		subscriptions.delete(room);
	}
	alert("removed from " + room);
	$(`#${roomMD5}`).remove(); //get rid of everything associated with the room. if this stays here they can still send messages, they just wont see anything
}

//print message to client's chat "room" 
function handleMessage(serverMessage, messageBoxId){
	//TODO This would probably be a good place to use like Vue/React? Can talk to Jeet
	//Want to also add buttons and onclicks within these divs we are making
	//change the "message" box to be within this box (the message they send I mean) + get rid of "send message" button and instead just send on keypress for enter
	var timeStamp = new Date().toTimeString().split(" ")[0];
	if(document.getElementById(messageBoxId)){
		
		$(`#${messageBoxId}`).append(`<span class="messageText">${timeStamp} ${serverMessage}</span><br/>`);
		var d = $(`#${messageBoxId}`);
		d.scrollTop(d.prop("scrollHeight"));
	}
	else{
		//TODO don't alert them
		alert("Something didn't work, how did you get here...");
	}
}

function sendMessage(domTextInput, destinationRoom){
	var messageObject = {
		userName: $("#name").val(),
		message: domTextInput.value
	}
	//we need to prepend /app so it looks for our annotated server method? 
	stompClient.send("/app/" + destinationRoom, {}, JSON.stringify(messageObject));
	domTextInput.value = "";
}
