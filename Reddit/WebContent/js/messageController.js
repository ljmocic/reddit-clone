function loadMessages() {
    $.ajax({
        url: baseUrl + "/index/messages"
    }).then(function (messages) {

        var messageId = 0;
        var unseenMessages = 0;
        messages.forEach(function (message) {

            var messageRow = '<p>From: ' + message.senderId + " Message: " + message.content;

            if (message.hasReport == true && message.seen == false) {
                messageRow += '&nbsp;<button type="button" id="deleteReport' + messageId + '" class="btn btn-info btn-sm">Delete</button>';
                messageRow += '&nbsp;<button type="button" id="warnReport' + messageId + '" class="btn btn-info btn-sm">Warn</button>';
                messageRow += '&nbsp;<button type="button" id="rejectReport' + messageId + '" class="btn btn-info btn-sm">Reject</button>';
            }


            if (message.seen == false) {
                messageRow += '&nbsp;<button type="button" id="seen' + messageId + '" class="btn btn-info btn-sm">Read</button>';
                unseenMessages++;
            }

            $('#messages').append(messageRow + '</p>');

            if (message.seen == false) {
                var tempMessageId = messageId;
                $("#seen" + tempMessageId).click(function () {
                    setMessageSeen(tempMessageId);
                });
            }

            if (message.hasReport == true) {
                var tempMessageId = messageId;
                $("#deleteReport" + tempMessageId).click(function () {
                    deleteEntityNotification(message, tempMessageId);
                });

                $("#warnReport" + tempMessageId).click(function () {
                    var tempMessageId = messageId;
                    warnEntityNotification(message, tempMessageId);
                });

                $("#rejectReport" + tempMessageId).click(function () {
                    var tempMessageId = messageId;
                    rejectEntityNotification(message, tempMessageId);
                });
            }

            messageId++;

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