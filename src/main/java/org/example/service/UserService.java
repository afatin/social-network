package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.dto.DTOConverter;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DTOConverter dtoConverter;

    @Transactional
    public User createUser(String login, String name, String password) {
        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setPassword(password);
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

    public UserDTO getUserById(Integer id) {
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

    @Transactional
    public void updateUserName(Integer userId, String newName) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setName(newName);
            entityManager.merge(user);
        }
    }

    @Transactional
    public void deleteUser(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
