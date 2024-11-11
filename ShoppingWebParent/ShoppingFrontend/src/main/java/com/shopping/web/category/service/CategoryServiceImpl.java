package com.shopping.web.category.service;

import com.shopping.common.entity.Category;
import com.shopping.web.category.repository.CategoryRepository;
import com.shopping.web.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    public static final int ITEMS_NUMBER_PER_PAGE = 5;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Category> getCategoriesByPage(Integer pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber-1, ITEMS_NUMBER_PER_PAGE, sort);

        if(keyword!=null && !keyword.isBlank())
            return categoryRepository.searchByKeyword(keyword.trim(), pageable);

        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> getEnabledCategories() {
        return categoryRepository.findByEnabledTrue();
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public void deleteCategory(String id) {
        // delete product of category in advanced :
        productRepository.deleteByCategoryId(id);

        categoryRepository.deleteById(id);
    }
}
