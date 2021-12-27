/**
 * 
 */
$(document).ready(function(){
	$("#createGameButton").click(function(){
		createGame();
	});
});

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
			
	}).done(function(data){
		console.log("In 'done' block now");
		setupShiritoriStompClient();
		//setup STOMP client here? 
		$("#shiritoriUserMessageBox").keypress(function(key){
			if(key.which === 13){
				sendShiritoriMessage();
			}
			
		})
	});
}

function setupShiritoriStompClient(){
	
	if(subscriptions){
		for(var [key, value] of subscriptions){
			value.unsubscribe(); //remove the room so they stop getting messages
		}
	}
	if(stompClient){
		stompClient.disconnect();
		stompClient = null; //check for the client and remove it
	}
	var gameName = $("#gameName").html();
	
	var socket = new SockJS('/gs-guide-websocket'); 
	stompClient = Stomp.over(socket); 
	
	//TODO add an "onError" and "onClose" for client
	stompClient.connect({}, function(frame){
		console.log("Shiritori connect frame: " + frame);
		subToShiritoriGame(gameName);
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
	$("#shiritoriUserMessageBox").before(`<span>${timeStamp} - ${builtMessage}<br/>`);
}

function sendShiritoriMessage(){
	
	var input = $("#shiritoriUserMessageBox");
	var shiritoriGameName = $("#gameName").html();
	
	var m = {
		userName: "",
		message: input.val(),
		gameName: shiritoriGameName
	}
	stompClient.send(`/app/shiritori/${shiritoriGameName}`, {}, JSON.stringify(m));
	input.val("");
}