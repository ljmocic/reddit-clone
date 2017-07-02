function loadMessages() {
    $.ajax({
        url: baseUrl + "/index/messages"
    }).then(function (messages) {

        var unseenMessages = 0;
        for (var i = 0; i < messages.length; i++) {

            var messageRow = '<p>From: ' + messages[i].senderId + " Message: " + messages[i].content;

            if (messages[i].hasReport == true && messages[i].seen == false) {
                messageRow += '&nbsp;<button type="button" id="' + i + '" class="deleteReport">Delete</button>';
                messageRow += '&nbsp;<button type="button" id="' + i + '" class="warnReport">Warn</button>';
                messageRow += '&nbsp;<button type="button" id="' + i + '" class="rejectReport">Reject</button>';
            }


            if (messages[i].seen == false) {
                messageRow += '&nbsp;<button type="button" id="' + i + '" class="seen">Read</button>';
                unseenMessages++;
            }

            $('#messages').append(messageRow + '</p>');

        }

        $(".seen").click(function () {
            var messageId = $(this).attr('id');
            setMessageSeen(messageId);
        });

        $(".deleteReport").click(function () {
            var messageId = $(this).attr('id');
            deleteEntityNotification(messages[messageId], messageId);
        });

        $(".warnReport").click(function () {
            var messageId = $(this).attr('id');
            warnEntityNotification(messages[messageId], messageId);
        });

        $(".rejectReport").click(function () {
            var messageId = $(this).attr('id');
            rejectEntityNotification(messages[messageId], messageId);
        });

        $('#receivedMessagesBadge').text(unseenMessages);
    });
}

function setMessageSeen(messageId) {
    $.ajax({
        url: baseUrl + "/user/seen/" + messageId
    }).then(function (message) {
        alert(message);
        $('#messages').empty();
        loadMessages();
    });
}