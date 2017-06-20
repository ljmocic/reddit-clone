$(document).ready(function(){

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
	
});
