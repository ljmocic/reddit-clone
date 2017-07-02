function loadRecommendations() {
    $.ajax({
        url: baseUrl + "/index/recommendations"
    }).then(function (data) {
        for (var i = 0; i < data.length; i++) {
            $('#recommendedTopicsList').append('<a href="#" id="' + data[i].parentSubforumName + '" class="topicIdRecommend">' + data[i].name + '</a><br>');
        }

        $(".topicIdRecommend").click(topicIdClickHandler);

    });
}