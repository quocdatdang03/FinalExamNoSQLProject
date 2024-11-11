var btnLogout;
$(document).ready(function() {
    btnLogout = $('#btnLogout');
    btnLogout.click(function(e) {
        e.preventDefault();
        $('#logoutForm').submit();
    })
    showProductQuantityInCart();
})

function showProductQuantityInCart() {
    $('.product-item').each(function(index) {
        $('#productQuantityInCart').text(index+1)
    })
}