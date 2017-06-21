var baseUrl = 'http://localhost:8080/reddit-clone/rest';

$(document).ready(function () {

    $('#navbarLoggedIn').hide();
    $('#adminActionsPanel').hide();
    $('#userActionsPanel').hide();

    checkLoggedInStatus();

    loadFollowedSubforum();

    loadSubforumLinks();

    loadUserList();

    loadProfileDetails();

    loadMessages();

    // forms setup
    var loginFormId = 'loginForm';
    $('#' + loginFormId).submit(function(e) {
        handleForm(e, loginFormId);
    });

    var registerFormId = 'registerForm';
    $('#' + registerFormId).submit(function(e) {
        handleForm(e, registerFormId);
    });

    var messageFormId = 'messageForm';
    $('#' + messageFormId).submit(function(e) {
        handleForm(e, messageFormId);
    });

    var addSubforumFormId = 'addSubforumForm';
    $('#' + addSubforumFormId).submit(function(e) {
        handleForm(e, addSubforumFormId);
    });

    var editSubforumFormId = 'editSubforumForm';
    $('#' + editSubforumFormId).submit(function(e) {
        handleForm(e, editSubforumFormId);
    });

    $('#addTopicForm').submit(function(e) {
        addTopic(e);
    });

    // buttons setup
    $('#editSubforumButton').click(function() {
        editSubforum();
    });

    $('#deleteSubforumButton').click(function() {
        deleteSubforum();
    });

    $('#logout').click(function() {
        logoutUser();
    });

});

function editSubforum() {
    var activeSubforumId = $('#subforumNameH3').text();
    $('#EditSubforumFormName').val(activeSubforumId);
    $('#editSubforum').modal('show');
}

function deleteSubforum() {
    var activeSubforumId = $('#subforumNameH3').text();
    $.ajax({
        url: baseUrl + "/subforum/delete/" + activeSubforumId
    }).then(function (message) {
        alert(message);
        refresh();
    });
}

function logoutUser() {

    $.ajax({
        url: baseUrl + "/user/logout"
    }).then(function(message) {
        alert(message);
        refresh();
    });

}

function handleForm(e, formId) {
    var form = $('#' + formId);
    e.preventDefault();
    $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
        data: form.serialize(),
        success: function(data) {
            alert(data);
            $('.modal').modal('hide');
            refresh();
        },
        error: function(data) {
            alert('An error occurred.');
        },
    });

}

function loadMessages() {
    $.ajax({
        url: "http://localhost:8080/reddit-clone/rest/index/messages"
    }).then(function(messages) {
        messages.forEach(function(message) {
            var messageRow = '<p>' + message.senderId + " " + message.content + '</p>';
            $('#messages').append(messageRow);
        });
    });
}

function checkLoggedInStatus() {
    $.ajax({
        url: baseUrl + "/user/active"
    }).then(function (user) {

        //alert(user.role);

        if(user != undefined) {
            $('#navbarLogin').hide();
            $('#navbarRegister').hide();
        }
        if(user.role == "user") {
            $('#navbarLoggedIn').show();
            $('#userActionsPanel').show();
        }
        else if(user.role == "admin" || user.role == "moderator") {
            // append delete update forum action
            $('#navbarLoggedIn').show();
            $('#userActionsPanel').show();
            $('#adminActionsPanel').show();
        }

    });
}

function loadFollowedSubforum() {
    $.ajax({
        url: baseUrl + "/user/active"
    }).then(function (user) {

        $('#subforumName').empty();

        if (user.followedSubforums.length > 0) {
            $('#subforumName').append('<h3 id="subforumNameH3">' + user.followedSubforums[0].name + '</h3>');
        }

        $('#topics').empty();

        user.followedSubforums.forEach(function (subforum) {
            loadSubforum(subforum.name);
        });

    });
}

function loadSubforumLinks() {
    $.ajax({
        url: baseUrl + "/index/subforums"
    }).then(function (subforums) {

        subforums.forEach(function (subforum) {
            $('#subforumsLinks').append(
                '<p><a href="/Reddit/subforum.html">' + subforum.name + " " + subforum.description + '</a></p>'
            );

            $('#subforumsSidebarList').append('<a href="#" class="subforumLink" id="' + subforum.name + '">' + subforum.name + '</a>&nbsp;');
        });

        $(".subforumLink").click(function () {
            var clickedButtonId = $(this).attr('id');
            loadSubforum(clickedButtonId);
        });

    });
}

function loadUserList() {
    $.ajax({
        url: baseUrl + "/index/users"
    }).then(function (users) {

        users.forEach(function (user) {
            var tableRow = '<tr>' +
                '<td>' + user.username + '</td>' +
                '<td><a href="/Reddit/rest/user/update/admin/' + user.username + '">Postavi kao administratora</a></td>' +
                '<td><a href="/Reddit/rest/user/update/moderator/' + user.username + '">Postavi kao moderatora</a></td>' +
                '<td><a href="/Reddit/rest/user/update/user/' + user.username + '">Postavi kao korisnika</a></td>' +
                '</tr>';

            $('#users tbody').append(tableRow);
        });
    });
}

function loadProfileDetails() {
    $.ajax({
        url: baseUrl + "/user/active"
    }).then(function (user) {

        user.savedTopics.forEach(function (savedTopic) {
            $('#profileSavedTopic').append('<p>' + savedTopic.name + '</p>');
        });

        user.savedComments.forEach(function (savedComment) {
            $('#profileSavedComments').append('<p>' + savedComment.name + '</p>');
        });

        user.likedTopics.forEach(function (likedTopic) {
            $('#profileLikes').append('<p>' + likedTopic.name + '</p>');
        });

        user.dislikedTopics.forEach(function (dislikedTopic) {
            $('#profileDislikes').append('<p>' + dislikedTopic.name + '</p>');
        });

        user.followedSubforums.forEach(function (subforum) {
            $('#profileFollowedSubforums').append('<p>' + subforum.name + '</p>');
        });

    });
}

function loadSubforum(subforumId) {

    $.ajax({
        url: baseUrl + '/subforum/' + subforumId + '/topics'
    }).then(function (topics) {

        $('#subforumName').empty();
        $('#subforumName').append('<h3 id="subforumNameH3">' + topics[0].parentSubforumName + '</h3>');

        $('#topics').empty();

        topics.forEach(function (topic) {
            var tableRow = '<tr>' +
                '<td>' +
                '<table border="1">' +
                '<tbody>' +
                '<tr>' +
                '<td><a href="/reddit-clone/rest/topic/like/' + topic.parentSubforumName + '/' + topic.name + '">Like</a></td>' +
                '<td><a href="#" class="topicId">' + topic.name + '</a></td>' +
                '<td><a href="/reddit-clone/rest/topic/saveTopic/' + topic.parentSubforumName + '/' + topic.name + '">Save</a></td>' +
                '</tr>' +
                '<tr>' +
                '<td>' + (parseInt(topic.likes) - parseInt(topic.dislikes)) + '</td>' +
                '<td>submitted by ' + topic.author.username + '</td>' +
                '<td></td>' +
                '</tr>' +
                '<tr>' +
                '<td><a href="/reddit-clone/rest/topic/dislike/' + topic.parentSubforumName + '/' + topic.name + '">Dislike</a></td>' +
                '<td>' + topic.comments.length + ' comments</td>' +
                '<td><a href="">Report</a></td>' +
                '</tr>' +
                '</tbody>' +
                '</table>' +
                '</td>'
            '</tr>'

            $('#topics').append(tableRow);

            $('.topicId').click(function () {
                var clickedTopic = $(this).text();

                $.ajax({
                    url: baseUrl + '/subforum/load/' + topics[0].parentSubforumName + '/' + clickedTopic + ''
                }).then(function (topic) {

                    $('#topicNameModal').empty();
                    $('#topicContentModal').empty();

                    $('#topicNameModal').append('<h4 class="modal-title">' + topic.name + '</h4>');
                    //alert(topic.type);

                    if (topic.type == "text") {
                        $('#topicContentModal').append('<div>' + topic.content + '</div>');
                    }
                    else if (topic.type == "link") {
                        $('#topicContentModal').append('<div><a href="' + topic.content + '">' + topic.name + '</a></div>');
                    }
                    else if (topic.type == "image") {
                        $('#topicContentModal').append('<div><img src="' + topic.content + ' alt="Nema prikaza"></div>');
                    }

                    $('#topicContentModal').append('<div>' + topic.content + '</div>');
                    $('#showTopic').modal('show');
                });

            });

        });
    });

}

function addTopic(e) {
    e.preventDefault();

    var activeSubforumId = $('#subforumNameH3').text();
    var form = $('#addTopicForm');

    alert(form.serialize());
    
    $.ajax({
        type: form.attr('method'),
        url: baseUrl + '/topic/create/' + activeSubforumId,
        data: form.serialize(),
        success: function(data) {
            alert(data);
            $('.modal').modal('hide');
            loadSubforum(activeSubforumId);
        },
        error: function(data) {
            alert('An error occurred.');
        },
    });

}

function refresh() {
    location.reload(true);
}
