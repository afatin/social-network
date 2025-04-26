package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.dto.DTOConverter;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DTOConverter dtoConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(String login, String name, String rawPassword, Role role) {
        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(rawPassword));
        entityManager.persist(user);
        user.setRole(role);
        return user;
    }

    @Transactional
    public User createUser(String login, String name, String rawPassword) {
        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(rawPassword));
        entityManager.persist(user);
        return user;
    }

    public List<UserDTO> getAllUsers() {
        String query = "SELECT u FROM User u";
        List<User> users = entityManager.createQuery(query, User.class).getResultList();
        return users.stream()
                .map(dtoConverter::convertToUserDTO)
                .toList();
    }

    public UserDTO getUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return user != null ? dtoConverter.convertToUserDTO(user) : null;
    }

    public List<UserDTO> searchUserByName(String nameQuery) {
        String query = "SELECT u FROM User u WHERE u.name LIKE :nameQuery";
        List<User> users = entityManager.createQuery(query, User.class)
                .setParameter("nameQuery", "%" + nameQuery + "%")
                .getResultList();
        return users.stream()
                .map(dtoConverter::convertToUserDTO)
                .toList();
    }

    public User searchUserByLogin(String loginQuery) {
        String query = "SELECT u FROM User u WHERE u.login LIKE :loginQuery";
        try {
            return entityManager.createQuery(query, User.class)
                    .setParameter("loginQuery", "%" + loginQuery + "%")
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public void updateUserName(Long userId, String newName) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setName(newName);
            entityManager.merge(user);
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
