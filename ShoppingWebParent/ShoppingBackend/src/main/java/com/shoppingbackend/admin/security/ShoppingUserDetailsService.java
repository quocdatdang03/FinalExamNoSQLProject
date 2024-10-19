package com.shoppingbackend.admin.security;

import com.shopping.common.entity.User;
import com.shoppingbackend.admin.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShoppingUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // Cách 2 : return custom UserDetails của ta : ShoppingUserDetails:
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // get User từ Database bằng email:
        User user = userRepository.findByEmail(email);

        ShoppingUserDetails customUserDetails;
        // check if there is user in DB:
        if(user!=null)
        {
            customUserDetails = new ShoppingUserDetails(user);
            return customUserDetails;
        }
        else
            throw new UsernameNotFoundException("There is no user with email: "+email);
    }
}
