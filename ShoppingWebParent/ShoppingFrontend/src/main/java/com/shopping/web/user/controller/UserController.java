package com.shopping.web.user.controller;

import com.shopping.common.entity.User;
import com.shopping.web.security.ShoppingUserDetails;
import com.shopping.web.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping("/account_details")
    public String showAccountDetails(@AuthenticationPrincipal ShoppingUserDetails userDetails, Model model) {
        // obj userDetails : chứa các thông tin của user khi login thành công
        User user = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        // set new firstName and lastName for UserDetails (if firstName and lastName of User have been updated):
        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());


        return "user/account_details";
    }

    @PostMapping("/account_details")
    public String showAccountDetails(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ) {

        userService.updateAccountDetail(user);

        redirectAttributes.addFlashAttribute("message", "Your account details have been updated");
        return "redirect:/account_details";
    }


}
