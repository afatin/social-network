package org.example.service;

import org.example.entity.User;
import org.example.security.Role;
import org.example.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.searchUserByLogin(login);
        if (user != null) {
            return new UserPrincipal(user.getId(), user.getLogin(), user.getPassword(), Role.USER);
        }


        throw new UsernameNotFoundException("User with login '" + login + "' not found");
    }
}