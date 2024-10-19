package com.shoppingbackend.admin.security;

import com.shopping.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShoppingUserDetails implements UserDetails {

    private User user;

    public ShoppingUserDetails(User user) {

        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> userRoles = new ArrayList<SimpleGrantedAuthority>();
        userRoles.add(new SimpleGrantedAuthority(this.user.getRole()));
        return userRoles;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName() {
        // Tạo method getFullName để getFullName của object User:
        return this.user.getFirstName() +" "+this.user.getLastName();
    }

    public void setFirstName(String firstName)
    {
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName)
    {
        this.user.setLastName(lastName);
    }

}
