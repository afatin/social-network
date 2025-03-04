package org.example.methods;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.utils.HibernateUtil;
import org.example.entity.User;

public class UserDAO {
    public void deleteUserById(Long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, userId);
            if (user != null) {
                session.remove(user);
                System.out.println("User deleted: " + user.getLogin());
            } else {
                System.out.println("User not found!");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
