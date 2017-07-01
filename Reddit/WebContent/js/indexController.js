var baseUrl = 'http://localhost:8080/reddit-clone/rest';

$(document).ready(function () {

    $('#navbarLoggedIn').hide();
    $('#userActions').hide();
    $('#moderatorActions').hide();
    $('#adminActions').hide();

    $('#showProfileLink').click(function () {
        loadProfileDetails();
    });

    checkLoggedInStatus();

    loadSubforumLinks();

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

    // subforum forms handlers
    var addSubforumFormId = 'addSubforumForm';
    $('#' + addSubforumFormId).submit(function (e) {
        handleForm(e, addSubforumFormId);
    });

    var editSubforumFormId = 'editSubforumForm';
    $('#' + editSubforumFormId).submit(function (e) {
        handleForm(e, editSubforumFormId);
    });

    var reportSubforumFormId = 'reportSubforumForm';
    $('#' + reportSubforumFormId).submit(function (e) {
        handleForm(e, reportSubforumFormId);
    });

    var advancedSearchSubforumsFormId = 'advancedSearchSubforumsForm';
    $('#' + advancedSearchSubforumsFormId).submit(function (e) {
        handleForm(e, advancedSearchSubforumsFormId, subforumSearchResponse);
    });

    var advancedSearchTopicsFormId = 'advancedSearchTopicsForm';
    $('#' + advancedSearchTopicsFormId).submit(function (e) {
        handleForm(e, advancedSearchTopicsFormId, topicSearchResponse);
    });

    // topic handlers
    var reportTopicFormId = 'reportTopicForm';
    $('#' + reportTopicFormId).submit(function (e) {
        handleForm(e, reportTopicFormId);
    });

    // message handlers
    var sendMessageFormId = 'sendMessageForm';
    $('#' + sendMessageFormId).submit(function (e) {
        handleForm(e, sendMessageFormId);
    });

    // buttons setup
    $('#addTopicForm').submit(function (e) {
        addTopic(e);
    });

    $('#searchPressed').click(function () {
        performSearch();
    });

    $('#searchQuery').on('keypress', function (e) {
        if (e.which === 13) {
            performSearch();
        }
    });

    $('#editSubforumButton').click(function () {
        editSubforum();
    });

    $('#deleteSubforumButton').click(function () {
        deleteSubforum();
    });

    $('input:file').change(function (e) {
        uploadFile();
    });

    $('#logout').click(function () {
        logoutUser();
    });

});

function uploadFile() {
    alert("To do upload!");
}

function subforumSearchResponse(data) {
    // alert("test!");
    $('#SubforumsSearchResults').empty();

    for (var i = 0; i < data.responseJSON.length; i++) {
        $('#SubforumsSearchResults').append('<p><a href="#" id="' + data.responseJSON[i].name + '" class="searchSubforumLink">' + data.responseJSON[i].name + '</a></p>');
    }

    $('.searchSubforumLink').click(function () {
        var clickedButtonId = $(this).attr('id');
        $('.modal').modal('hide');
        loadSubforum(clickedButtonId);
    });

    $('#advancedSearchSubforums').modal('hide');
    $('#searchResultPage').modal('show');
}

function topicSearchResponse(data) {
    // alert("test!");
    $('#TopicsSearchResults').empty();

    for (var i = 0; i < data.responseJSON.length; i++) {
        $('#TopicsSearchResults').append('<p><a href="#" class="topicId' + data.responseJSON[i].parentSubforumName + '">' + data.responseJSON[i].name + '</a></p>');
    }

    $('#advancedSearchTopics').modal('hide');
    $('#searchResultPage').modal('show');
}

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
    $.ajax({
        url: baseUrl + "/subforum/" + activeSubforumId
    }).then(function (subforum) {
        //alert(message);
        $('#EditSubforumFormName').val(subforum.name);
        $('#EditSubforumFormDescription').val(subforum.description);
        $('#EditSubforumFormRules').val(subforum.rules);
        $('#EditSubforumFormModerator').val(subforum.responsibleModerator);

        $('#editSubforum').modal('show');
    });

}

function reportSubforum() {
    var activeSubforumId = $('#subforumNameH3').text();
    $('#reportSubforumFormName').val(activeSubforumId);
    $('#reportSubforum').modal('show');
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

function handleForm(e, formId, responseAction) {
    var form = $('#' + formId);
    e.preventDefault();

    var input = $('#' + formId + ' :input');

    var data = {};
    for (var i = 0; i < input.length; i++) {
        if (input[i].name) {
            data[input[i].name] = input[i].value;
        }
    }

    $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
        contentType: "application/json",
        data: JSON.stringify(data),
        complete: function (data) {
            if (responseAction == undefined) {
                alert(data.responseText);
                $('.modal').modal('hide');
                refresh();
            }
            else {
                responseAction(data);
            }
        }
    });

}

function loadMessages() {
    $.ajax({
        url: baseUrl + "/index/messages"
    }).then(function (messages) {

        var messageId = 0;
        var unseenMessages = 0;
        messages.forEach(function (message) {

            var messageRow = '<p>' + message.senderId + " " + message.content;

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

function deleteEntityNotification(message, messageId) {
    var path;
    if (message.report.subforumId != undefined) {
        path = "/subforum/report/delete/";
    }
    if (message.report.topicId != undefined) {
        path = "/topic/report/delete/";
    }

    $.ajax({
        url: baseUrl + path + messageId
    }).then(function (message) {
        alert(message);
    });
}

function warnEntityNotification(message, messageId) {
    var path;
    if (message.report.subforumId != undefined) {
        path = "/subforum/report/warn/";
    }
    if (message.report.topicId != undefined) {
        path = "/topic/report/warn/";
    }

    $.ajax({
        url: baseUrl + path + messageId
    }).then(function (message) {
        alert(message);
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

function checkLoggedInStatus() {
    $.ajax({
        url: baseUrl + "/user/active"
    }).then(function (user) {

        if (user != undefined) {
            $('#navbarLogin').hide();
            $('#navbarRegister').hide();

            $('#loggedInUsername').text(user.username);
            $('#navbarLoggedIn').show();
            $('#userActions').show();
            if (user.role == "moderator" || user.role == "admin") {
                $('#moderatorActions').show();
            }
            if (user.role == "admin") {
                $('#adminActions').show();
                loadUserList();
            }
            loadMessages();
            loadFollowedSubforum();
        }

    });
}

function loadFollowedSubforum() {
    $.ajax({
        url: baseUrl + "/user/active"
    }).then(function (user) {

        if (user.followedSubforums.length != 0) {
            user.followedSubforums.forEach(function (subforumId) {
                loadSubforum(subforumId, true);
            });
        }
        else {
            $('#topics').text("User doesn't follow any subforums!");
        }
        $('#addTopicButton').hide();

    });
}

function loadSubforumLinks() {
    $.ajax({
        url: baseUrl + "/index/subforums"
    }).then(function (subforums) {

        subforums.forEach(function (subforum) {
            $('#subforumsLinks').append(
                '<p><a class="subforumLink" id="' + subforum.name + '" href="#">' + subforum.name + " " + subforum.description + '</a></p>'
            );

            $('#subforumsSidebarList').append('<a href="#" class="subforumLink" id="' + subforum.name + '">' + subforum.name + '</a>&nbsp;');
        });

        $(".subforumLink").click(function () {
            var clickedButtonId = $(this).attr('id');
            $('.modal').modal('hide');
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

        $('#profileSavedTopic').empty();
        user.savedTopics.forEach(function (savedTopic) {
            $('#profileSavedTopic').append('<p><a href="#" class="topicId' + savedTopic.parentSubforumName + '">' + savedTopic.name + '</a></p>');
        });

        $('#profileSavedComments').empty();
        user.savedComments.forEach(function (savedComment) {
            $('#profileSavedComments').append('<p>' + savedComment.name + '</p>');
        });

        $('#profileLikes').empty();
        user.likedTopics.forEach(function (likedTopic) {
            $('#profileLikes').append('<p><a href="#" class="topicId' + likedTopic.parentSubforumName + '">' + likedTopic.name + '</a></p>');
        });

        $('#profileDislikes').empty();
        user.dislikedTopics.forEach(function (dislikedTopic) {
            $('#profileDislikes').append('<p><a href="#" class="topicId' + dislikedTopic.parentSubforumName + '">' + dislikedTopic.name + '</a></p>');
        });

        $('#profileFollowedSubforums').empty();
        user.followedSubforums.forEach(function (subforum) {
            $('#profileFollowedSubforums').append('<p><a href="#" id="' + subforum + '" class="profileSubforumLink">' + subforum + '</a></p>');
        });
        $('.profileSubforumLink').click(function () {
            var clickedButtonId = $(this).attr('id');
            $('.modal').modal('hide');
            loadSubforum(clickedButtonId);
        });

    });
}

function performSearch() {

    var searchQuery = $("#searchQuery").val();

    if(searchQuery == "") {
        return;
    }

    $.ajax({
        url: baseUrl + "/index/searchSubforums/" + searchQuery
    }).then(function (subforums) {

        $('#SubforumsSearchResults').empty();
        subforums.forEach(function (subforum) {
            $('#SubforumsSearchResults').append('<p><a href="#" id="' + subforum.name + '" class="searchSubforumLink">' + subforum.name + '</a></p>');
        });

        $('.searchSubforumLink').click(function () {
            var clickedButtonId = $(this).attr('id');
            $('.modal').modal('hide');
            loadSubforum(clickedButtonId);
        });

    });

    $.ajax({
        url: baseUrl + "/index/searchTopics/" + searchQuery
    }).then(function (topics) {

        $('#TopicsSearchResults').empty();
        topics.forEach(function (topic) {
            $('#TopicsSearchResults').append('<p><a href="#" class="topicId' + topic.parentSubforumName + '">' + topic.name + '</a></p>');
        });

    });

    $.ajax({
        url: baseUrl + "/index/searchUsers/" + searchQuery
    }).then(function (users) {

        $('#UsersSearchResults').empty();
        users.forEach(function (user) {
            $('#UsersSearchResults').append('<p>' + user.username + '</p>');
        });

    });

    $('#searchResultPage').modal('show');

}

function loadSubforum(subforumId, followedForumsMode) {

    $.ajax({
        url: baseUrl + '/subforum/' + subforumId + '/topics'
    }).then(function (topics) {

        $.ajax({
            url: baseUrl + "/user/active/"
        }).then(function (user) {

            if (followedForumsMode == true) {

            }
            else {
                $('#subforumName').empty();
                $('#subforumName').append('<h3 id="subforumNameH3">' + subforumId + '</h3><a id="followSubforumLink" href="#">Follow</a>&nbsp;<a id="reportSubforumLink" href="#">Report</a><br>');
                $('#followSubforumLink').click(function () {
                    followSubforum();
                });
                $('#reportSubforumLink').click(function () {
                    reportSubforum();
                });
                $('#topics').empty();
                $('#addTopicButton').show();
            }

            topics.forEach(function (topic) {



                var tableRow = '<tr>' +
                    '<td>' +
                    '<table>' +
                    '<tbody>' +
                    '<tr>' +
                    '<td class="likeTopicRow"><a href="#" class="likeTopic' + topics[0].parentSubforumName + '">Like</a></td>' +
                    '<td class="topicIdRow"><a href="#" class="topicId' + topics[0].parentSubforumName + '">' + topic.name + '</a></td>' +
                    '<td class="saveTopicRow"><a href="#" class="saveTopic' + topics[0].parentSubforumName + '">Save</a></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td class="likesCount">' + (parseInt(topic.likes) - parseInt(topic.dislikes)) + '</td>' +
                    '<td class="submittedBy">submitted by ' + topic.author + '</td>';
                if (user != undefined) {
                    if (topic.author == user.username || user.role == "moderator" || user.role == "admin") {
                        tableRow += '<td><a href="#" class="deleteTopic' + topics[0].parentSubforumName + '">Delete</a>&nbsp;<a href="#" class="editTopic' + topics[0].parentSubforumName + '">Edit</a></td>';
                    }
                    else {
                        tableRow += '<td></td>';
                    }
                }
                else {
                    tableRow += '<td></td>';
                }

                tableRow += '</tr>' +
                    '<tr>' +
                    '<td class="dislikeTopicRow"><a href="#" class="dislikeTopic' + topics[0].parentSubforumName + '">Dislike</a></td>' +
                    '<td class="commentsCount">' + topic.comments.length + ' comments</td>' +
                    '<td class="reportTopicRow"><a href="#" class="reportTopic' + topics[0].parentSubforumName + '">Report</a></td>' +
                    '</tr>' +
                    '</tbody>' +
                    '</table>' +
                    '</td>'
                '</tr>';

                $('#topics').append(tableRow);
            });

            $('.topicId' + topics[0].parentSubforumName).click(function () {
                var clickedTopic = $(this).text();

                $.ajax({
                    url: baseUrl + '/subforum/load/' + topics[0].parentSubforumName + '/' + clickedTopic + ''
                }).then(function (topic) {

                    $('.modal').modal('hide');

                    $('#topicNameModal').empty();
                    $('#topicContentModal').empty();

                    $('#topicNameModal').append('<h4 class="modal-title">' + topic.name + '</h4>');

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

            $('.likeTopic' + topics[0].parentSubforumName).click(function () {
                var clickedTopicId = $(this).closest('table').find('.topicId' + topics[0].parentSubforumName).text();

                $.ajax({
                    url: baseUrl + '/topic/like/' + topics[0].parentSubforumName + '/' + clickedTopicId
                }).then(function (message) {
                    alert(message);
                    if($('#subforumNameH3').length == 0) {
                        refresh();
                    }
                    else {
                        loadSubforum(topics[0].parentSubforumName);
                    }
                });

            });

            $('.dislikeTopic' + topics[0].parentSubforumName).click(function () {
                var clickedTopicId = $(this).closest('table').find('.topicId' + topics[0].parentSubforumName).text();

                $.ajax({
                    url: baseUrl + '/topic/dislike/' + topics[0].parentSubforumName + '/' + clickedTopicId
                }).then(function (message) {
                    alert(message);
                    if($('#subforumNameH3').length == 0) {
                        refresh();
                    }
                    else {
                        loadSubforum(topics[0].parentSubforumName);
                    }
                });

            });

            $('.saveTopic' + topics[0].parentSubforumName).click(function () {
                var clickedTopicId = $(this).closest('table').find('.topicId' + topics[0].parentSubforumName).text();

                $.ajax({
                    url: baseUrl + '/topic/saveTopic/' + topics[0].parentSubforumName + '/' + clickedTopicId
                }).then(function (message) {
                    alert(message);
                    if($('#subforumNameH3').length == 0) {
                        refresh();
                    }
                    else {
                        loadSubforum(topics[0].parentSubforumName);
                    }
                });

            });

            $('.reportTopic' + topics[0].parentSubforumName).click(function () {
                var clickedTopicId = $(this).closest('table').find('.topicId' + topics[0].parentSubforumName).text();

                $('#reportSubforumIdInputName').val(topics[0].parentSubforumName);
                $('#reportTopicIdInputName').val(clickedTopicId);

                $('#reportTopic').modal('show');
            });

            $('.deleteTopic' + topics[0].parentSubforumName).click(function () {
                var clickedTopicId = $(this).closest('table').find('.topicId' + topics[0].parentSubforumName).text();

                $.ajax({
                    url: baseUrl + '/topic/delete/' + topics[0].parentSubforumName + '/' + clickedTopicId
                }).then(function (message) {
                    alert(message);
                    if($('#subforumNameH3').length == 0) {
                        refresh();
                    }
                    else {
                        loadSubforum(topics[0].parentSubforumName);
                    }
                });

            });

            $('.editTopic' + topics[0].parentSubforumName).click(function () {
                var clickedTopicId = $(this).closest('table').find('.topicId' + topics[0].parentSubforumName).text();

                topics.forEach(function (topic) {
                    if (topic.name == clickedTopicId) {

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

    });

}

function addTopic(e) {

    var formId = "addTopicForm";
    var activeSubforumId = $('#subforumNameH3').text();

    var form = $('#' + formId);
    e.preventDefault();

    var input = $('#' + formId + ' :input');

    var data = {};
    for (var i = 0; i < input.length; i++) {
        if (input[i].name) {
            data[input[i].name] = input[i].value;
        }
    }

    $.ajax({
        type: form.attr('method'),
        url: baseUrl + '/topic/create/' + activeSubforumId,
        contentType: "application/json",
        data: JSON.stringify(data),
        complete: function (data) {
            alert(data.responseText);
            $('.modal').modal('hide');
            loadSubforum(activeSubforumId);
        }
    });
}

function refresh() {
    location.reload(true);
}
