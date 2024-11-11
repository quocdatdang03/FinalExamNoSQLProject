package com.shopping.web.shoppingCart.controller;

import com.shopping.common.entity.CartItem;
import com.shopping.common.entity.User;
import com.shopping.web.shoppingCart.service.ShoppingCartServiceImpl;
import com.shopping.web.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("")
    public String showProductInCart(Model model, HttpServletRequest request) {

        // get user
        User user = getAuthenticatedCustomer(request);

        List<CartItem> cartItems = shoppingCartService.getAllCartItemsOfCustomer(user);

        model.addAttribute("cartItems", cartItems);

        return "cart/cart";
    }

    // get Authenticated Customer :
    private User getAuthenticatedCustomer(HttpServletRequest request) {
        String customerEmail = userService.getEmailOfAuthenticatedCustomer(request);
        User user = userService.getUserByEmail(customerEmail);

        return user;
    }
}
