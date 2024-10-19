package com.shopping.web.product.service;

import com.shopping.common.entity.Product;
import com.shopping.common.entity.ProductImage;
import com.shopping.web.product.repository.ProductRepository;
import com.shopping.web.productImage.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService{

    public static final int ITEMS_NUMBER_PER_PAGE = 5;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).get();
    }

    @Override
    public Page<Product> getProductsByPage(Integer pageNumber, String sortField, String sortDir, String keyword, String categoryId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber-1, ITEMS_NUMBER_PER_PAGE, sort);

        if(keyword!=null && !keyword.isBlank())
        {
            // in case filter by category and search with keyword :
            if(categoryId!=null && !categoryId.equals("0"))
            {
                return productRepository.searchWithKeywordAndFilterByCategory(keyword.trim(), categoryId, pageable);
            }
            // else in case search with keyword only
            return productRepository.searchByKeyword(keyword.trim(), pageable);
        }

        // in case filter by category without search with keyword
        if(categoryId!=null && !categoryId.equals("0")) {
            return productRepository.findProductsByCategoryId(categoryId, pageable);
        }

        return productRepository.findAll(pageable);
    }


}
