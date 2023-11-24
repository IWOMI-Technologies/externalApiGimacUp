var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    //  var socket = new SockJS('http://localhost:8765/ws');
    var socket = new SockJS('http://57.128.163.118:9090/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.send("/digitalbank/userAll", {}, JSON.stringify({'wallet': $("#sender").val(),'id': $("#id").val()}));
        stompClient.subscribe('/topic/transactionAll', function (greeting) {
            // showGreeting(JSON.parse(greeting.body).chl1);
            // showGreeting(JSON.parse(greeting.body).message);
        });
    });
}
function connect2() {
    var socket = new SockJS('http://localhost:9090/ws');
    // var socket = new SockJS('http://57.128.163.118:8765/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.send("/digitalbank/userAll", {}, JSON.stringify({'wallet': $("#sender").val(),'id': $("#id").val()}));
        stompClient.subscribe('/topic/transactionAll', function (greeting) {
            // showGreeting(JSON.parse(greeting.body).chl1);
            // showGreeting(JSON.parse(greeting.body).message);
        });
    });

}

function connect3() {
    var socket = new SockJS('http://localhost:9090/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.send("/digitalbank/user", {}, JSON.stringify({'sender': $("#sender").val(),  'id': $("#id").val()}));
        stompClient.subscribe('/topic/greetings', function (greeting) {
            //showGreeting(JSON.parse(greeting.body).chl1);
            //showGreeting(JSON.parse(greeting.body).message2);  @SendTo("/topic/greetings")
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
    stompClient.send("/topic/notifT", {}, JSON.stringify({'sender': $("#sender").val(), 'message': $("#message").val(), 'message2': $("#message2").val(), 'id': $("#id").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#connect2" ).click(function() { connect2(); });
    $( "#connect3" ).click(function() { connect3(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});