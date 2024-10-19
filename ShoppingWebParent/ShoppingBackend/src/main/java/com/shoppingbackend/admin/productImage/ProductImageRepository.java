package com.shoppingbackend.admin.productImage;

import com.shopping.common.entity.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends MongoRepository<ProductImage, String> {
    public void deleteByProductId(String productId);
}
