package com.shopping.web.shoppingCart.controller;


import com.shopping.common.entity.User;
import com.shopping.common.exception.UserNotFoundException;
import com.shopping.web.shoppingCart.service.ShoppingCartServiceImpl;
import com.shopping.web.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cart")
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/add/{productId}/{quantity}")
    public String addProductToCart(
            @PathVariable("productId") String productId,
            @PathVariable("quantity") Integer quantity,
            HttpServletRequest request) {

        try {

            User user = getAuthenticatedCustomer(request);
            shoppingCartService.addProduct(quantity, productId, user);

        } catch (UserNotFoundException userNotFoundException) {
            return "notLogin";
        }

        return "success";
    }

    @PutMapping("/update/{productId}/{quantity}")
    public String updateProductQuantityInCart(
            @PathVariable("productId") String productId,
            @PathVariable("quantity") Integer quantity,
            HttpServletRequest request
    ) {
        try {

            User user = getAuthenticatedCustomer(request);
            double totalPriceOfCartItem = shoppingCartService.updateProductQuantity(quantity, productId, user);

            if((int)totalPriceOfCartItem==0)
                return String.valueOf(0);

            return String.valueOf(totalPriceOfCartItem);

        } catch (UserNotFoundException userNotFoundException) {
            return userNotFoundException.getMessage();
        }
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProductInCart(
            @PathVariable("productId") String productId,
            HttpServletRequest request
    ) {
        try {

            User user = getAuthenticatedCustomer(request);

            return shoppingCartService.deleteProduct(productId, user.getId());

        } catch (UserNotFoundException userNotFoundException) {

            return userNotFoundException.getMessage();

        }

    }

    // get Authenticated Customer :
    private User getAuthenticatedCustomer(HttpServletRequest request) throws UserNotFoundException {
        String userEmail = userService.getEmailOfAuthenticatedCustomer(request);
        User user = userService.getUserByEmail(userEmail);

        // if customer == null -> not login
        if(user==null)
            throw new UserNotFoundException("No Authenticated User");

        return user;
    }
}
