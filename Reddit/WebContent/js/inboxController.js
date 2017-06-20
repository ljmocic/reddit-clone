$(document).ready(function(){

	$.ajax({
        url: "http://localhost:8080/reddit-clone/rest/index/messages"
    }).then(function(messages) {
        messages.forEach(function(message) {
            var messageRow = '<p>' + message.senderId + " " + message.content + '</p>';
            $('#messages').append(messageRow);
        });
    });
    	
});
