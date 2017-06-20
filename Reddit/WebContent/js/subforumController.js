$(document).ready(function(){

    // load main page
	$.ajax({
        url: "http://localhost:8080/reddit-clone/rest/user/active"
    }).then(function(user) {

        user.followedSubforums.forEach(function(subforum) {
            subforum.topics.forEach(function(topic) {
                var tableRow = '<tr>' + 
                                '<td>' + 
                                    '<table border="1">' +
                                        '<tbody>' + 
                                            '<tr>' +
                                                '<td><a href="/reddit-clone/rest/topic/like/' + topic.parentSubforumName + '/' + topic.name + '">Like</a></td>' +
                                                '<td>' + topic.name + '</td>' + 
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

            });
        });

    });
	
});
