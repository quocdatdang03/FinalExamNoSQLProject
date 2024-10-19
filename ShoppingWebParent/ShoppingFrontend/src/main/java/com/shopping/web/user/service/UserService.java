package com.shopping.web.user.service;

import com.shopping.common.entity.User;

public interface UserService {
    public User getUserByEmail(String email);
    public User saveUser(User user);
}
