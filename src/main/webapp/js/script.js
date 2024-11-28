$(function () {
    $('[data-toggle="tooltip"]').tooltip()
    $('.toast').toast()

    if ($('.toast .toast-body').text()) {
        $('.toast').toast('show')
    }
})