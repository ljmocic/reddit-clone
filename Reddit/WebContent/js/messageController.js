$(document).ready(function(){

    var form = $('#sendMessageForm');

    form.submit(function (e) {

        e.preventDefault();

        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: form.serialize(),
            success: function (data) {
                alert(data);
                $('.modal').modal('hide');
            },
            error: function (data) {
                alert('An error occurred.');
            },
        });
    });

});