function addTopic(e) {

    var formId = "addTopicForm";
    var activeSubforumId = $('#subforumNameH3').text();

    var form = $('#' + formId);
    e.preventDefault();

    var input = $('#' + formId + ' :input');

    var data = {};
    for (var i = 0; i < input.length; i++) {
        if (input[i].name) {
            if(input[i].type != "file") {
                data[input[i].name] = input[i].value;
            }
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

function uploadImage() {

    var file = $('#imageUpload')[0].files[0];

    $.ajax({
        url : baseUrl + "/topic/image",
        type : "POST",
        contentType : "multipart/form-data",
        dataType: "json",
        data: file,
        processData: false,
        async: false,
        complete: function(response) {
            //alert("File uploaded!");
            //alert(response.responseText);
            $("#AddTopicInputContent").val(response.responseText);
        }
    });
}

function topicIdClickHandler() {
    var clickedTopic = $(this).text();
    var clickedTopicParentSubforum = $(this).attr("id");

    $.ajax({
        url: baseUrl + '/subforum/load/' + clickedTopicParentSubforum + '/' + clickedTopic + ''
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
            $('#topicContentModal').append('<img class="img-responsive" src="' + topic.content + '" alt="Nema prikaza" />');
        }

        $('#showTopic').modal('show');
    });

}