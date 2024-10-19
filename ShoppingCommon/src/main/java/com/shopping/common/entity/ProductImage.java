package com.shopping.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "product_images")
public class ProductImage {
    @Id
    private String id;
    private String name;

    private String productId;

    public ProductImage(String id, String name, Product product) {
        this.id = id;
        this.name = name;
        this.productId = product.getId();
    }

    public ProductImage(String name, Product product) {
        this.name = name;
        this.productId = product.getId();
    }

    @Transient
    public String getProductExtraImagePath() {
        return "/product-images/"+productId+"/extras/"+this.getName();
    }
}
