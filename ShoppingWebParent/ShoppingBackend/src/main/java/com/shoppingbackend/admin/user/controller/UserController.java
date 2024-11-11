package com.shoppingbackend.admin.user.controller;

import com.shopping.common.entity.Category;
import com.shopping.common.entity.User;
import com.shoppingbackend.admin.security.ShoppingUserDetails;
import com.shoppingbackend.admin.user.repository.UserRepository;
import com.shoppingbackend.admin.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("")
    public String showUserList(Model model) {

        return showUsersByPage(1,"asc","firstName",null,model);
    }

    @GetMapping("/page/{pageNumber}")
    public String showUsersByPage(
            @PathVariable("pageNumber") Integer pageNumber,
            @Param("sortDir") String sortDir,
            @Param("sortField") String sortField,
            @Param("keyword") String keyword,
            Model model
    ) {
        if(sortDir==null || sortDir.isEmpty())
            sortDir = "asc";
        if(sortField==null || sortField.isEmpty())
            sortField = "firstName";

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        Page<User> pageResult = userService.getUsersByPage(pageNumber, sortField, sortDir, keyword);

        // get information for pagination:
        List<User> userList = pageResult.getContent();
        int totalPages = pageResult.getTotalPages();
        long totalElements = pageResult.getTotalElements();
        int firstPageNumber = ((pageNumber-1)*userService.ITEMS_NUMBER_PER_PAGE)+1;
        int lastPageNumber = pageNumber * userService.ITEMS_NUMBER_PER_PAGE;
        if(lastPageNumber>=totalElements)
            lastPageNumber = (int) totalElements;

        model.addAttribute("userList", userList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);
        model.addAttribute("firstPageNumber", firstPageNumber);
        model.addAttribute("lastPageNumber", lastPageNumber);
        return "user/users";
    }

    @GetMapping("/{userId}/enabled/{enabledStatus}")
    public String updateEnabledStatus(
            @PathVariable("userId") String userId,
            @PathVariable("enabledStatus") boolean enabledStatus,
            RedirectAttributes redirectAttributes
    )
    {
        userService.updateEnabledStatus(userId, enabledStatus);
        String message;
        if(enabledStatus==true)
            message = "Activate User (id:"+userId+") successfully  !";
        else
            message = "Deactivate User (id:"+userId+") successfully!";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
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
        return "redirect:/users/account_details";
    }

}
