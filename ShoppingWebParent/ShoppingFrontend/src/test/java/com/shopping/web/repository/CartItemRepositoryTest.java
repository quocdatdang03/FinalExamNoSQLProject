package com.shopping.web.repository;


import com.shopping.common.entity.CartItem;
import com.shopping.common.entity.Product;
import com.shopping.common.entity.User;
import com.shopping.web.shoppingCart.repository.CartItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    public void testSaveCartItem() {
        User user = new User();
        Product product = new Product();
        user.setId("6713656f3d54f18eca1ec591");
        product.setId("6713288870523478c6a5a1ca");
        int quantity = 2;

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        Assertions.assertThat(savedCartItem.getId()).isNotNull();
    }

//    @Test
//    public void testSaveManyCartItems() {
//        Customer customer1 = new Customer();
//        Product product1 = new Product();
//        customer1.setId(10);
//        product1.setId(3);
//        int quantity1 = 2;
//
//        Customer customer2 = new Customer();
//        Product product2 = new Product();
//        customer2.setId(2);
//        product2.setId(2);
//        int quantity2 = 1;
//
//        CartItem cartItem1 = new CartItem();
//        cartItem1.setCustomer(customer1);
//        cartItem1.setProduct(product1);
//        cartItem1.setQuantity(quantity1);
//
//        CartItem cartItem2 = new CartItem();
//        cartItem2.setCustomer(customer2);
//        cartItem2.setProduct(product2);
//        cartItem2.setQuantity(quantity2);
//
//
//
//        Iterable<CartItem> cartItems = cartItemRepository.saveAll(List.of(cartItem1, cartItem2));
//
//        Assertions.assertThat(cartItems).size().isGreaterThan(0);
//    }

//    @Test
//    public void testFindByCustomer() {
//        Customer customer = new Customer();
//        customer.setId(10);
//        List<CartItem> cartItemList = cartItemRepository.findByCustomer(customer);
//
//        cartItemList.forEach(System.out::println);
//
//        Assertions.assertThat(cartItemList).size().isGreaterThan(0);
//    }
}
