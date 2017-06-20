$(document).ready(function(){

	$.ajax({
        url: "http://localhost:8080/reddit-clone/rest/subforum/topics"
    }).then(function(topics) {

        topics.forEach(function(topic) {
            var tableRow = '<tr>' + 
                            '<td>' + 
				                '<table border="1">' +
                                    '<tbody>' + 
                                        '<tr>' +
                                            '<td><a href="/reddit-clone/rest/topic/like/' + topic.parentSubforum.name + '/' + topic.name + '">Like</a></td>' +
                                            '<td>' + topic.name + '</td>' + 
                                        '</tr>' +
                                        '<tr>' +
                                            '<td>' + (parseInt(topic.likes) - parseInt(topic.dislikes)) + '</td>' + 
                                            '<td>submitted by ' + topic.author.name + '</td>' +
                                        '</tr>' +
                                        '<tr>' +
                                            '<td><a href="/reddit-clone/rest/topic/dislike/' + topic.parentSubforum.name + '/' + topic.name + '">Dislike</a></td>' +
                                            '<td>' + topic.comments.length + ' comments</td>' + 
                                        '</tr>' + 
                                    '</tbody>' + 
                                '</table>' + 
                            '</td>'
                            '</tr>'

            $('#topics').append(tableRow);

        });

    });
	
});
