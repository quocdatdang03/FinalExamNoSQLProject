package com.shopping.web.user.controller;

import com.shopping.common.entity.User;
import com.shopping.web.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/register")
    public String showSignUpForm(@ModelAttribute("user") User user) {
        return "user/register_form";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Register account successfully! Enter account to login");
        userService.saveUser(user);
        return "redirect:/login";
    }


}
