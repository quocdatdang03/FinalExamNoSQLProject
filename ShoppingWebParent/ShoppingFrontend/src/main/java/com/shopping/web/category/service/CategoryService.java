package com.shopping.web.category.service;

import com.shopping.common.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategories();
    public Page<Category> getCategoriesByPage(Integer pageNumber, String sortField, String sortDir, String keyword);
    public List<Category> getEnabledCategories();
    public Category saveCategory(Category category);
    public Category getCategoryById(String id);
    public void deleteCategory(String id);

}
