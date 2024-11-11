package com.shopping.web.user.service;

import com.shopping.common.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    public User getUserByEmail(String email);
    public User saveUser(User user);
    public String getEmailOfAuthenticatedCustomer(HttpServletRequest request);
    public User updateAccountDetail(User userInForm);
}
