package com.shopping.web.controller;

import com.shopping.common.entity.Category;
import com.shopping.web.category.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/")
    public String showHomePage(Model model)
    {
        List<Category> categoryList = categoryService.getEnabledCategories();
        model.addAttribute("categoryList", categoryList);
        return "index";
    }
}
