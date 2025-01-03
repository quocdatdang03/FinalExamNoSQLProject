package com.shopping.web.product.service;

import com.shopping.common.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public List<Product> getAllProducts();
    public List<Product> getAllProductsByCategoryId(String categoryId);
    public Product getProductById(String id);
    public Page<Product> getProductsByPage(Integer pageNumber, String sortField, String sortDir, String keyword, String categoryId);

}
