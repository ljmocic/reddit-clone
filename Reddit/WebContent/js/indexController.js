$(document).ready(function(){
    
	$.ajax({
        url: "http://localhost:8080/reddit-clone/rest/index/subforums"
    }).then(function(subforums) {

        subforums.forEach(function(subforum) {
            $('#subforumsLinks').append(
                '<p><a href="/Reddit/subforum.html">' + subforum.name + " " + subforum.description + '</a></p>'
                );

            $('#subforumsSidebarList').append('<a href="#">' + subforum.name + '</a>&nbsp;');
        });

    });

    $.ajax({
        url: "http://localhost:8080/reddit-clone/rest/index/users"
    }).then(function(users) {

        users.forEach(function(user) {
            var tableRow = '<tr>' + 
                                '<td>' + user.username + '</td>' +
                                '<td><a href="/Reddit/rest/user/update/admin/' + user.username + '">Postavi kao administratora</a></td>' +
                                '<td><a href="/Reddit/rest/user/update/moderator/' + user.username + '">Postavi kao moderatora</a></td>' +
                                '<td><a href="/Reddit/rest/user/update/user/' + user.username + '">Postavi kao korisnika</a></td>' +
                            '</tr>';

            $('#users tbody').append(tableRow);
        });
    });

    $.ajax({
        url: "http://localhost:8080/reddit-clone/rest/user/active"
    }).then(function(user) {

        user.savedTopics.forEach(function(savedTopic) {
            $('#profileSavedTopic').append('<p>' + savedTopic.name + '</p>');
        });

        user.savedComments.forEach(function(savedComment) {
            $('#profileSavedComments').append('<p>' + savedComment.name + '</p>');
        });

        user.likedTopics.forEach(function(likedTopic) {
            $('#profileLikes').append('<p>' + likedTopic.name + '</p>');
        });

        user.dislikedTopics.forEach(function(dislikedTopic) {
            $('#profileDislikes').append('<p>' + dislikedTopic.name + '</p>');
        });

        user.followedSubforums.forEach(function(subforum) {
            $('#profileFollowedSubforums').append('<p>' + subforum.name + '</p>');
        });

    });
	
});