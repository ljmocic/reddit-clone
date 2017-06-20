$(document).ready(function(){
    
	$.ajax({
        url: "http://localhost:8080/reddit-clone/rest/index/subforums"
    }).then(function(subforums) {

        subforums.forEach(function(subforum) {
            $('#subforumsLinks').append(
                '<p><a href="/Reddit/subforum.html">' + subforum.name + " " + subforum.description + '</a></p>'
                );
        });

    });
	
});