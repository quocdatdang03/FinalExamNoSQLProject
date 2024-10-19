package com.shopping.web.productImage;

import com.shopping.common.entity.ProductImage;
import com.shopping.web.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    public ProductImage saveProductImage(ProductImage productImage, String productId) {
        productImage.setProductId(productId);
        return productImageRepository.save(productImage);
    }

}
