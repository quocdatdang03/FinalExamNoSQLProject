package com.shopping.web.category.controller;

import com.shopping.common.entity.Category;
import com.shopping.web.category.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/c/{categoryId}")
    public String showCategoryList(Model model) {

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return showCategoriesByPage(1,"asc","name",null,model);
    }

    @GetMapping("/page/{pageNumber}")
    public String showCategoriesByPage(
            @PathVariable("pageNumber") Integer pageNumber,
            @Param("sortDir") String sortDir,
            @Param("sortField") String sortField,
            @Param("keyword") String keyword,
            Model model
    ) {
        if(sortDir==null || sortDir.isEmpty())
            sortDir = "asc";
        if(sortField==null || sortField.isEmpty())
            sortField = "name";

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        Page<Category> pageResult = categoryService.getCategoriesByPage(pageNumber, sortField, sortDir, keyword);

        // get information for pagination:
        List<Category> categoryList = pageResult.getContent();
        int totalPages = pageResult.getTotalPages();
        long totalElements = pageResult.getTotalElements();
        int firstPageNumber = ((pageNumber-1)*categoryService.ITEMS_NUMBER_PER_PAGE)+1;
        int lastPageNumber = pageNumber * categoryService.ITEMS_NUMBER_PER_PAGE;
        if(lastPageNumber>=totalElements)
            lastPageNumber = (int) totalElements;

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);
        model.addAttribute("firstPageNumber", firstPageNumber);
        model.addAttribute("lastPageNumber", lastPageNumber);
        return "category/category";
    }

    @GetMapping("/new")
    public String showCategoryForm(@ModelAttribute("category")Category category, Model model) {
        model.addAttribute("pageTitle", "Create New Category");
        return "category/category_form";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        // Nếu ID là chuỗi rỗng, đặt nó thành null để MongoDB tự động tạo ID mới
        if (category.getId() == null || category.getId().trim().isEmpty()) {
            category.setId(null);
        }

        categoryService.saveCategory(category);
        return "redirect:/categories";
    }


    @GetMapping("/edit/{id}")
    public String showCategoryFormEdit(@PathVariable("id") String id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Edit Category");
        return "category/category_form";
    }

    @GetMapping("/delete/{id}")
    private String deleteCategory(@PathVariable("id") String id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
