package org.example.service;

import org.example.entity.User;
import org.example.security.Role;
import org.example.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
        if (user == null) {
            throw new UsernameNotFoundException("User with login '" + login + "' not found");
        }
//        return new UserPrincipal(user.getId(), user.getLogin(), user.getPassword(), Role.USER);

        Role role = user.getRole();
        if (role == null) {
            role = Role.USER;
        }
        return new UserPrincipal(user.getId(), user.getLogin(), user.getPassword(), role);

    }
}