package com.shopping.web.security;


import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public UserDetailsService userDetailsService()
    {
        return new ShoppingUserDetailsService();
    }

    // Bean for PasswordEncoder:
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // register custom AuthenticationProvider với AuthenticationManagerBuilder:
        auth
                .authenticationProvider(authenticationProvider());
    }


    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        // DaoAuthenticationProvider : chính là implementation class của inteface AuthenticationProvider :
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/cart").authenticated()
                .anyRequest()
                    .permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .defaultSuccessUrl("/").permitAll()
                .and()
                    .logout()
                    .permitAll()
                .and()
                    .rememberMe() // enable remember me
                        .key("1212abcdefsad123456789") // set fixed private key để cookie remember-me vẫn được lưu khi restart project
                        .rememberMeParameter("remember-me") // chỉ định param nhận được từ bên form login là remember-me
                        .tokenValiditySeconds(7*24*60*60)// set expiration time of Cookie : 1 weeks (by default 2 weeks);;
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // ignore authentication với các folder dưới:
        web.ignoring().antMatchers("/images/**", "/js/**", "/css/**", "/lib/**", "/richtext/**", "/scss/**", "/staticLogin/**");
    }
}
