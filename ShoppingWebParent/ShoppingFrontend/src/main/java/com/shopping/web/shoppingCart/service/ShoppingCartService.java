package com.shopping.web.shoppingCart.service;

import com.shopping.common.entity.CartItem;
import com.shopping.common.entity.User;

import java.util.List;

public interface ShoppingCartService {

    public List<CartItem> getAllCartItemsOfCustomer(User user);
    public void addProduct(Integer quantity, String productId, User user);
    public double updateProductQuantity(Integer quantity, String productId, User user);
    public String deleteProduct(String productId, String customerId);

}
