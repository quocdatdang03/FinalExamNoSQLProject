$(document).ready(function() {

    // change quantity product in product detail page
    $('.btnMinus').on('click', function (e) {
        e.preventDefault();

        const productId = $(this).attr('productId')
        const quantityInput = $('#quantityInput'+productId)
        const quantity = parseInt(quantityInput.val())
        console.log(quantity);
        if(quantity===1) {
            $(this).addClass('disabled')
        }
        if(quantity>1) {
            quantityInput.val(quantity-1)
        }
    })

    $('.btnPlus').on('click', function (e) {
        e.preventDefault();
        $('.btnMinus').removeClass('disabled')
        const productId = $(this).attr('productId')
        const quantityInput = $('#quantityInput'+productId)
        const quantity = parseInt(quantityInput.val())
        quantityInput.val(quantity+1)
    })

})