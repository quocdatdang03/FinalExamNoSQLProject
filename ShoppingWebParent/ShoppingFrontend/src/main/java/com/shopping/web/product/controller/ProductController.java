package com.shopping.web.product.controller;

import com.shopping.common.entity.Category;
import com.shopping.common.entity.Product;
import com.shopping.web.category.service.CategoryServiceImpl;
import com.shopping.web.product.service.ProductServiceImpl;
import com.shopping.web.productImage.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @ModelAttribute("categoryList")
    public List<Category> getCategoryList() {
        return categoryService.getAllCategories();
    }


    @GetMapping("")
    public String showProductList(Model model) {

        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);

        return showProductsByPage(1,"asc","name",null,"0",model);
    }

    @GetMapping("/page/{pageNumber}")
    public String showProductsByPage(
            @PathVariable("pageNumber") Integer pageNumber,
            @Param("sortDir") String sortDir,
            @Param("sortField") String sortField,
            @Param("keyword") String keyword,
            @Param("categoryId") String categoryId,
            Model model
    ) {
        if(sortDir==null || sortDir.isEmpty())
            sortDir = "asc";
        if(sortField==null || sortField.isEmpty())
            sortField = "name";

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        Page<Product> pageResult = productService.getProductsByPage(pageNumber, sortField, sortDir, keyword, categoryId);

        // get information for pagination:
        List<Product> productList = pageResult.getContent();
        Map<String, String> categoryNameOfProducts = new HashMap<>();
        for(Product product : productList) {
            String categoryName = categoryService.getCategoryById(product.getCategoryId()).getName();
            categoryNameOfProducts.put(product.getId(), categoryName);
        }

        int totalPages = pageResult.getTotalPages();
        long totalElements = pageResult.getTotalElements();
        int firstPageNumber = ((pageNumber-1)*categoryService.ITEMS_NUMBER_PER_PAGE)+1;
        int lastPageNumber = pageNumber * categoryService.ITEMS_NUMBER_PER_PAGE;
        if(lastPageNumber>=totalElements)
            lastPageNumber = (int) totalElements;

        model.addAttribute("productList", productList);
        model.addAttribute("categoryNameOfProducts", categoryNameOfProducts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);
        model.addAttribute("firstPageNumber", firstPageNumber);
        model.addAttribute("lastPageNumber", lastPageNumber);

        if(categoryId!=null)
           model.addAttribute("selectedCategoryId", categoryId);
        return "product/product";
    }

    @GetMapping("/p/{id}")
    public String showProductDetail(@PathVariable("id") String id, Model model)
    {
        Product product = productService.getProductById(id);

        model.addAttribute("product", product);

        model.addAttribute("pageTitle", product.getName());

        return "product/product_details";
    }

}
