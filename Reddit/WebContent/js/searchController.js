function subforumSearchResponse(data) {
    $('#SubforumsSearchResults').empty();
    $('#TopicsSearchResults').empty();
    $('#UsersSearchResults').empty();

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
    $('#SubforumsSearchResults').empty();
    $('#TopicsSearchResults').empty();
    $('#UsersSearchResults').empty();

    for (var i = 0; i < data.responseJSON.length; i++) {
        $('#TopicsSearchResults').append('<p><a href="#" id="' + data.responseJSON[i].parentSubforumName + '" class="topicIdSearch">' + data.responseJSON[i].name + '</a></p>');
    }

    $('.topicIdSearch').click(topicIdClickHandler);

    $('#advancedSearchTopics').modal('hide');
    $('#searchResultPage').modal('show');
}

function performSearch() {

    var searchQuery = $("#searchQuery").val();

    if (searchQuery == "") {
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