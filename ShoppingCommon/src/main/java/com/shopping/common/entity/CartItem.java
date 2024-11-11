package com.shopping.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection="cart_items")
public class CartItem {

    @Id
    private String id;

    @DBRef(lazy = false)
    private Product product;

    @DBRef(lazy = false)
    private User user;

    private int quantity;

    @Transient
    public double getTotalPrice() {
        return product.getDiscountPrice()*quantity;
    }
}
