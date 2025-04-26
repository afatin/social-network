package org.example.dto;

import org.example.entity.User;
import org.example.security.Role;

public class RegisterRequest {
    private String login;
    private String password;
    private String name;
    private Role role;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }


    public User toUser() {
        User user = new User();
        user.setLogin(this.login);
        user.setPassword(this.password);
        user.setName(this.name);
        return user;
    }
}
