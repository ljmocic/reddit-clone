var baseUrl = 'http://localhost:8080/reddit-clone/rest';

$(document).ready(function () {

    $('#navbarLoggedIn').hide();
    $('#userActions').hide();
    $('#moderatorActions').hide();
    $('#adminActions').hide();

    $('#imageUploadDiv').hide();

    $('#showProfileLink').click(function () {
        loadProfileDetails();
    });

    checkLoggedInStatus();

    loadSubforumLinks();

    //loadRecommendations();

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
        addSubforumForm(e, addSubforumFormId);
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

    $('#AddTopicInputType').change(function (e) {
        if($('#AddTopicInputType').val() == "image") {
            $('#imageUploadDiv').show();
        }
        else {
            $('#imageUploadDiv').hide();
        }
    });
    
    $('#imageUpload').change(function (e) {
        uploadImage();
    });

    $('#logout').click(function () {
        logoutUser();
    });

});

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
            $('#profileSavedTopic').append('<p><a href="#" id="' + savedTopic.parentSubforumName + '" class="topicId">' + savedTopic.name + '</a></p>');
        });

        $('#profileSavedComments').empty();
        user.savedComments.forEach(function (savedComment) {
            $('#profileSavedComments').append('<p>' + savedComment.name + '</p>');
        });

        $('#profileLikes').empty();
        user.likedTopics.forEach(function (likedTopic) {
            $('#profileLikes').append('<p><a href="#" id="' + likedTopic.parentSubforumName + '" class="topicId">' + likedTopic.name + '</a></p>');
        });

        $('#profileDislikes').empty();
        user.dislikedTopics.forEach(function (dislikedTopic) {
            $('#profileDislikes').append('<p><a href="#" id="' + dislikedTopic.parentSubforumName + '" class="topicId">' + dislikedTopic.name + '</a></p>');
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

        $('.topicId').click(topicIdClickHandler);

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

function refresh() {
    location.reload(true);
}
