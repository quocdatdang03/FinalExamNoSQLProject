package com.shoppingbackend.admin.user.service;

import com.shopping.common.entity.User;
import com.shoppingbackend.admin.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService{

    public static final int ITEMS_NUMBER_PER_PAGE = 5;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getUsersByPage(Integer pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber-1, ITEMS_NUMBER_PER_PAGE, sort);

        if(keyword!=null && !keyword.isBlank())
            return userRepository.searchByKeyword(keyword.trim(), pageable);

        return userRepository.findByRoleCustomer(pageable);
    }

    @Override
    public void updateEnabledStatus(String userId, boolean enabledStatus) {

        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User is not exists with given id: "+userId));

        user.setEnabled(enabledStatus);

        userRepository.save(user);
    }

    @Override
    public User updateAccountDetail(User userInForm) {
        User userInDb = userRepository.findById(userInForm.getId()).get();

        userInForm.setCreatedTime(userInDb.getCreatedTime());

        // check if password of user in form is empty -> Keep current password in DB
        if(userInForm.getPassword().isEmpty()) {
            userInForm.setPassword(userInDb.getPassword());
        }
        else {
            encodePassword(userInForm);
        }

        return userRepository.save(userInForm);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
