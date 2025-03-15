package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public User createUser(String login, String name, String password) {
        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setPassword(password);  // Хеширование пароля происходит внутри сеттера
        entityManager.persist(user);
        return user;
    }

    public List<User> getAllUsers() {
        String query = "SELECT u FROM User u";
        return entityManager.createQuery(query, User.class).getResultList();
    }

    public User getUserById(Integer id) {
        return entityManager.find(User.class, id);
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
