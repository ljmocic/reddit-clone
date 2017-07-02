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
                        $('#topicContentModal').append('<div><img class="img-responsive" src="' + topic.content + '" alt="Nema prikaza"></div>');
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
                    if ($('#subforumNameH3').length == 0) {
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
                    if ($('#subforumNameH3').length == 0) {
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
                    if ($('#subforumNameH3').length == 0) {
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
                    if ($('#subforumNameH3').length == 0) {
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

                            var input = $('#editTopicForm :input');

                            var data = {};
                            for (var i = 0; i < input.length; i++) {
                                if (input[i].name) {
                                    data[input[i].name] = input[i].value;
                                }
                            }

                            $.ajax({
                                type: form.attr('method'),
                                url: baseUrl + '/topic/update/' + topic.parentSubforumName + '/' + topic.name,
                                contentType: "application/json",
                                data: JSON.stringify(data),
                                complete: function (data) {
                                    alert(data.responseText);
                                    $('.modal').modal('hide');
                                    loadSubforum(topic.parentSubforumName);
                                }
                            });
                        });

                    }
                });




            });

        });

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

            var imageTag = '<img src="' + subforum.icon + '" alt="" />';
            
            $('#subforumsLinks').append(
                '<p>' + imageTag + '<a class="subforumLink" id="' + subforum.name + '" href="#">' + subforum.name + " - " + subforum.description + '</a></p>'
            );

            $('#subforumsSidebarList').append(imageTag + '<a href="#" class="subforumLink" id="' + subforum.name + '">' + subforum.name + '</a><br>');
        });

        $(".subforumLink").click(function () {
            var clickedButtonId = $(this).attr('id');
            $('.modal').modal('hide');
            loadSubforum(clickedButtonId);
        });

    });
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

function addSubforumForm(e, formId, responseAction) {

    e.preventDefault();

    var file = $('#iconUpload')[0].files[0];

    $.ajax({
        url : baseUrl + "/subforum/icon",
        type : "POST",
        contentType : "multipart/form-data",
        dataType: "json",
        data: file,
        processData: false,
        async: false,
        complete: function(response) {
            alert("File uploaded!");
            alert(response.responseText);

            var form = $('#' + formId);

            var input = $('#' + formId + ' :input');

            var data = {};
            for (var i = 0; i < input.length; i++) {
                if (input[i].name) {
                    if(input[i].type != "file") {
                        data[input[i].name] = input[i].value;
                    }
                }
            }

            data["icon"] = response.responseText;

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
    });
}