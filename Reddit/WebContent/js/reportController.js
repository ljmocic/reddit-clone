function deleteEntityNotification(message, messageId) {
    var path;
    if (message.report.subforumId != "") {
        path = "/subforum/report/delete/";
    }
    if (message.report.topicId != "") {
        path = "/topic/report/delete/";
    }

    $.ajax({
        url: baseUrl + path + messageId
    }).then(function (message) {
        alert(message);
        $('#messages').empty();
        loadMessages();
    });
}

function warnEntityNotification(message, messageId) {
    var path;
    if (message.report.subforumId != "") {
        path = "/subforum/report/warn/";
    }
    if (message.report.topicId != "") {
        path = "/topic/report/warn/";
    }

    $.ajax({
        url: baseUrl + path + messageId
    }).then(function (message) {
        alert(message);
        $('#messages').empty();
        loadMessages();
    });
}

function rejectEntityNotification(message, messageId) {
    var path;
    if (message.report.subforumId != "") {
        path = "/subforum/report/reject/";
    }
    if (message.report.topicId != "") {
        path = "/topic/report/reject/";
    }

    $.ajax({
        url: baseUrl + path + messageId
    }).then(function (message) {
        alert(message);
        $('#messages').empty();
        loadMessages();
    });
}