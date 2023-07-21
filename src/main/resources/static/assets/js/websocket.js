var stompClient = null;

var socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    console.log(frame);
    stompClient.subscribe('/all/notification', function(result) {
        show(JSON.parse(result.body));
    });
});

var socket = new SockJS('/ws');
privateStompClient = Stomp.over(socket)
privateStompClient.connect({}, function (frame) {
    console.log(frame)
    privateStompClient.subscribe('/user/specific', function (result) {
        console.log(result.body)
        show(JSON.parse(result.body))
    })
})

function sendMessage() {
    var text = document.getElementById('text').value
    stompClient.send('/app/application', {}, JSON.stringify({ from: from, text: text }))
}
