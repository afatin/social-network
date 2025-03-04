package org.example.methods;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.utils.HibernateUtil;
import org.example.entity.User;

public class SubscriptionDAO {
    public void subscribe(Long subscriberId, Long authorId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User subscriber = session.get(User.class, subscriberId);
            User author = session.get(User.class, authorId);

            if (subscriber != null && author != null) {
                subscriber.getSubscriptions().add(author);
                session.persist(subscriber); // Обновляем подписчика с новой подпиской
                System.out.println("User " + subscriber.getLogin() + " подписался на " + author.getLogin());
            } else {
                System.out.println("Пользователь не найден!");
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
