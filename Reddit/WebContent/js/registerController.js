$(document).ready(function(){

    var form = $('#registerForm');

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
