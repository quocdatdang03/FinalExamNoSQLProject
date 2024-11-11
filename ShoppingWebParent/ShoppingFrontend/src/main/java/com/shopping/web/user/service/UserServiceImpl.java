package com.shopping.web.user.service;

import com.shopping.common.entity.User;
import com.shopping.web.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        // encode password of customer :
        encodePassword(user);

        // enable user account by default:
        user.setEnabled(true);

        // by default is CUSTOMER role:
        user.setRole("CUSTOMER");

        user.setCreatedTime(new Date());

        return userRepository.save(user);
    }


    @Override
    public String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Object principal = request.getUserPrincipal();

        // if user not login -> return null
        if(principal==null)
            return null;

        String userEmail = null;

        userEmail = request.getUserPrincipal().getName();

        return userEmail;
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
