var stompClient = null;

function setConnected(connected) {
	document.getElementById("connect").disabled = connected;
	document.getElementById("disconnect").disabled = !connected;

    if (connected) {
    	 document.getElementById("conversation").style.display = 'block';
    }
    else {
    	document.getElementById("conversation").style.display = 'none';
    }

    document.getElementById("greetings").innerHTML ="";
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    //stompClient.debug = () => {};

    stompClient.debug = f => f;
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/gretting', function (greeting) {
            showGreeting(JSON.parse(greeting.body).name);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	var name = document.getElementById("name").value;
    stompClient.send("/app/hello", {}, JSON.stringify({'name': name}));
}

function showGreeting(message) {
	var add = "<tr><td>" + message + "</td></tr>";
	document.getElementById("greetings").innerHTML += add;
}