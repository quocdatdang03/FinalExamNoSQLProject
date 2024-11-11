package com.shopping.web.shoppingCart.service;

import com.shopping.common.entity.CartItem;
import com.shopping.common.entity.Product;
import com.shopping.common.entity.User;
import com.shopping.web.product.repository.ProductRepository;
import com.shopping.web.shoppingCart.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CartItem> getAllCartItemsOfCustomer(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Override
    public void addProduct(Integer quantity, String productId, User user) {
        Product product = productRepository.findById(productId).get();
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);

        // check whether this product was already existed in Shopping Cart :
        if(cartItem==null) {
            // in case add new product to shopping cart :
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setUser(user);
            cartItem.setQuantity(quantity);
        }
        else {
            // in case add update quantity for existed product in shopping cart :
            Integer productQuantityInDB = cartItem.getQuantity();
            cartItem.setQuantity(productQuantityInDB+quantity);
        }

        cartItemRepository.save(cartItem);
    }

    @Override
    public double updateProductQuantity(Integer quantity, String productId, User user) {
        Product product = productRepository.findById(productId).get();
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);

        if(quantity<=0) {
            cartItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
            return 0;
        }

        cartItem.setQuantity(quantity);

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        double totalPrice = updatedCartItem.getQuantity()*updatedCartItem.getProduct().getDiscountPrice();

        return totalPrice;
    }

    @Override
    public String deleteProduct(String productId, String customerId) {

        cartItemRepository.deleteByUserIdAndProductId(customerId, productId);

        return "ok";
    }
}
