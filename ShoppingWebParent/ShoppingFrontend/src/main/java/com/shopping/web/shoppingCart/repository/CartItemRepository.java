package com.shopping.web.shoppingCart.repository;

import com.shopping.common.entity.CartItem;
import com.shopping.common.entity.Product;
import com.shopping.common.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {

    public List<CartItem> findByUser(User user);

    public CartItem findByUserAndProduct(User user, Product product);

    @Transactional
    @Query(value = "{ 'user.id': ?0, 'product.id': ?1 }", delete = true)
    void deleteByUserIdAndProductId(String userId, String productId);

}
