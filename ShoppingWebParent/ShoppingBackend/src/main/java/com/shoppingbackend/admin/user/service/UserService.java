package com.shoppingbackend.admin.user.service;

import com.shopping.common.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    public User getUserByEmail(String email);
    public List<User> getAllUsers();
    public Page<User> getUsersByPage(Integer pageNumber, String sortField, String sortDir, String keyword);
    public void updateEnabledStatus(String userId, boolean enabledStatus);
    public User updateAccountDetail(User userInForm);

}
