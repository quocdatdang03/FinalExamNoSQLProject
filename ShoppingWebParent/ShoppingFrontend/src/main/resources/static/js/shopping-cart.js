$(document).ready(function() {

    // change quantity product in shopping cart
    $('.btnMinusQuantityCartItem').on('click', function (e) {
        e.preventDefault();

        const productId = $(this).attr('productId')
        const quantityInput = $('#quantityInput'+productId)
        const quantity = parseInt(quantityInput.val())
        quantityInput.val(quantity-1)

        const quantityInputVal = quantityInput.val();

        updateProductQuantity(productId, quantityInputVal)
    })

    $('.btnPlusQuantityCartItem').on('click', function (e) {
        e.preventDefault();

        const productId = $(this).attr('productId')
        const quantityInput = $('#quantityInput'+productId)
        const quantity = parseInt(quantityInput.val())
        quantityInput.val(quantity+1)

        const quantityInputVal = quantityInput.val();

        updateProductQuantity(productId, quantityInputVal)
    })

    updateCartTotalPrice();
    showProductQuantityInCart();

    // format total price each product:
    $('.product-total-price').number(true,2);

    // remove product from cart
    $('.btn-remove').on('click', function () {
        const productId = $(this).attr('productId');

        handleRemoveProductFromCart(productId);
    })
})

function updateProductQuantity(productId, quantityInputVal) {

    const requestURL = contextPath+"cart/update/"+productId+"/"+quantityInputVal

    $.ajax({
        type : "PUT",
        url : requestURL,
        beforeSend : function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (totalPrice) {
        if(totalPrice==='0') {
            // delete current product
            $('#productItemDiv'+productId).remove()

            showEmptyCartMessage();
        }
        else {
            const formattedTotalPrice = formatNumber(totalPrice);

            // update total price of each product on cart page
            $('#productTotalPrice'+productId).text(formattedTotalPrice)

            // update total price of entire shopping cart on cart page
            updateCartTotalPrice();
        }
    })
}

function updateCartTotalPrice() {
    // update total price of entire shopping cart on cart page
    let cartTotalPrice = 0;
    $('.product-total-price').each(function(index, element) {
        const totalPriceEachProduct = parseFloat($(element).text().replace(/,/g, ''))
        cartTotalPrice += totalPriceEachProduct;
    })

    $('#cartTotalPrice').text(formatNumber('$ '+cartTotalPrice));

    showEmptyCartMessage();
    showProductQuantityInCart();
}

function formatNumber(number) {
    const formattedNumber = $.number(number,2)

    return formattedNumber
}

function handleRemoveProductFromCart(productId) {

    const requestURL = contextPath+"cart/delete/"+productId;

    $.ajax({
        type : "DELETE",
        url : requestURL,
        beforeSend : function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        if(response==='ok') {
            const productItemDiv = $('#productItemDiv'+productId);
            productItemDiv.remove();

            // update total price of entire shopping cart on cart page
            updateCartTotalPrice();
        }
    })

}

function showEmptyCartMessage() {

    if($('.product-item').length === 0) {
        // Show empty cart message
        $('#cartContent').html(`
                    <div class="w-100 d-flex flex-column gap-5 pt-5">
                        <h1 class="text-center">Your cart is empty</h1>
                        <a class="btn btn-primary d-inline-block mx-auto mt-3" href="${contextPath}">
                            Continue shopping
                        </a>
                    </div>
                `);
    }

}

function showProductQuantityInCart() {
    $('.product-item').each(function(index) {
        $('#productQuantityInCart').text(index+1)
    })
}