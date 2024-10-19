package com.shoppingbackend.admin.product.service;

import com.shopping.common.entity.Category;
import com.shopping.common.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public List<Product> getAllProducts();
    public Product getProductById(String id);
    public Page<Product> getProductsByPage(Integer pageNumber, String sortField, String sortDir, String keyword, String categoryId);
    public Product saveProduct(Product product);
    public void deleteProduct(String id);

}
