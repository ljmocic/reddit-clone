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
    // TODO refactor later
    var loginFormId = 'loginForm';
    $('#' + loginFormId).submit(function (e) {
        handleForm(e, loginFormId);
    });

    var registerFormId = 'registerForm';
    $('#' + registerFormId).submit(function (e) {
        handleForm(e, registerFormId);
    });

    var messageFormId = 'messageForm';
    $('#' + messageFormId).submit(function (e) {
        handleForm(e, messageFormId);
    });

    var addSubforumFormId = 'addSubforumForm';
    $('#' + addSubforumFormId).submit(function (e) {
        handleForm(e, addSubforumFormId);
    });

    var editSubforumFormId = 'editSubforumForm';
    $('#' + editSubforumFormId).submit(function (e) {
        handleForm(e, editSubforumFormId);
    });

    var reportTopicFormId = 'reportTopicForm';
    $('#' + reportTopicFormId).submit(function (e) {
        handleForm(e, reportTopicFormId);
    });

    var sendMessageFormId = 'sendMessageForm';
    $('#' + sendMessageFormId).submit(function (e) {
        handleForm(e, sendMessageFormId);
    });

    $('#addTopicForm').submit(function (e) {
        addTopic(e);
    });

    // buttons setup
    $('#editSubforumButton').click(function () {
        editSubforum();
    });

    $('#deleteSubforumButton').click(function () {
        deleteSubforum();
    });

    $('#logout').click(function () {
        logoutUser();
    });

});

function followSubforum() {
    var subforumId = $('#subforumNameH3').text();
    $.ajax({
        url: baseUrl + "/subforum/follow/" + subforumId
    }).then(function (message) {
        alert(message);
    });
}

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
    }).then(function (message) {
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
        success: function (data) {
            alert(data);
            $('.modal').modal('hide');
            refresh();
        },
        error: function (data) {
            alert('An error occurred.');
        },
    });

}

function loadMessages() {
    $.ajax({
        url: "http://localhost:8080/reddit-clone/rest/index/messages"
    }).then(function (messages) {
        messages.forEach(function (message) {
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

        if (user != undefined) {
            $('#navbarLogin').hide();
            $('#navbarRegister').hide();
        }
        if (user.role == "user") {
            $('#loggedInUsername').text(user.username);
            $('#navbarLoggedIn').show();
            $('#userActionsPanel').show();
        }
        else if (user.role == "admin" || user.role == "moderator") {
            // append delete update forum action
            $('#loggedInUsername').text(user.username);
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

        user.followedSubforums.forEach(function (subforumId) {
            loadSubforum(subforumId, true);
        });

    });
}

function loadSubforumLinks() {
    $.ajax({
        url: baseUrl + "/index/subforums"
    }).then(function (subforums) {

        subforums.forEach(function (subforum) {
            $('#subforumsLinks').append(
                '<p><a class="subforumLink" href="#">' + subforum.name + " " + subforum.description + '</a></p>'
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

        $('#users tbody').empty();
        // table header
        var tableRow = '<tr>' +
            '<td>username</td>' +
            '<td>role</td>' +
            '<td></td>' +
            '<td></td>' +
            '<td></td>' +
            '</tr>';

        $('#users tbody').append(tableRow);

        users.forEach(function (user) {
            var tableRow = '<tr>' +
                '<td>' + user.username + '</td>' +
                '<td>' + user.role + '</td>' +
                '<td><a class="setRoleAdmin" href="#">Postavi kao administratora</a></td>' +
                '<td><a class="setRoleModerator" href="#">Postavi kao moderatora</a></td>' +
                '<td><a class="setRoleUser" href="#">Postavi kao korisnika</a></td>' +
                '</tr>';

            $('#users tbody').append(tableRow);
        });

        $('.setRoleAdmin').click(function () {
            var username = $(this).closest('tr').find('td:first-child').text();
            $.ajax({
                url: baseUrl + '/user/update/admin/' + username
            }).then(function (message) {
                alert(message);
                loadUserList();
            });
        });

        $('.setRoleModerator').click(function () {
            var username = $(this).closest('tr').find('td:first-child').text();
            $.ajax({
                url: baseUrl + '/user/update/moderator/' + username
            }).then(function (message) {
                alert(message);
                loadUserList();
            });
        });

        $('.setRoleUser').click(function () {
            var username = $(this).closest('tr').find('td:first-child').text();
            $.ajax({
                url: baseUrl + '/user/update/user/' + username
            }).then(function (message) {
                alert(message);
                loadUserList();
            });
        });

    });
}

function loadProfileDetails() {
    $.ajax({
        url: baseUrl + "/user/active"
    }).then(function (user) {

        user.savedTopics.forEach(function (savedTopic) {
            $('#profileSavedTopic').empty();
            $('#profileSavedTopic').append('<p>' + savedTopic.name + '</p>');
        });

        user.savedComments.forEach(function (savedComment) {
            $('#profileSavedComments').empty();
            $('#profileSavedComments').append('<p>' + savedComment.name + '</p>');
        });

        user.likedTopics.forEach(function (likedTopic) {
            $('#profileLikes').empty();
            $('#profileLikes').append('<p>' + likedTopic.name + '</p>');
        });

        user.dislikedTopics.forEach(function (dislikedTopic) {
            $('#profileDislikes').empty();
            if(dislikedTopic.name != undefined) {
                $('#profileDislikes').append('<p>' + dislikedTopic.name + '</p>');
            }
        });

        user.followedSubforums.forEach(function (subforum) {
            $('#profileFollowedSubforums').empty();
            $('#profileFollowedSubforums').append('<p>' + subforum.name + '</p>');
        });

    });
}

function loadSubforum(subforumId, followedForumsMode) {

    $.ajax({
        url: baseUrl + '/subforum/' + subforumId + '/topics'
    }).then(function (topics) {

        // TODO refator this
        if (followedForumsMode == true) {

        }
        else {
            $('#subforumName').empty();
            $('#subforumName').append('<h3 id="subforumNameH3">' + topics[0].parentSubforumName + '</h3><a id="followSubforumLink" href="#">Follow</a>');
            $('#followSubforumLink').click(function () {
                followSubforum();
            });
            $('#topics').empty();
        }

        topics.forEach(function (topic) {
            var tableRow = '<tr>' +
                '<td>' +
                '<table border="1">' +
                '<tbody>' +
                '<tr>' +
                '<td><a href="#" class="likeTopic">Like</a></td>' +
                '<td><a href="#" class="topicId">' + topic.name + '</a></td>' +
                '<td><a href="#" class="saveTopic">Save</a></td>' +
                '</tr>' +
                '<tr>' +
                '<td>' + (parseInt(topic.likes) - parseInt(topic.dislikes)) + '</td>' +
                '<td>submitted by ' + topic.author.username + '</td>' +
                '<td><a href="#" class="deleteTopic">Delete</a><a href="#" class="editTopic">Edit</a></td>' +
                '</tr>' +
                '<tr>' +
                '<td><a href="#" class="dislikeTopic">Dislike</a></td>' +
                '<td>' + topic.comments.length + ' comments</td>' +
                '<td><a href="#" class="reportTopic">Report</a></td>' +
                '</tr>' +
                '</tbody>' +
                '</table>' +
                '</td>'
            '</tr>'

            $('#topics').append(tableRow);

        });

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
                    $('#topicContentModal').append('<div><img src="' + topic.content + '" alt="Nema prikaza"></div>');
                }

                $('#topicContentModal').append('<div>' + topic.content + '</div>');
                $('#showTopic').modal('show');
            });

        });

        $('.likeTopic').click(function () {
            var clickedTopicId = $(this).closest('table').find('.topicId').text();

            $.ajax({
                url: baseUrl + '/topic/like/' + topics[0].parentSubforumName + '/' + clickedTopicId
            }).then(function (message) {
                alert(message);
                refresh();
            });

        });

        $('.dislikeTopic').click(function () {
            var clickedTopicId = $(this).closest('table').find('.topicId').text();

            $.ajax({
                url: baseUrl + '/topic/dislike/' + topics[0].parentSubforumName + '/' + clickedTopicId
            }).then(function (message) {
                alert(message);
                refresh();
            });

        });

        $('.saveTopic').click(function () {
            var clickedTopicId = $(this).closest('table').find('.topicId').text();

            $.ajax({
                url: baseUrl + '/topic/saveTopic/' + topics[0].parentSubforumName + '/' + clickedTopicId
            }).then(function (message) {
                alert(message);
                refresh();
            });

        });

        $('.reportTopic').click(function () {
            var clickedTopicId = $(this).closest('table').find('.topicId').text();

            $('#reportSubforumIdInputName').val(topics[0].parentSubforumName);
            $('#reportTopicIdInputName').val(clickedTopicId);

            $('#reportTopic').modal('show');
        });

        $('.deleteTopic').click(function () {
            var clickedTopicId = $(this).closest('table').find('.topicId').text();

            $.ajax({
                url: baseUrl + '/topic/delete/' + topics[0].parentSubforumName + '/' + clickedTopicId
            }).then(function (message) {
                alert(message);
                loadSubforum(topics[0].parentSubforumName);
                refresh();
            });

        });

        $('.editTopic').click(function () {
            var clickedTopicId = $(this).closest('table').find('.topicId').text();
            
            topics.forEach(function (topic) {
                if(topic.name == clickedTopicId) {

                    $('#EditTopicInputName').val(topic.name);
                    $('#EditTopicInputType').val(topic.type);
                    $('#EditTopicInputContent').val(topic.content);

                    $('#editTopic').modal('show');

                    $('#editTopicForm').submit(function (e) {
                        var form = $('#editTopicForm');
                        e.preventDefault();
                        $.ajax({
                            type: form.attr('method'),
                            url: baseUrl + '/topic/update/' + topic.parentSubforumName + '/' + topic.name,
                            data: form.serialize(),
                            success: function (data) {
                                alert(data);
                                $('.modal').modal('hide');
                                refresh();
                            },
                            error: function (data) {
                                alert('An error occurred.');
                            },
                        });
                    });

                }
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
        success: function (data) {
            alert(data);
            $('.modal').modal('hide');
            loadSubforum(activeSubforumId);
        },
        error: function (data) {
            alert('An error occurred.');
        },
    });

}

function refresh() {
    location.reload(true);
}
